package com.numisence.numisensebackend.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaConfig {

    /**
     * Topic for incoming high-frequency sensor data.
     * We use 3 partitions to allow for future horizontal scaling of consumer microservices.
     */
    @Bean
    fun telemetryInTopic(): NewTopic {
        return TopicBuilder.name("telemetry.in")
            .partitions(3)
            .replicas(1)
            .build()
    }

    /**
     * Topic for outgoing commands to physical/simulated IoT actuators (e.g., water pumps).
     */
    @Bean
    fun actuatorOutTopic(): NewTopic {
        return TopicBuilder.name("actuator.out")
            .partitions(3)
            .replicas(1)
            .build()
    }
}