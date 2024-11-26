package com.iot.Air.Quality.dto

data class SensorDataDto(
    val temperature: Float,
    val humidity: Float,
    val co2: Int,
    val tvco: Int
)
