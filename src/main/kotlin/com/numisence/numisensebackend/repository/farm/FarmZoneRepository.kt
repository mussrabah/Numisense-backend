package com.numisence.numisensebackend.repository.farm

import com.numisence.numisensebackend.domain.farm.FarmZone
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FarmZoneRepository : JpaRepository<FarmZone, UUID> {
    fun findAllByFarmerId(farmerId: UUID): List<FarmZone>
}