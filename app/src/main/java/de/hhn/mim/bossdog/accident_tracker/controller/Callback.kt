package de.hhn.mim.bossdog.accident_tracker.controller

import android.util.Log
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.DeltaDataType

public val heartRateCallback = object : MeasureCallback {
    override fun onAvailabilityChanged(dataType: DeltaDataType<*, *>, availability: Availability) {
        if (availability is DataTypeAvailability) {
            // Handle availability change.
            Log.d("debug: ", "heart rate availability changed")
        }
    }

    override fun onDataReceived(data: DataPointContainer) {
        // Inspect data points.
        data.getData(DataType.Companion.HEART_RATE_BPM).forEach { value ->
            Log.d("Heart Rate: ", "val: ${value.value}, acc: ${value.accuracy}")
        }
    }
}

public val vO2Callback = object : MeasureCallback {
    override fun onAvailabilityChanged(dataType: DeltaDataType<*, *>, availability: Availability) {
        if (availability is DataTypeAvailability) {
            // Handle availability change.
            Log.d("debug: ", "vo2 max availability changed")
        }
    }

    override fun onDataReceived(data: DataPointContainer) {
        // Inspect data points.
        data.getData(DataType.Companion.VO2_MAX).forEach { value ->
            Log.d("VO2_Max: ", "val: ${value.value}, acc: ${value.accuracy}")
        }
    }
}