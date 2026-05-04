package com.numisence.numisensebackend.service.iot

import com.numisence.numisensebackend.domain.iot.DeviceActionLog
import com.numisence.numisensebackend.domain.iot.Telemetry
import com.numisence.numisensebackend.repository.iot.DeviceActionLogRepository
import com.numisence.numisensebackend.repository.iot.IotDeviceRepository
import com.numisence.numisensebackend.repository.iot.TelemetryRepository
import com.numisence.numisensebackend.service.notification.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IotTelemetryService(
    private val telemetryRepository: TelemetryRepository,
    private val iotDeviceRepository: IotDeviceRepository,
    private val deviceActionLogRepository: DeviceActionLogRepository,
    private val notificationService: NotificationService,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Kafka Consumer listening to high-throughput IoT events.
     * In a production environment, 'message' would be deserialized from a JSON string into a DTO.
     * Expected JSON format: {"macAddress": "A1:B2:C3:D4:E5:F6", "value": 45.2}
     */
    @KafkaListener(topics = ["telemetry.in"], groupId = "numiterra-backend-group")
    @Transactional
    fun consumeTelemetry(message: String) {
        log.info("Received Kafka Telemetry Event: {}", message)

        // TODO: Replace with proper Jackson ObjectMapper deserialization
        // Mock parsing logic for demonstration
        val macAddress = "MOCK_MAC_ADDRESS" // Extract from message
        val readingValue = 45.2             // Extract from message

        val device = iotDeviceRepository.findByMacAddress(macAddress)

        if (device != null) {
            // 1. Save the high-frequency telemetry reading
            val telemetry = Telemetry(
                device = device,
                readingValue = readingValue
            )
            telemetryRepository.save(telemetry)

            // 2. Evaluate thresholds (Business Logic Rule Engine)
            evaluateAgronomicThresholds(device.id!!, readingValue)

        } else {
            log.warn("Telemetry received for unknown MAC Address: {}", macAddress)
        }
    }

    private fun evaluateAgronomicThresholds(deviceId: java.util.UUID, moistureLevel: Double) {
        // Example Rule: If moisture drops below 30%, trigger the water pump automatically.
        val optimalMoistureMin = 30.0

        if (moistureLevel < optimalMoistureMin) {
            log.warn("Moisture level {} is below threshold {}. Triggering AI_AUTO Irrigation.", moistureLevel, optimalMoistureMin)

            val device = iotDeviceRepository.findById(deviceId).orElseThrow()

            // Log the action to the database for audit and dashboard reporting
            val actionLog = DeviceActionLog(
                device = device,
                actionType = "TURN_ON",
                triggeredBy = "AI_AUTO"
            )
            deviceActionLogRepository.save(actionLog)

            // Phase 4 Complete: Push a command back out to Kafka topic 'actuator.out'
            val actuatorPayload = """{"macAddress":"${device.macAddress}", "command":"TURN_ON", "value": 100}"""
            kafkaTemplate.send("actuator.out", actuatorPayload)

            // Phase 4 Complete: Push a WebSocket alert to the KMP Mobile App
            if (device.farmZone != null) {
                notificationService.sendActuatorAlert(
                    farmZoneId = device.farmZone!!.id!!,
                    message = "Warning: Moisture level critically low ($moistureLevel%). Water Pump [${device.macAddress}] activated automatically."
                )
            }
        }
    }
}