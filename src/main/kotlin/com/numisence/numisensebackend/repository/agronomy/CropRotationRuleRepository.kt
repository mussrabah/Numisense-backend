package com.numisence.numisensebackend.repository.agronomy

import com.numisence.numisensebackend.domain.agronomy.CropRotationRule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CropRotationRuleRepository : JpaRepository<CropRotationRule, UUID> {
    // Used by UC1.3 to fetch the highest-scoring successor crops
    fun findByPreviousCropIdOrderBySuitabilityScoreDesc(previousCropId: UUID): List<CropRotationRule>
}