package de.hhn.mim.bossdog.accident_tracker.complication

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.health.services.client.HealthServices
import androidx.health.services.client.HealthServicesClient
import androidx.health.services.client.MeasureClient
import androidx.health.services.client.data.DataType
import de.hhn.mim.bossdog.accident_tracker.controller.heartRateCallback
import de.hhn.mim.bossdog.accident_tracker.controller.vO2Callback

class AccidentService: Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var acc: Sensor? = null
    private lateinit var healthClient: HealthServicesClient
    private lateinit var measureClient: MeasureClient
    private var sending = false

    override fun onCreate() {
        Log.d("debug", "created service")
        Toast.makeText(this, "Service started!", Toast.LENGTH_LONG).show()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL)
        healthClient = HealthServices.getClient(this /*context*/)
        measureClient = healthClient.measureClient
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onSensorChanged(event: SensorEvent) {
        val y = event.values[1] - 9.81
        if (y<-1){
            Log.d("debug", "trigger hit")
            if (!sending){
                measureClient.registerMeasureCallback(DataType.Companion.HEART_RATE_BPM, heartRateCallback)
                measureClient.registerMeasureCallback(DataType.Companion.VO2_MAX, vO2Callback)
                sending = true
            }

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //yes
    }
}