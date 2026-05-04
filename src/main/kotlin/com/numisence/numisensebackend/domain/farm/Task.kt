package com.numisence.numisensebackend.domain.farm

import com.numisence.numisensebackend.domain.identity.Farmer
import jakarta.persistence.*
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "task")
class Task(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    var zone: FarmZone? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    var assignedTo: Farmer? = null,

    @Column(nullable = false, length = 255)
    var title: String = "",

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(length = 50)
    var status: String = "PENDING",

    @Column(name = "due_date")
    var dueDate: LocalDate? = null,

    @Column(name = "created_at", updatable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)