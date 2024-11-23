package com.iot.Air.Quality.service

import com.github.michaelbull.result.*
import com.iot.Air.Quality.dto.SensorDataDto
import com.iot.Air.Quality.error.SensorError
import com.iot.Air.Quality.mapper.SensorDataMapper
import com.iot.Air.Quality.repository.SensorDataRepository
import org.springframework.stereotype.Service

@Service
class SensorService(private val sensorRepository: SensorDataRepository) {
    fun addReading(data: SensorDataDto): Result<SensorDataDto, SensorError> =
        runCatching {
            val sensorData = SensorDataMapper.toModel(data)
            sensorRepository.save(sensorData)
            data
        }.mapError { e ->
            SensorError.DatabaseError(
                message = "Failed to save sensor reading: ${e.message}",
                operation = "save",
            )
        }

    fun getReading(): Result<SensorDataDto, SensorError> =
        sensorRepository.findFirstByOrderByTimestampDesc()
            ?.let { Ok(SensorDataMapper.toDto(it)) }
            ?: Err(
                SensorError.DatabaseError(
                    message = "No sensor data found",
                    operation = "read"
                )
            )
}
