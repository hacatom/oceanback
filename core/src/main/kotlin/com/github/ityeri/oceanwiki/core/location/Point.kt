package com.github.ityeri.oceanwiki.core.location

import kotlinx.serialization.Serializable

@Serializable
data class Point(val latitude: Double, val longitude: Double)
