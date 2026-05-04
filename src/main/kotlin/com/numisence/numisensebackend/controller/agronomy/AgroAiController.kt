package com.numisence.numisensebackend.controller.agronomy

import com.numisence.numisensebackend.service.agronomy.AgroAiService
import com.numisence.numisensebackend.service.storage.MinioStorageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/ai")
class AgroAiController(
    private val agroAiService: AgroAiService,
    private val minioStorageService: MinioStorageService
) {

    @GetMapping("/diagnostics/upload-url")
    fun getPresignedUploadUrl(
        @RequestParam(defaultValue = "jpg") extension: String
    ): ResponseEntity<Map<String, String>> {
        val urlData = minioStorageService.generatePreSignedUploadUrl(extension)
        return ResponseEntity.ok(urlData)
    }

    @PostMapping("/diagnostics/sync")
    fun syncDiagnosticResult(@RequestBody request: SyncDiagnosticRequest): ResponseEntity<Map<String, String>> {
        val report = agroAiService.syncDiagnosticReport(
            userId = request.userId,
            zoneId = request.zoneId,
            detectedDiseaseName = request.detectedDiseaseName,
            aiConfidence = request.aiConfidence,
            imageUrl = request.imageUrl
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(
            mapOf(
                "status" to "Synced successfully. Analysis alerts dispatched.",
                "reportId" to report.id.toString()
            )
        )
    }

    @GetMapping("/crops/suggest/{zoneId}")
    fun suggestCrops(@PathVariable zoneId: UUID): ResponseEntity<List<Map<String, Any>>> {
        val suggestedCrops = agroAiService.generateCropSuggestions(zoneId)

        // Simplifying the response mapping for the client
        val response = suggestedCrops.map { crop ->
            mapOf(
                "id" to crop.id.toString(),
                "name" to crop.name,
                "waterRequirementMm" to (crop.waterRequirementMmPerSeason ?: 0.0),
                "nitrogenFixing" to crop.nitrogenFixing
            )
        }
        return ResponseEntity.ok(response)
    }
}

// Data Transfer Object
data class SyncDiagnosticRequest(
    val userId: UUID,
    val zoneId: UUID,
    val detectedDiseaseName: String?,
    val aiConfidence: Double,
    val imageUrl: String
)