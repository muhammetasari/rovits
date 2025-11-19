package com.rovits.app.data.remote.api

import com.rovits.app.data.remote.ApiConstants
import com.rovits.app.data.remote.dto.ApiResponse
import com.rovits.app.data.remote.dto.AuthResponse
import com.rovits.app.data.remote.dto.LoginRequest
import com.rovits.app.data.remote.dto.SocialLoginRequest
import com.rovits.app.data.remote.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST(ApiConstants.Auth.LOGIN)
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>

    @POST(ApiConstants.Auth.SOCIAL_LOGIN)
    suspend fun socialLogin(@Body request: SocialLoginRequest): Response<ApiResponse<AuthResponse>>

    @POST(ApiConstants.Auth.REGISTER)
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponse>>
}