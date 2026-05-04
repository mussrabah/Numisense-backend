package com.numisence.numisensebackend.domain.iot

import com.numisence.numisensebackend.domain.farm.FarmZone
import jakarta.persistence.*
import org.locationtech.jts.geom.Point
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "iot_device")
class IotDevice(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_zone_id", nullable = false)
    var farmZone: FarmZone? = null,

    @Column(name = "device_category", nullable = false, length = 50)
    var deviceCategory: String = "",

    @Column(name = "device_type", nullable = false, length = 50)
    var deviceType: String = "",

    @Column(name = "mac_address", unique = true, nullable = false, length = 17)
    var macAddress: String = "",

    @Column(name = "installation_date")
    var installationDate: LocalDate? = null,

    @Column(length = 50)
    var status: String = "ACTIVE",

    @Column(columnDefinition = "geometry(Point,4326)")
    var location: Point? = null
)



