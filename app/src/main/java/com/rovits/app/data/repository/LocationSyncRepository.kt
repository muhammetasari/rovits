package com.rovits.app.data.repository

import com.rovits.app.data.remote.api.LocationSyncApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationSyncRepository @Inject constructor(
    private val locationSyncApiService: LocationSyncApiService
) {
    suspend fun syncLocations(
        lat: Double,
        lng: Double,
        radius: Int = 5000,
        type: String = "restaurant"
    ) = locationSyncApiService.syncLocations(lat, lng, radius, type)
}

