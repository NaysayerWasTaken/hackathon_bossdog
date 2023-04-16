/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package de.hhn.mim.bossdog.accident_tracker.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.health.services.client.HealthServices
import androidx.health.services.client.awaitWithException
import androidx.health.services.client.data.DataType
import androidx.lifecycle.lifecycleScope
import de.hhn.mim.bossdog.accident_tracker.complication.AccidentService
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("debug", "started main activity")
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.BODY_SENSORS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                Log.d("debug: ","created activity")
                val healthClient = HealthServices.getClient(this /*context*/)
                val measureClient = healthClient.measureClient
                lifecycleScope.launch {
                    val capabilities = measureClient.getCapabilitiesAsync().awaitWithException()
                    val supportsHeartRate = DataType.HEART_RATE_BPM in capabilities.supportedDataTypesMeasure
                    val supportsVO2Max = DataType.VO2_MAX in capabilities.supportedDataTypesMeasure
                    Log.d("debug: ", "Heart rate support:" + supportsHeartRate.toString() +
                            "\n VO2 support:" + supportsVO2Max)
                }
                Log.d("debug", "start accident ser")
                Intent(this, AccidentService::class.java).also { intent ->
                    startService(intent)
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

