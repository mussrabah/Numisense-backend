package com.numisence.numisensebackend.domain.agronomy

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "disease")
class Disease(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false, length = 255)
    var name: String = "",

    @Column(name = "scientific_name", length = 255)
    var scientificName: String? = null,

    @Column(name = "symptoms_description", columnDefinition = "TEXT")
    var symptomsDescription: String? = null,

    @Column(name = "treatment_recommendation", columnDefinition = "TEXT")
    var treatmentRecommendation: String? = null,

    @Column(name = "severity_level", length = 50)
    var severityLevel: String? = null,

    @Column(name = "ai_generated_flag")
    var aiGeneratedFlag: Boolean = false,

    @Column(name = "created_at", updatable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: ZonedDateTime = ZonedDateTime.now()
)