package de.hhn.mim.bossdog.accident_tracker.model

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.hl7.fhir.r5.model.CodeableConcept
import org.hl7.fhir.r5.model.DecimalType
import org.hl7.fhir.r5.model.Enumerations
import org.hl7.fhir.r5.model.Observation
import org.hl7.fhir.r5.model.PrimitiveType
import org.hl7.fhir.r5.model.Quantity

object ValueBuffer {
    private const val BUFFER_SIZE = 5

    @Serializable
    private val buffer = mutableListOf<Observation>()
    public suspend fun add(client: HttpClient, time: Instant, value: Double){
        val ob = Observation()
        val con = CodeableConcept()
        con.addCoding("Wear", "MDC_ECG_ELEC_POTL", "Smartwatch")
        ob.status = Enumerations.ObservationStatus.FINAL
        ob.code = con
        ob.value = Quantity(value)


        buffer.add(ob)
        Log.d("Debug", "buffer size: " + buffer.size.toString())
        if (buffer.size == BUFFER_SIZE){
            Log.d("Info", buffer.toString())
            client.post("http://10.0.2.2:8080/dplist") {
                contentType(ContentType.Application.Json)
                setBody(buffer)
            }
            buffer.clear()
        }
    }
}