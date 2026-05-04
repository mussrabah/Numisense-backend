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
@Table(name = "device_action_log")
class DeviceActionLog(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    var device: IotDevice? = null,

    @Column(name = "action_type", nullable = false, length = 50)
    var actionType: String = "",

    @Column(name = "action_value")
    var actionValue: Double? = null,

    @Column(name = "triggered_by", nullable = false, length = 50)
    var triggeredBy: String = "",

    @Column(name = "executed_at", updatable = false)
    var executedAt: ZonedDateTime = ZonedDateTime.now()
)