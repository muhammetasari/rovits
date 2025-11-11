package com.rovits.app.data.remote.api

import com.rovits.app.data.remote.ApiConstants
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Header

interface LocationSyncApiService {

    @POST("api/sync/locations")
    suspend fun syncLocations(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("radius") radius: Int = 5000,
        @Query("type") type: String = "restaurant",
        @Header(ApiConstants.HEADER_ACCEPT_LANGUAGE) language: String = "tr-TR"
    ): Response<String>
}

