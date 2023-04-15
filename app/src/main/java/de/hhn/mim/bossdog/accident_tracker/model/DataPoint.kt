package de.hhn.mim.bossdog.accident_tracker.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class DataPoint constructor(val time: Instant, val value: Double)