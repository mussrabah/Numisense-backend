package com.numisence.numisensebackend.domain.identity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "farmer")
class Farmer(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(name = "first_name", nullable = false, length = 100)
    var firstName: String = "",

    @Column(name = "last_name", nullable = false, length = 100)
    var lastName: String = "",

    @Column(name = "dob")
    var dob: LocalDate? = null,

    @Column(name = "nationality", length = 100)
    var nationality: String? = null,

    @Column(name = "phone_number", unique = true, length = 50)
    var phoneNumber: String? = null,

    @Column(name = "email", unique = true, nullable = false, length = 255)
    var email: String = "",

    @Column(name = "country", length = 100)
    var country: String? = null,

    @Column(name = "city", length = 100)
    var city: String? = null,

    @Column(name = "district", length = 100)
    var district: String? = null,

    @Column(name = "postal_code", length = 20)
    var postalCode: String? = null,

    @Column(name = "national_id", unique = true, length = 100)
    var nationalId: String? = null,

    @Column(name = "specialty", length = 255)
    var specialty: String? = null,

    @Column(name = "password_hash", nullable = false, length = 255)
    var passwordHash: String = "",

    @Column(name = "role", nullable = false, length = 50)
    var role: String = "FARMER",

    @Column(name = "created_at", updatable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)