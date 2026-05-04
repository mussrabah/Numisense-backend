package com.numisence.numisensebackend.domain.agronomy

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "soil_profile")
class SoilProfile(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(name = "soil_type_name", nullable = false, length = 100)
    var soilTypeName: String = "",

    @Column(name = "ph_range", length = 50)
    var phRange: String? = null,

    @Column(name = "organic_matter_percentage")
    var organicMatterPercentage: Double? = null,

    @Column(name = "texture_classification", length = 100)
    var textureClassification: String? = null
)