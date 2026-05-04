package com.numisence.numisensebackend.repository.agronomy

import com.numisence.numisensebackend.domain.agronomy.Crop
import com.numisence.numisensebackend.domain.agronomy.DiagnosticReport
import com.numisence.numisensebackend.domain.agronomy.Disease
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DiseaseRepository : JpaRepository<Disease, UUID> {
    fun findByNameIgnoreCase(name: String): Disease?
}