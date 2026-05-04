package com.numisence.numisensebackend.controller.identity

import com.numisence.numisensebackend.domain.identity.Farmer
import com.numisence.numisensebackend.repository.identity.FarmerRepository
import com.numisence.numisensebackend.security.JwtService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val farmerRepository: FarmerRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<Any> {
        if (farmerRepository.findByEmail(request.email) != null) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Email already exists"))
        }

        val newFarmer = Farmer(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password)!!,
            role = "FARMER"
        )

        val savedFarmer = farmerRepository.save(newFarmer)
        val token = jwtService.generateToken(savedFarmer.email, savedFarmer.role, savedFarmer.id.toString())

        return ResponseEntity.status(HttpStatus.CREATED).body(
            AuthResponse(token = token, userId = savedFarmer.id.toString())
        )
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        val farmer = farmerRepository.findByEmail(request.email)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "Invalid credentials"))

        if (!passwordEncoder.matches(request.password, farmer.passwordHash)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "Invalid credentials"))
        }

        val token = jwtService.generateToken(farmer.email, farmer.role, farmer.id.toString())

        return ResponseEntity.ok(AuthResponse(token = token, userId = farmer.id.toString()))
    }
}

// DTOs for Authentication
data class RegisterRequest(
    val firstName: String, val lastName: String, val email: String, val password: String
)
data class LoginRequest(val email: String, val password: String)
data class AuthResponse(val token: String, val userId: String)