/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package de.hhn.mim.bossdog.accident_tracker.presentation

import android.graphics.drawable.Icon
import android.os.Bundle
import android.widget.Switch
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleButton
import androidx.wear.compose.material.ToggleButtonColors
import androidx.wear.compose.material.ToggleButtonDefaults
import de.hhn.mim.bossdog.accident_tracker.R
import de.hhn.mim.bossdog.accident_tracker.presentation.theme.Accident_trackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp("Android")
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
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
            ToggleSend()
        }
    }
}

@Composable
fun ToggleSend() {
    val sending = remember {
        mutableStateOf(false)
    }
    if (!sending.value) {
        Text(text = "Send data", modifier = Modifier.padding(15.dp))
        Button(onClick = {sending.value = true},
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Green
            )
        ) {
            Image(painter = painterResource(id = R.drawable.baseline_send_24), contentDescription = "xyz")
        }
    } else {
        Text(text = "Stop sending", modifier = Modifier.padding(15.dp))
        Button(onClick = {sending.value = false},
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red
            )
        ) {
            Image(painter = painterResource(id = R.drawable.baseline_stop_24), contentDescription = "xyz")
        }
    }

}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}
