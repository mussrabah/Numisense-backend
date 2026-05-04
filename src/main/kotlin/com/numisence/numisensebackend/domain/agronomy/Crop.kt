package com.numisence.numisensebackend.domain.agronomy

import jakarta.persistence.*
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "crop")
class Crop(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(name = "name", nullable = false, length = 255)
    var name: String = "",

    @Column(name = "scientific_name", length = 255)
    var scientificName: String? = null,

    @Column(name = "variety", length = 255)
    var variety: String? = null,

    @Column(name = "crop_family", length = 100)
    var cropFamily: String? = null,

    @Column(name = "crop_type", length = 100)
    var cropType: String? = null,

    @Column(name = "growth_cycle_days")
    var growthCycleDays: Int? = null,

    @Column(name = "optimal_temperature_min")
    var optimalTemperatureMin: Double? = null,

    @Column(name = "optimal_temperature_max")
    var optimalTemperatureMax: Double? = null,

    @Column(name = "water_requirement_mm_per_season")
    var waterRequirementMmPerSeason: Double? = null,

    @Column(name = "nitrogen_fixing")
    var nitrogenFixing: Boolean = false,

    @Column(name = "created_at", updatable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)