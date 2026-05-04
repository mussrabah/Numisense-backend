package com.numisence.numisensebackend.repository.identity

import com.numisence.numisensebackend.domain.identity.Farmer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FarmerRepository : JpaRepository<Farmer, UUID> {
    fun findByEmail(email: String): Farmer?
}