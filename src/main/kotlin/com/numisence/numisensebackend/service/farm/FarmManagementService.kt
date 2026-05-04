package com.numisence.numisensebackend.service.farm

import com.numisence.numisensebackend.domain.farm.FarmZone
import com.numisence.numisensebackend.domain.farm.Task
import com.numisence.numisensebackend.dto.farm.FarmDashboardSummaryResponse
import com.numisence.numisensebackend.dto.farm.WeatherSummaryDto
import com.numisence.numisensebackend.repository.farm.FarmZoneRepository
import com.numisence.numisensebackend.repository.farm.TaskRepository
import com.numisence.numisensebackend.repository.identity.FarmerRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Service
class FarmManagementService(
    private val farmZoneRepository: FarmZoneRepository,
    private val taskRepository: TaskRepository,
    private val farmerRepository: FarmerRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun getDashboardSummary(zoneId: UUID): FarmDashboardSummaryResponse {
        log.info("Aggregating Dashboard BFF data for zone {}", zoneId)

        // 1. Fetch real pending tasks count from DB
        val pendingTasksCount = taskRepository.countByZoneIdAndStatus(zoneId, "PENDING")

        // 2. In a real scenario, we'd inject IotTelemetryService here to get the last moisture reading
        val currentMoisture = 42.5

        // 3. Optional: Hit external Weather API via RestClient
        val mockWeather = WeatherSummaryDto(temperatureCelsius = 24.5, condition = "Sunny", windSpeedKmh = 12.0)

        return FarmDashboardSummaryResponse(
            activeTasksCount = pendingTasksCount,
            overallMoistureLevel = currentMoisture,
            activeAlerts = listOf("All systems optimal"),
            weatherSummary = mockWeather
        )
    }

    @Transactional(readOnly = true)
    fun getFarmerZones(farmerId: UUID): List<FarmZone> {
        return farmZoneRepository.findAllByFarmerId(farmerId)
    }

    @Transactional
    fun createTask(zoneId: UUID, farmerId: UUID, title: String, description: String, dueDate: LocalDate): Task {
        val zone = farmZoneRepository.findById(zoneId).orElseThrow { IllegalArgumentException("Zone not found") }
        val farmer = farmerRepository.findById(farmerId).orElseThrow { IllegalArgumentException("Farmer not found") }

        val task = Task(
            zone = zone,
            assignedTo = farmer,
            title = title,
            description = description,
            dueDate = dueDate,
            status = "PENDING"
        )
        return taskRepository.save(task)
    }
}