package com.numisence.numisensebackend.repository.agronomy

import com.numisence.numisensebackend.domain.agronomy.DiagnosticReport
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DiagnosticReportRepository : JpaRepository<DiagnosticReport, UUID>