package com.numisence.numisensebackend.domain.farm

import com.numisence.numisensebackend.domain.agronomy.Crop
import com.numisence.numisensebackend.domain.identity.Farmer
import jakarta.persistence.*
import org.locationtech.jts.geom.MultiPolygon
import org.locationtech.jts.geom.Point
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "farm_zone")
class FarmZone(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    var farmer: Farmer? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_crop_id")
    var currentCrop: Crop? = null,

    @Column(name = "name", nullable = false, length = 255)
    var name: String = "",

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String? = null,

    // PostGIS Spatial Mappings via JTS
    @Column(name = "boundary", columnDefinition = "geometry(MultiPolygon,4326)")
    var boundary: MultiPolygon? = null,

    @Column(name = "centroid", columnDefinition = "geometry(Point,4326)")
    var centroid: Point? = null,

    @Column(name = "area_hectares")
    var areaHectares: Double? = null,

    @Column(name = "elevation_avg")
    var elevationAvg: Double? = null,

    @Column(name = "status", nullable = false, length = 50)
    var status: String = "ACTIVE",

    @Column(name = "planting_date")
    var plantingDate: LocalDate? = null,

    @Column(name = "expected_harvest_date")
    var expectedHarvestDate: LocalDate? = null,

    @Column(name = "marketplace_visibility_radius_km")
    var marketplaceVisibilityRadiusKm: Double = 25.0,

    @Column(name = "allow_external_offers")
    var allowExternalOffers: Boolean = true,

    @Column(name = "created_at", updatable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: ZonedDateTime = ZonedDateTime.now()
)