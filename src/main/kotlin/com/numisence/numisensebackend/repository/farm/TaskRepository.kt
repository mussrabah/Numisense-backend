package com.numisence.numisensebackend.repository.farm

import com.numisence.numisensebackend.domain.farm.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TaskRepository : JpaRepository<Task, UUID> {
    fun findAllByZoneId(zoneId: UUID): List<Task>
    fun countByZoneIdAndStatus(zoneId: UUID, status: String): Int
}