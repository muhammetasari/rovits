package com.rovits.app.data.remote.api

import com.rovits.app.data.remote.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlacesApiService {

    @GET(ApiConstants.Places.NEARBY)
    suspend fun getNearbyPlaces(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("radius") radius: Int = 5000,
        @Query("type") type: String = "restaurant"
    ): Response<Any>

    @GET(ApiConstants.Places.TEXT_SEARCH)
    suspend fun textSearch(
        @Query("query") query: String,
        @Query("languageCode") languageCode: String = "tr",
        @Query("maxResults") maxResults: Int = 20,
        @Query("lat") lat: Double? = null,
        @Query("lng") lng: Double? = null,
        @Query("radius") radius: Int? = null
    ): Response<Any>

    @GET(ApiConstants.Places.DETAILS)
    suspend fun getPlaceDetails(
        @Path("placeId") placeId: String
    ): Response<Any>
}

