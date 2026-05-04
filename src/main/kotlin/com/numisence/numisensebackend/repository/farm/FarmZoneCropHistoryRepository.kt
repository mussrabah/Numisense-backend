package com.numisence.numisensebackend.repository.farm

import com.numisence.numisensebackend.domain.farm.BidMessage
import com.numisence.numisensebackend.domain.farm.FarmZoneCropHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FarmZoneCropHistoryRepository : JpaRepository<FarmZoneCropHistory, UUID> {
    // Crucial for the AI to know what was planted last season to recommend the next crop
    fun findTopByFarmZoneIdOrderBySeasonYearDesc(farmZoneId: UUID): FarmZoneCropHistory?
}