package com.numisence.numisensebackend.repository.farm

import com.numisence.numisensebackend.domain.farm.BidMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BidMessageRepository : JpaRepository<BidMessage, UUID>