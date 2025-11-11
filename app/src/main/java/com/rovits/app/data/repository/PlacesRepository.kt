package com.rovits.app.data.repository

import com.rovits.app.data.remote.api.PlacesApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepository @Inject constructor(
    private val placesApiService: PlacesApiService
) {
    suspend fun getNearbyPlaces(
        lat: Double,
        lng: Double,
        radius: Int = 5000,
        type: String = "restaurant"
    ) = placesApiService.getNearbyPlaces(lat, lng, radius, type)

    suspend fun textSearch(
        query: String,
        languageCode: String = "tr",
        maxResults: Int = 20,
        lat: Double? = null,
        lng: Double? = null,
        radius: Int? = null
    ) = placesApiService.textSearch(query, languageCode, maxResults, lat, lng, radius)

    suspend fun getPlaceDetails(placeId: String) = placesApiService.getPlaceDetails(placeId)
}

