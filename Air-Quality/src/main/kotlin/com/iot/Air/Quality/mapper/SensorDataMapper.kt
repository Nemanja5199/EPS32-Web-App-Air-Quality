package com.iot.Air.Quality.mapper

import com.iot.Air.Quality.dto.SensorDataDto
import com.iot.Air.Quality.model.SensorData
import java.time.LocalDateTime

object SensorDataMapper {

    fun toModel(dto: SensorDataDto): SensorData =
        SensorData(
            temperature = dto.temperature,
            humidity = dto.humidity,
            timestamp = LocalDateTime.now()

        )

    fun toDto(entity: SensorData): SensorDataDto =
        SensorDataDto(
            temperature = entity.temperature,
            humidity = entity.humidity
        )
}
