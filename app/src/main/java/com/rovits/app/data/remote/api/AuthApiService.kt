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

    /**
     * Login with email and password
     *
     * Request: POST /api/auth/login
     * Body: { "email": "user@example.com", "password": "password123" }
     *
     * Response: ApiResponse<AuthResponse>
     */
    @POST(ApiConstants.Auth.LOGIN)
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<AuthResponse>>

    /**
     * Register new user
     *
     * Request: POST /api/auth/register
     * Body: { "name": "John Doe", "email": "user@example.com", "password": "password123" }
     *
     * Response: ApiResponse<AuthResponse>
     */
    @POST(ApiConstants.Auth.REGISTER)
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<AuthResponse>>

    /**
     * Social login with Firebase token
     *
     * Request: POST /api/auth/social-login
     * Body: { "firebaseToken": "...", "provider": "google" }
     *
     * Response: ApiResponse<AuthResponse>
     */
    @POST(ApiConstants.Auth.SOCIAL_LOGIN)
    suspend fun socialLogin(
        @Body request: SocialLoginRequest
    ): Response<ApiResponse<AuthResponse>>

    /**
     * Logout user
     *
     * Request: POST /api/auth/logout
     * Headers: Authorization: Bearer <token>
     * Body (optional): { "refreshToken": "..." }
     *
     * Response: ApiResponse<Unit>
     *
     * Note: Token automatically added by AuthInterceptor
     */
    @POST(ApiConstants.Auth.LOGOUT)
    suspend fun logout(): Response<ApiResponse<Unit>>
}