package com.iot.Air.Quality.mapper

import com.iot.Air.Quality.dto.SensorDataDto
import com.iot.Air.Quality.model.SensorData
import java.time.LocalDateTime

object SensorDataMapper {

    fun toModel(dto: SensorDataDto): SensorData =
        SensorData(
            temperature = dto.temperature,
            humidity = dto.humidity,
            co2 = dto.co2,
            tvoc = dto.tvco,
            timestamp = LocalDateTime.now()

        )

    fun toDto(entity: SensorData): SensorDataDto =
        SensorDataDto(
            temperature = entity.temperature,
            humidity = entity.humidity,
            co2 = entity.co2,
            tvco = entity.tvoc
        )
}
