package com.iot.Air.Quality.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.iot.Air.Quality.dto.SensorDataDto
import com.iot.Air.Quality.error.SensorError
import com.iot.Air.Quality.model.SensorData
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap

@Component
class SensorWebSocketHandler : TextWebSocketHandler() {
    private val sessions = mutableSetOf<WebSocketSession>()
    private val objectMapper = ObjectMapper()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        runCatching {
            sessions.add(session)
            println("WebSocket connection established")
        }.getOrElse { e ->
            SensorError.NetworkError(
                message = "Failed to establish WebSocket connection: ${e.message}",
                operation = "connect"
            )
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        runCatching {
            sessions.remove(session)
            println("WebSocket connection closed")
        }.getOrElse { e->
            SensorError.NetworkError(
                message = "Failed to close WebSocket connection: ${e.message}",
                operation = "disconnect"
            )
        }

    }

    fun broadcastSensorData(sensorData: SensorDataDto)= runCatching {
        val message = TextMessage(objectMapper.writeValueAsString(sensorData))
        sessions.forEach { session ->
            if (session.isOpen) {
                session.sendMessage(message)
            }
        }
    }.getOrElse { e->
            SensorError.NetworkError(
            message = "Failed to broadcast sensor data: ${e.message}",
            operation = "broadcast"
        )

    }
}