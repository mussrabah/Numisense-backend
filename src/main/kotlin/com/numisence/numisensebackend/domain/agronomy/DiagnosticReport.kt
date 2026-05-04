package com.numisence.numisensebackend.domain.agronomy

import com.numisence.numisensebackend.domain.farm.FarmZone
import com.numisence.numisensebackend.domain.identity.Farmer
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
import java.util.UUID

@Entity
@Table(name = "diagnostic_report")
class DiagnosticReport(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    var zone: FarmZone? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: Farmer? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detected_disease_id")
    var detectedDisease: Disease? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suggested_crop_id")
    var suggestedCrop: Crop? = null,

    @Column(name = "image_url", length = 1024)
    var imageUrl: String? = null,

    @Column(name = "ai_confidence")
    var aiConfidence: Double? = null,

    @Column(name = "created_at", updatable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)