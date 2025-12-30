package com.github.ityeri.oceanwiki.core.location.impl

import com.github.ityeri.oceanwiki.core.config.AppConfig
import com.github.ityeri.oceanwiki.core.location.Location
import com.github.ityeri.oceanwiki.core.location.LocationResolver
import com.github.ityeri.oceanwiki.core.location.Point
import kotlinx.coroutines.delay

class LocationResolverImpl(private val config: AppConfig) : LocationResolver {

    private data class MockAddress(val fullAddress: String, val province: String, val city: String)

    override suspend fun resolveLocationFromCoords(coords: Point): Location {
        val addressInfo = resolveAddress(coords)

        return Location(
            point = coords,
            fullAddress = addressInfo.fullAddress,
            province = addressInfo.province,
            city = addressInfo.city
        )
    }

    /**
     * Mocks a call to an external Geocoding API.
     */
    private suspend fun resolveAddress(coords: Point): MockAddress {
        delay(100) // Simulate network delay
        return when {
            coords.latitude > 35.1 && coords.longitude > 129.1 -> MockAddress(
                fullAddress = "부산광역시 해운대구 우동 (가상주소)",
                province = "부산광역시",
                city = "해운대구"
            )
            else -> MockAddress(
                fullAddress = "알 수 없는 해안 지역 (가상주소)",
                province = "미지정",
                city = "미지정"
            )
        }
    }
}