package com.numisence.numisensebackend.domain.farm

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "farm_zone_sustainability_report")
class FarmZoneSustainabilityReport(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_zone_id", nullable = false)
    var farmZone: FarmZone? = null,

    @Column(name = "reporting_period_start", nullable = false)
    var reportingPeriodStart: LocalDate = LocalDate.now(),

    @Column(name = "reporting_period_end", nullable = false)
    var reportingPeriodEnd: LocalDate = LocalDate.now(),

    @Column(name = "carbon_emission_kg")
    var carbonEmissionKg: Double? = null,

    @Column(name = "water_consumption_liters")
    var waterConsumptionLiters: Double? = null,

    @Column(name = "nitrogen_balance")
    var nitrogenBalance: Double? = null,

    @Column(name = "biodiversity_score")
    var biodiversityScore: Double? = null,

    @Column(name = "eu_compliance_score")
    var euComplianceScore: Double? = null,

    @Column(name = "created_at", updatable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)