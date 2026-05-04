package com.numisence.numisensebackend.controller.farm

import com.numisence.numisensebackend.dto.farm.FarmDashboardSummaryResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/dashboard")
class DashboardController {

    /**
     * GET /api/v1/dashboard/summary/{farmZoneId}
     * This is the BFF pattern in action. The mobile app makes ONE call here,
     * and this controller orchestrates gathering tasks, IoT data, and weather.
     */
    @GetMapping("/summary/{farmZoneId}")
    fun getDashboardSummary(
        @PathVariable farmZoneId: UUID
    ): ResponseEntity<FarmDashboardSummaryResponse> {

        // TODO: Inject TaskService to get actual tasks
        // TODO: Inject IotTelemetryService to get latest moisture
        // TODO: Use Spring RestClient to hit OpenWeatherMap API

        // Mocked aggregation for now
        val summary = FarmDashboardSummaryResponse(
            activeTasksCount = 3,
            overallMoistureLevel = 42.5,
            activeAlerts = listOf("Pump #1 Maintenance Required"),
            weatherSummary = null
        )

        return ResponseEntity.ok(summary)
    }
}