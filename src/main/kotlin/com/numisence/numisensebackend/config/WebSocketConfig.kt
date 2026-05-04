package com.numisence.numisensebackend.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        // The prefix used for messages pushing OUT from the server to the mobile client
        config.enableSimpleBroker("/topic")

        // The prefix used for messages coming IN from the mobile client to the server
        config.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        // The actual handshake endpoint the KMP app will connect to.
        // We allow all origin patterns for local development.
        registry.addEndpoint("/ws-numiterra")
            .setAllowedOriginPatterns("*")
            .withSockJS() // Fallback option if native WebSockets are blocked
    }
}