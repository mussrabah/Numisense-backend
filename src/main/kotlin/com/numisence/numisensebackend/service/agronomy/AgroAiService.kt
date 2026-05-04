package com.numisence.numisensebackend.service.agronomy

import com.numisence.numisensebackend.domain.agronomy.Crop
import com.numisence.numisensebackend.domain.agronomy.DiagnosticReport
import com.numisence.numisensebackend.domain.agronomy.Disease
import com.numisence.numisensebackend.repository.agronomy.CropRepository
import com.numisence.numisensebackend.repository.agronomy.CropRotationRuleRepository
import com.numisence.numisensebackend.repository.agronomy.DiagnosticReportRepository
import com.numisence.numisensebackend.repository.agronomy.DiseaseRepository
import com.numisence.numisensebackend.repository.farm.FarmZoneCropHistoryRepository
import com.numisence.numisensebackend.repository.farm.FarmZoneRepository
import com.numisence.numisensebackend.repository.identity.FarmerRepository
import com.numisence.numisensebackend.service.ai.GeminiLlmService
import com.numisence.numisensebackend.service.notification.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class AgroAiService(
    private val diseaseRepository: DiseaseRepository,
    private val diagnosticReportRepository: DiagnosticReportRepository,
    private val farmZoneRepository: FarmZoneRepository,
    private val farmerRepository: FarmerRepository,
    private val geminiLlmService: GeminiLlmService,
    private val notificationService: NotificationService,
    private val farmZoneCropHistoryRepository: FarmZoneCropHistoryRepository,
    private val cropRotationRuleRepository: CropRotationRuleRepository,
    private val cropRepository: CropRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun syncDiagnosticReport(userId: UUID, zoneId: UUID, detectedDiseaseName: String?, aiConfidence: Double, imageUrl: String): DiagnosticReport {
        log.info("Syncing Edge AI Diagnostic for user {} on zone {}", userId, zoneId)

        val user = farmerRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
        val zone = farmZoneRepository.findById(zoneId).orElseThrow { IllegalArgumentException("Zone not found") }

        var resolvedDisease: Disease? = null

        // If the Edge AI detected a disease, verify it in our DB
        if (!detectedDiseaseName.isNullOrBlank()) {
            resolvedDisease = diseaseRepository.findByNameIgnoreCase(detectedDiseaseName)

            // LLM FALLBACK TRIGGER: Disease not in PostGIS database!
            if (resolvedDisease == null) {
                log.warn("Disease '{}' not found in local DB. Triggering LLM fallback.", detectedDiseaseName)

                val llmData = geminiLlmService.getDiseaseFallbackData(detectedDiseaseName)

                // Create and save the new AI-generated disease entry
                val newDisease = Disease(
                    name = detectedDiseaseName,
                    scientificName = llmData.scientificName,
                    symptomsDescription = llmData.symptoms,
                    treatmentRecommendation = llmData.treatment,
                    severityLevel = llmData.severityLevel,
                    aiGeneratedFlag = true // Flags this for Cooperative Admin review later
                )
                resolvedDisease = diseaseRepository.save(newDisease)
            }
        }

        // Save the final Diagnostic Report linking the User, Zone, and Disease
        val report = DiagnosticReport(
            zone = zone,
            user = user,
            detectedDisease = resolvedDisease,
            imageUrl = imageUrl,
            aiConfidence = aiConfidence
        )
        val savedReport = diagnosticReportRepository.save(report)

        // Trigger Real-Time Notification to the User's Dashboard
        notificationService.sendDiagnosticCompletionAlert(
            userId = user.id!!,
            zoneName = zone.name,
            diseaseDetected = resolvedDisease?.name
        )

        return savedReport
    }

    /**
     * Implements UC1.3: Generate Crop Suggestions based on historical planting data.
     */
    @Transactional(readOnly = true)
    fun generateCropSuggestions(zoneId: UUID): List<Crop> {
        log.info("Generating crop suggestions for zone {}", zoneId)
        val lastHistory = farmZoneCropHistoryRepository.findTopByFarmZoneIdOrderBySeasonYearDesc(zoneId)

        // Fallback: If no history exists, return general default crops
        if (lastHistory == null || lastHistory.crop == null) {
            return cropRepository.findAll().take(5)
        }

        // Logic: Fetch the rotation rules for the previously planted crop, ordered by suitability score
        val rules = cropRotationRuleRepository.findByPreviousCropIdOrderBySuitabilityScoreDesc(lastHistory.crop!!.id!!)
        return rules.mapNotNull { it.nextCrop }
    }
}