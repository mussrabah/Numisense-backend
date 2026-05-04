package com.numisence.numisensebackend.domain.iot

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Entity
@Table(name = "telemetry")
class Telemetry(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // Matches BIGSERIAL in Postgres
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    var device: IotDevice? = null,

    @Column(name = "reading_value", nullable = false)
    var readingValue: Double = 0.0,

    @Column(name = "recorded_at", nullable = false)
    var recordedAt: ZonedDateTime = ZonedDateTime.now()
)