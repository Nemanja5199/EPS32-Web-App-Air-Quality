package com.iot.Air.Quality.config

import com.iot.Air.Quality.handler.SensorWebSocketHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
    @Autowired
    private lateinit var webSocketHandler: SensorWebSocketHandler

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        println("Registering WebSocket handler")
        registry.addHandler(webSocketHandler, "/ws/sensor")
            .setAllowedOrigins("*")
        println("WebSocket handler registered successfully")
    }
}
