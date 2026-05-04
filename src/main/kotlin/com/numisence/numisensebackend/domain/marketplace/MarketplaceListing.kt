package com.numisence.numisensebackend.domain.marketplace

import com.numisence.numisensebackend.domain.identity.Farmer
import jakarta.persistence.*
import org.locationtech.jts.geom.Point
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "marketplace_listing")
class MarketplaceListing(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    var seller: Farmer? = null,

    @Column(name = "title", nullable = false, length = 255)
    var title: String = "",

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String? = null,

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    var price: BigDecimal = BigDecimal.ZERO,

    // The PostGIS point used for radius searches
    @Column(name = "location", nullable = false, columnDefinition = "geometry(Point,4326)")
    var location: Point? = null,

    @Column(name = "status", length = 50)
    var status: String = "ACTIVE",

    @Column(name = "created_at", updatable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)