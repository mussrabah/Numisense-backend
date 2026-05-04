package com.numisence.numisensebackend.service.marketplace

import com.numisence.numisensebackend.domain.marketplace.MarketplaceListing
import com.numisence.numisensebackend.repository.marketplace.MarketplaceListingRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SpatialMarketplaceService(
    private val marketplaceListingRepository: MarketplaceListingRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Fetches local peer-to-peer marketplace items for the farmer's feed.
     * Radius is expected in Kilometers from the mobile client and converted to Meters for PostGIS.
     */
    @Transactional(readOnly = true)
    fun getLocalListings(longitude: Double, latitude: Double, radiusKm: Double): List<MarketplaceListing> {
        log.info("Fetching marketplace listings within {}km of lon: {}, lat: {}", radiusKm, longitude, latitude)

        val radiusMeters = radiusKm * 1000.0

        return marketplaceListingRepository.findActiveListingsWithinRadius(
            longitude = longitude,
            latitude = latitude,
            radiusMeters = radiusMeters
        )
    }
}