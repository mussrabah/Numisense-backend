package com.numisence.numisensebackend.dto.farm

// A Backend-For-Frontend (BFF) aggregation object
data class FarmDashboardSummaryResponse(
    val activeTasksCount: Int,
    val overallMoistureLevel: Double?,
    val activeAlerts: List<String>,
    val weatherSummary: WeatherSummaryDto? // Fetched from External API
)

data class WeatherSummaryDto(
    val temperatureCelsius: Double,
    val condition: String,
    val windSpeedKmh: Double
)