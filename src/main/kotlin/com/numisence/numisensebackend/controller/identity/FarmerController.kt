package com.numisence.numisensebackend.controller.identity

import com.numisence.numisensebackend.repository.identity.FarmerRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/farmers")
class FarmerController(
    private val farmerRepository: FarmerRepository
) {

    @GetMapping("/me")
    fun getCurrentUser(authentication: Authentication?): ResponseEntity<Map<String, Any?>> {
        // Because /api/v1/farmers/** is NOT whitelisted, the JwtFilter will process the token.
        if (authentication == null || !authentication.isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        val farmer = farmerRepository.findByEmail(authentication.name)
            ?: throw RuntimeException("User not found")

        return ResponseEntity.ok(
            mapOf(
                "id" to farmer.id,
                "firstName" to farmer.firstName,
                "lastName" to farmer.lastName,
                "email" to farmer.email,
                "role" to farmer.role,
                "farmName" to "Numisense HQ",       // ADDED THIS
                "location" to "Poznań, Poland"
            )
        )
    }
}