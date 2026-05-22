package com.numisence.numisensebackend.controller.iot

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/iot")
class IotController {


    var simulatedDevices = mutableListOf<MutableMap<String, Any>>()

    @GetMapping("/telemetry/{zoneId}")
    fun getZoneTelemetry(@PathVariable zoneId: String): ResponseEntity<List<Map<String, Any>>> {
        // We provide mock simulated telemetry so the KMP UI comes to life!

        val devices = mutableListOf<MutableMap<String, Any>>(
            mutableMapOf(
                "id" to "sns-001",
                "name" to "Zone 1 Soil Moisture",
                "category" to "SENSOR",
                "type" to "MOISTURE",
                "status" to "ONLINE",
                "currentValue" to (40.0 + Math.random() * 5.0),
                "unit" to "%"
            ),
            mutableMapOf(
                "id" to "act-001",
                "name" to "Main Water Pump",
                "category" to "ACTUATOR",
                "type" to "PUMP",
                "status" to "ONLINE",
                "currentValue" to 0.0,
                "unit" to ""
            ),

            mutableMapOf(
                "id" to "sns-002",
                "name" to "Zone 2 Soil Moisture",
                "category" to "SENSOR",
                "type" to "MOISTURE",
                "status" to "ONLINE",
                "currentValue" to (35.0 + Math.random() * 10.0),
                "unit" to "%"
            ),
            mutableMapOf(
                "id" to "sns-003",
                "name" to "Greenhouse Temperature",
                "category" to "SENSOR",
                "type" to "TEMPERATURE",
                "status" to "ONLINE",
                "currentValue" to (20.0 + Math.random() * 8.0),
                "unit" to "°C"
            ),
            mutableMapOf(
                "id" to "sns-004",
                "name" to "Ambient Humidity",
                "category" to "SENSOR",
                "type" to "HUMIDITY",
                "status" to "ONLINE",
                "currentValue" to (55.0 + Math.random() * 15.0),
                "unit" to "%"
            ),
            mutableMapOf(
                "id" to "sns-005",
                "name" to "Water Tank Level",
                "category" to "SENSOR",
                "type" to "LEVEL",
                "status" to "ONLINE",
                "currentValue" to (60.0 + Math.random() * 30.0),
                "unit" to "%"
            ),
            mutableMapOf(
                "id" to "sns-006",
                "name" to "Soil pH Sensor",
                "category" to "SENSOR",
                "type" to "PH",
                "status" to "ONLINE",
                "currentValue" to (6.0 + Math.random()),
                "unit" to "pH"
            ),
            mutableMapOf(
                "id" to "sns-007",
                "name" to "Light Intensity Sensor",
                "category" to "SENSOR",
                "type" to "LIGHT",
                "status" to "ONLINE",
                "currentValue" to (5000.0 + Math.random() * 2000.0),
                "unit" to "lux"
            ),
            mutableMapOf(
                "id" to "sns-008",
                "name" to "Wind Speed Sensor",
                "category" to "SENSOR",
                "type" to "WIND",
                "status" to "OFFLINE",
                "currentValue" to (5.0 + Math.random() * 10.0),
                "unit" to "km/h"
            ),
            mutableMapOf(
                "id" to "sns-009",
                "name" to "Rain Detector",
                "category" to "SENSOR",
                "type" to "RAIN",
                "status" to "ONLINE",
                "currentValue" to (Math.random() * 10.0),
                "unit" to "mm"
            ),
            mutableMapOf(
                "id" to "act-002",
                "name" to "Irrigation Valve A",
                "category" to "ACTUATOR",
                "type" to "VALVE",
                "status" to "ONLINE",
                "currentValue" to 1.0,
                "unit" to ""
            ),
            mutableMapOf(
                "id" to "act-003",
                "name" to "Irrigation Valve B",
                "category" to "ACTUATOR",
                "type" to "VALVE",
                "status" to "OFFLINE",
                "currentValue" to 0.0,
                "unit" to ""
            ),
            mutableMapOf(
                "id" to "act-004",
                "name" to "Greenhouse Fan",
                "category" to "ACTUATOR",
                "type" to "FAN",
                "status" to "ONLINE",
                "currentValue" to 1.0,
                "unit" to ""
            ),
            mutableMapOf(
                "id" to "act-005",
                "name" to "Heating System",
                "category" to "ACTUATOR",
                "type" to "HEATER",
                "status" to "ONLINE",
                "currentValue" to 0.0,
                "unit" to ""
            ),
            mutableMapOf(
                "id" to "act-006",
                "name" to "Nutrient Dispenser",
                "category" to "ACTUATOR",
                "type" to "DISPENSER",
                "status" to "ONLINE",
                "currentValue" to 1.0,
                "unit" to ""
            ),
            mutableMapOf(
                "id" to "act-007",
                "name" to "Emergency Alarm",
                "category" to "ACTUATOR",
                "type" to "ALARM",
                "status" to "ONLINE",
                "currentValue" to 0.0,
                "unit" to ""
            ),
            mutableMapOf(
                "id" to "sns-010",
                "name" to "CO2 Sensor",
                "category" to "SENSOR",
                "type" to "CO2",
                "status" to "ONLINE",
                "currentValue" to (350.0 + Math.random() * 100.0),
                "unit" to "ppm"
            ),
            mutableMapOf(
                "id" to "sns-011",
                "name" to "Leaf Wetness Sensor",
                "category" to "SENSOR",
                "type" to "WETNESS",
                "status" to "ONLINE",
                "currentValue" to (10.0 + Math.random() * 50.0),
                "unit" to "%"
            )
        )
        simulatedDevices.addAll(devices)
        return ResponseEntity.ok(simulatedDevices)
    }

    @PostMapping("/actuator/{deviceId}/toggle")
    fun toggleActuator(
        @PathVariable deviceId: String,
        @RequestParam isOn: Boolean
    ): ResponseEntity<Any> {

        val device = simulatedDevices.find { it["id"] == deviceId }

        if (device == null) {
            return ResponseEntity.notFound().build()
        }

        device["currentValue"] = if (isOn) 1.0 else 0.0
        device["status"] = "ONLINE"

        return ResponseEntity.ok(
            mapOf(
                "id" to deviceId,
                "isOn" to isOn,
                "currentValue" to device["currentValue"]
            )
        )
    }
}