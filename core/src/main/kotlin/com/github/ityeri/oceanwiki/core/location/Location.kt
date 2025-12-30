package com.github.ityeri.oceanwiki.core.location

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val point: Point,
    val fullAddress: String,
    val province: String, // Maps to address_depth1 in DB
    val city: String      // Maps to address_depth2 in DB
)