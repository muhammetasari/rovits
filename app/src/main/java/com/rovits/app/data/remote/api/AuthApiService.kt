package com.rovits.app.data.remote.api

import com.rovits.app.data.remote.ApiConstants
import com.rovits.app.data.remote.dto.AuthResponse
import com.rovits.app.data.remote.dto.LoginRequest
import com.rovits.app.data.remote.dto.SocialLoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST(ApiConstants.Auth.LOGIN)
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST(ApiConstants.Auth.SOCIAL_LOGIN)
    suspend fun socialLogin(@Body request: SocialLoginRequest): Response<AuthResponse>
}