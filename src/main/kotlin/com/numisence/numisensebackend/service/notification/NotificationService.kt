package com.numisence.numisensebackend.service.notification

import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class NotificationService(
    private val messagingTemplate: SimpMessagingTemplate
) {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Pushes a real-time alert to any farmer currently viewing a specific Farm Zone.
     * Useful for IoT automated triggers like water pumps.
     * The mobile app should subscribe to: /topic/alerts/zone/{farmZoneId}
     */
    fun sendActuatorAlert(farmZoneId: UUID, message: String) {
        val destination = "/topic/alerts/zone/$farmZoneId"

        log.info("Pushing real-time WebSocket alert to {}: {}", destination, message)

        // Wrapping the string in a map automatically converts it to a clean JSON response
        val payload = mapOf(
            "alertType" to "ACTUATOR_TRIGGERED",
            "message" to message,
            "timestamp" to System.currentTimeMillis()
        )

        messagingTemplate.convertAndSend(
            destination,
            payload as Any
        )
    }

    /**
     * Pushes a notification when a new bid is placed on a marketplace listing.
     * The mobile app should subscribe to: /topic/marketplace/bids/{sellerId}
     */
    fun sendMarketplaceBidAlert(sellerId: UUID, listingTitle: String, bidAmount: BigDecimal, bidderName: String) {
        val destination = "/topic/marketplace/bids/$sellerId"

        log.info("Pushing real-time bid alert to seller {}: {} bid on {}", sellerId, bidAmount, listingTitle)

        val payload = mapOf(
            "alertType" to "NEW_MARKETPLACE_BID",
            "listingTitle" to listingTitle,
            "bidAmount" to bidAmount,
            "bidderName" to bidderName,
            "message" to "New bid of $$bidAmount received from $bidderName for $listingTitle",
            "timestamp" to System.currentTimeMillis()
        )

        messagingTemplate.convertAndSend(
            destination,
            payload as Any
        )
    }

    /**
     * Pushes a notification when an asynchronous AI Diagnostic sync is complete.
     * The mobile app should subscribe to: /topic/diagnostics/{userId}
     */
    fun sendDiagnosticCompletionAlert(userId: UUID, zoneName: String, diseaseDetected: String?) {
        val destination = "/topic/diagnostics/$userId"

        val statusMessage = if (diseaseDetected != null) {
            "Analysis complete for $zoneName: Detected $diseaseDetected."
        } else {
            "Analysis complete for $zoneName: Crop appears healthy."
        }

        log.info("Pushing diagnostic completion alert to user {}: {}", userId, statusMessage)

        val payload = mapOf(
            "alertType" to "DIAGNOSTIC_COMPLETE",
            "zoneName" to zoneName,
            "diseaseDetected" to diseaseDetected,
            "message" to statusMessage,
            "timestamp" to System.currentTimeMillis()
        )

        messagingTemplate.convertAndSend(destination, payload)
    }

    /**
     * Broadcasts a general cooperative or system alert to all active users.
     * The mobile app should subscribe to: /topic/system/broadcast
     */
    fun sendSystemBroadcast(message: String, severity: String = "INFO") {
        val destination = "/topic/system/broadcast"

        log.info("Broadcasting system alert ({}): {}", severity, message)

        val payload = mapOf(
            "alertType" to "SYSTEM_BROADCAST",
            "severity" to severity,
            "message" to message,
            "timestamp" to System.currentTimeMillis()
        )

        messagingTemplate.convertAndSend(
            destination,
            payload as Any
        )
    }
}