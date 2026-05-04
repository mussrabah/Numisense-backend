package com.numisence.numisensebackend.domain.farm

import com.numisence.numisensebackend.domain.agronomy.Crop
import jakarta.persistence.*
import org.locationtech.jts.geom.MultiPolygon
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "farm_zone_crop_history")
class FarmZoneCropHistory(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_zone_id", nullable = false)
    var farmZone: FarmZone? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id", nullable = false)
    var crop: Crop? = null,

    @Column(name = "season_year", nullable = false)
    var seasonYear: Int = 0,

    @Column(name = "season_type", length = 100)
    var seasonType: String? = null,

    @Column(name = "planting_date")
    var plantingDate: LocalDate? = null,

    @Column(name = "harvest_date")
    var harvestDate: LocalDate? = null,

    @Column(name = "yield_tons")
    var yieldTons: Double? = null,

    @Column(name = "fertilizer_used_kg")
    var fertilizerUsedKg: Double? = null,

    @Column(name = "irrigation_water_liters")
    var irrigationWaterLiters: Double? = null,

    @Column(name = "disease_incidents_count")
    var diseaseIncidentsCount: Int = 0,

    @Column(name = "major_disease_detected", length = 255)
    var majorDiseaseDetected: String? = null,

    @Column(name = "farmer_notes", columnDefinition = "TEXT")
    var farmerNotes: String? = null,

    @Column(name = "created_at", updatable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)