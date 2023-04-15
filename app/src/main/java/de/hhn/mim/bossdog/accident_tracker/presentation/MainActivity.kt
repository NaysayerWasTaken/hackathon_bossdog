/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package de.hhn.mim.bossdog.accident_tracker.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.health.services.client.HealthServices
import androidx.health.services.client.HealthServicesClient
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.MeasureClient
import androidx.health.services.client.awaitWithException
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.DeltaDataType
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import de.hhn.mim.bossdog.accident_tracker.R
import de.hhn.mim.bossdog.accident_tracker.presentation.theme.Accident_trackerTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BODY_SENSORS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                Log.d("debug: ","created activity")
                val healthClient = HealthServices.getClient(this /*context*/)
                val measureClient = healthClient.measureClient
                lifecycleScope.launch {
                    val capabilities = measureClient.getCapabilitiesAsync().awaitWithException()
                    val supportsHeartRate = DataType.HEART_RATE_BPM in capabilities.supportedDataTypesMeasure
                    Log.d("debug: ", supportsHeartRate.toString())
                    if (supportsHeartRate) {
                        setContent {
                            WearApp(measureClient)
                        }
                    } else {
                        Log.d("debug: ", "doesn't support heart rate")
                    }
                }

            }

            else -> {
                // You can directly ask for the permission.
                requestPermissions(
                    arrayOf(Manifest.permission.BODY_SENSORS),
                    200)
            }
        }

    }
}

@Composable
fun WearApp(measureClient: MeasureClient) {
    Accident_trackerTheme {
        /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
         * version of LazyColumn for wear devices with some added features. For more information,
         * see d.android.com/wear/compose.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .selectableGroup(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ToggleSend(measureClient)
        }
    }
}

@Composable
fun ToggleSend(measureClient: MeasureClient) {
    val sending = remember {
        mutableStateOf(false)
    }
    if (!sending.value) {
        Text(text = "Send data", modifier = Modifier.padding(15.dp))
        Button(onClick = {sending.value = true
            measureClient.registerMeasureCallback(DataType.Companion.HEART_RATE_BPM, heartRateCallback)
            Log.d("debug: ", "start")
                         },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Green
            )
        ) {
            Image(painter = painterResource(id = R.drawable.baseline_send_24), contentDescription = "xyz")
        }
    } else {
        Text(text = "Stop sending", modifier = Modifier.padding(15.dp))
        Button(onClick = {sending.value = false
            Log.d("debug: ", "stop")
            runBlocking {
                measureClient.unregisterMeasureCallbackAsync(DataType.Companion.HEART_RATE_BPM, heartRateCallback)
            }
                         },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red
            )
        ) {
            Image(painter = painterResource(id = R.drawable.baseline_stop_24), contentDescription = "xyz")
        }
    }

}

val heartRateCallback = object : MeasureCallback {
    override fun onAvailabilityChanged(dataType: DeltaDataType<*, *>, availability: Availability) {
        if (availability is DataTypeAvailability) {
            // Handle availability change.
            Log.d("debug: ", "availability changed")
        }
    }

    override fun onDataReceived(data: DataPointContainer) {
        // Inspect data points.
        data.getData(DataType.Companion.HEART_RATE_BPM).forEach {value ->
            Log.d("Datapoint: ", "val: ${value.value}, acc: ${value.accuracy}")
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(HealthServices.getClient(LocalContext.current).measureClient)
}

