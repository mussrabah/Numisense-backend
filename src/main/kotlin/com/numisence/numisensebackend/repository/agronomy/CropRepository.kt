package com.numisence.numisensebackend.repository.agronomy

import com.numisence.numisensebackend.domain.agronomy.Crop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CropRepository : JpaRepository<Crop, UUID> {
    fun findByNameIgnoreCase(name: String): Crop?
}