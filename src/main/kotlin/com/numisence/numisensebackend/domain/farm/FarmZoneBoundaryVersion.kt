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
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "farm_zone_boundary_version")
class FarmZoneBoundaryVersion(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_zone_id", nullable = false)
    var farmZone: FarmZone? = null,

    @Column(name = "boundary", nullable = false, columnDefinition = "geometry(MultiPolygon,4326)")
    var boundary: MultiPolygon? = null,

    @Column(name = "valid_from", nullable = false)
    var validFrom: ZonedDateTime = ZonedDateTime.now(),

    @Column(name = "valid_to")
    var validTo: ZonedDateTime? = null
)