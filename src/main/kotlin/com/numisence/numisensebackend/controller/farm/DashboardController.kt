package com.numisence.numisensebackend.controller.farm

import com.numisence.numisensebackend.dto.farm.FarmDashboardSummaryResponse
import com.numisence.numisensebackend.service.farm.FarmManagementService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/api/v1/dashboard")
class DashboardController(
    private val farmManagementService: FarmManagementService
) {

    /**
     * GET /api/v1/dashboard/summary/{farmZoneId}
     * This is the BFF pattern in action.
     */
    @GetMapping("/summary/{farmZoneId}")
    fun getDashboardSummary(
        @PathVariable farmZoneId: UUID
    ):  ResponseEntity<Any> { //return type was ResponseEntity<FarmDashboardSummaryResponse> -- reason: DB is empty
        //val summary = farmManagementService.getDashboardSummary(farmZoneId) DB is emptu

        // Manually convert the string to UUID, or handle it as a String if your repository supports it
        // This stops the MethodArgumentTypeMismatchException
        val zoneId = try {
            UUID.fromString(farmZoneId.toString())
        } catch (e: IllegalArgumentException) {
            // Handle the case where the mobile app sends an invalid ID
            // For now, let's just log and return a default/error
            null
        }

        val summary = mapOf(
            "activeTasksCount" to 3,
            "overallMoistureLevel" to 42.5,
            "activeAlerts" to 1,
            "weather" to mapOf(
                "temperatureCelsius" to 24.5,
                "condition" to "Sunny",
                "windSpeedKmh" to 12.0
            )
        )
        return ResponseEntity.ok(summary)
    }

    /**
     * GET /api/v1/dashboard/map/{farmerId}
     * UC4.3: Returns all zones with their PostGIS boundaries for the mobile map.
     */
    @GetMapping("/map/{farmerId}")
    fun getGeospatialFarmMap(@PathVariable farmerId: UUID): ResponseEntity<List<Map<String, Any>>> {
        val zones = farmManagementService.getFarmerZones(farmerId)

        // Map entities to DTOs, safely extracting geometries
        val response = zones.map { zone ->
            mapOf(
                "id" to zone.id.toString(),
                "name" to zone.name,
                "areaHectares" to (zone.areaHectares ?: 0.0),
                "status" to zone.status
                // Note: To return raw GeoJSON to the client, you'd integrate a JTS-to-GeoJSON serializer here.
            )
        }
        return ResponseEntity.ok(response)
    }

    /**
     * POST /api/v1/dashboard/tasks
     * UC4.2: Manage Farm Tasks
     */
    @PostMapping("/tasks")
    fun createTask(@RequestBody request: CreateTaskRequest): ResponseEntity<Map<String, String>> {
        val task = farmManagementService.createTask(
            zoneId = request.zoneId,
            farmerId = request.assignedTo,
            title = request.title,
            description = request.description,
            dueDate = request.dueDate
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(mapOf("taskId" to task.id.toString(), "status" to "Task created"))
    }
}

data class CreateTaskRequest(
    val zoneId: UUID,
    val assignedTo: UUID,
    val title: String,
    val description: String,
    val dueDate: LocalDate
)