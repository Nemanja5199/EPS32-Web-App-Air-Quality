package com.iot.Air.Quality.controller

import com.github.michaelbull.result.binding
import com.github.michaelbull.result.mapBoth
import com.iot.Air.Quality.dto.SensorDataDto
import com.iot.Air.Quality.error.SensorError
import com.iot.Air.Quality.service.SensorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sensor")
class SensorController(private val sensorService: SensorService) {
    @PostMapping
    fun addReading(
        @RequestBody data: SensorDataDto,
    ): ResponseEntity<out Any> =
        binding {
            val savedData = sensorService.addReading(data).bind()
            ResponseEntity.ok(savedData)
        }.mapBoth(
            success = { response -> response },
            failure = { error ->
                when (error) {
                    is SensorError.DatabaseError ->
                        ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(
                                mapOf(
                                    "message" to error.message,
                                    "operation" to error.operation,
                                ),
                            )

                    else ->
                        ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(
                                mapOf(
                                    "message" to "An unexpected error occurred",
                                ),
                            )
                }
            },
        )

    @GetMapping
    fun getReading(): ResponseEntity<out Any> = binding {
        val sensorData = sensorService.getReading().bind()
        ResponseEntity.ok(sensorData)
    }.mapBoth(
        success = { response -> response },
        failure = { error ->
            when (error) {
                is SensorError.DatabaseError ->
                    ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                            mapOf(
                                "message" to error.message,
                                "operation" to error.operation,
                            )
                        )
                else ->
                    ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                            mapOf(
                                "message" to "An unexpected error occurred",
                            )
                        )
            }
        }
    )
}
