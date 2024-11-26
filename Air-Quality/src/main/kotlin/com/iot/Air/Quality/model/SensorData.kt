package com.iot.Air.Quality.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "sensor_data")
data class SensorData(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    val temperature: Float,
    @Column(nullable = false)
    val humidity: Float,
    @Column(nullable = false)
    val co2: Int,
    @Column(nullable = false)
    val tvoc: Int,
    @Column(nullable = false)
    val timestamp: LocalDateTime? = null,
)
