package com.numisence.numisensebackend.domain.agronomy

import jakarta.persistence.*
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "crop_rotation_rule")
class CropRotationRule(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_crop_id")
    var previousCrop: Crop? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_crop_id")
    var nextCrop: Crop? = null,

    @Column(name = "suitability_score")
    var suitabilityScore: Double? = null,

    @Column(name = "recommended_wait_years")
    var recommendedWaitYears: Int? = null,

    @Column(name = "agronomic_reason", columnDefinition = "TEXT")
    var agronomicReason: String? = null,

    @Column(name = "created_at", updatable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)