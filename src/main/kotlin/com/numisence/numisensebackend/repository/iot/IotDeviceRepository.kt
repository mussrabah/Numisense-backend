package com.numisence.numisensebackend.repository.iot

import com.numisence.numisensebackend.domain.iot.DeviceActionLog
import com.numisence.numisensebackend.domain.iot.IotDevice
import com.numisence.numisensebackend.domain.iot.Telemetry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface IotDeviceRepository : JpaRepository<IotDevice, UUID> {
    fun findByMacAddress(macAddress: String): IotDevice?
}

@Repository
interface TelemetryRepository : JpaRepository<Telemetry, Long> {
    // Highly optimized query to get the last reading for an IoT device
    fun findTopByDeviceIdOrderByRecordedAtDesc(deviceId: UUID): Telemetry?
}

@Repository
interface DeviceActionLogRepository : JpaRepository<DeviceActionLog, Long>