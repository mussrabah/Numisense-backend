package com.numisence.numisensebackend.controller.marketplace

import com.numisence.numisensebackend.dto.marketplace.MarketplaceListingResponse
import com.numisence.numisensebackend.dto.marketplace.toDto
import com.numisence.numisensebackend.service.marketplace.SpatialMarketplaceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/marketplace")
class SpatialMarketplaceController(
    private val spatialMarketplaceService: SpatialMarketplaceService
) {

    /**
     * GET /api/v1/marketplace/nearby?longitude=19.45&latitude=51.75&radiusKm=25.0
     * Triggered by the Mobile App's map view to find local peers.
     */
    @GetMapping("/nearby")
    fun getNearbyListings(
        @RequestParam longitude: Double,
        @RequestParam latitude: Double,
        @RequestParam(defaultValue = "25.0") radiusKm: Double
    ): ResponseEntity<List<MarketplaceListingResponse>> {

        // 1. Fetch Entities via PostGIS Service
        val listings = spatialMarketplaceService.getLocalListings(longitude, latitude, radiusKm)

        // 2. Map Entities to DTOs to hide DB structure from the client
        val response = listings.map { it.toDto() }

        // 3. Return 200 OK
        return ResponseEntity.ok(response)
    }
}