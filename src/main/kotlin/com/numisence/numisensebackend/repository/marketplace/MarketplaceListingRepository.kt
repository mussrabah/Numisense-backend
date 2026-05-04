package com.numisence.numisensebackend.repository.marketplace

import com.numisence.numisensebackend.domain.marketplace.MarketplaceListing
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MarketplaceListingRepository : JpaRepository<MarketplaceListing, UUID> {

    /**
     * Executes a PostGIS spatial query to find all active listings within a specific radius.
     * We cast the geometry to 'geography' to calculate the distance accurately in meters.
     */
    @Query(
        value = """
            SELECT * FROM marketplace_listing m 
            WHERE m.status = 'ACTIVE' 
            AND ST_DWithin(
                m.location::geography, 
                ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography, 
                :radiusMeters
            )
        """,
        nativeQuery = true
    )
    fun findActiveListingsWithinRadius(
        @Param("lon") longitude: Double,
        @Param("lat") latitude: Double,
        @Param("radiusMeters") radiusMeters: Double
    ): List<MarketplaceListing>
}