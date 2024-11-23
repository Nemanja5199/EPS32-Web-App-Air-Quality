package com.iot.Air.Quality.repository

import com.iot.Air.Quality.model.SensorData
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface SensorDataRepository : JpaRepository<SensorData, Long> {
    fun findFirstByOrderByTimestampDesc(): SensorData?

    fun findByTimestampBetween(
        from: LocalDateTime,
        to: LocalDateTime,
    ): List<SensorData>
}
