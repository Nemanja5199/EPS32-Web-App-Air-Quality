package com.iot.Air.Quality.error

sealed class SensorError {
    abstract val message: String

    data class HardwareError(
        override val message: String,
        val sensorId: String,
    ) : SensorError()

    data class DatabaseError(
        override val message: String,
        val operation: String,
    ) : SensorError()

    data class NetworkError(
        override val message: String,
        val operation: String,
    ) : SensorError()

    data class ValidationError(
        override val message: String,
        val field: String,
        val value: Any?,
    ) : SensorError()
}
