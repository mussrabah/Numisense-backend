package com.numisence.numisensebackend.dto.marketplace

import com.numisence.numisensebackend.domain.marketplace.MarketplaceListing
import java.math.BigDecimal
import java.util.UUID

// 1. The Response Object sent to the Mobile Client
data class MarketplaceListingResponse(
    val id: UUID,
    val sellerName: String,
    val title: String,
    val description: String?,
    val price: BigDecimal,
    val longitude: Double,
    val latitude: Double,
    val status: String
)

// 2. Kotlin Extension Function to easily map Entity -> DTO
fun MarketplaceListing.toDto(): MarketplaceListingResponse {
    return MarketplaceListingResponse(
        id = this.id!!,
        // Safely access lazy-loaded seller data
        sellerName = "${this.seller?.firstName} ${this.seller?.lastName}",
        title = this.title,
        description = this.description,
        price = this.price,
        // Extract Coordinates from the PostGIS JTS Point
        longitude = this.location?.x ?: 0.0,
        latitude = this.location?.y ?: 0.0,
        status = this.status
    )
}