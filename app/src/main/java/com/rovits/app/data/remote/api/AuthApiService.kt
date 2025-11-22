package com.rovits.app.data.remote.api

import com.rovits.app.data.remote.ApiConstants
import com.rovits.app.data.remote.dto.ApiResponse
import com.rovits.app.data.remote.dto.AuthResponse
import com.rovits.app.data.remote.dto.FirebaseTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    /**
     * Login with Firebase ID token
     *
     * Request: POST /api/auth/login
     * Body: { "firebaseToken": "firebase-id-token-here..." }
     *
     * Response: ApiResponse<AuthResponse>
     */
    @POST(ApiConstants.Auth.LOGIN)
    suspend fun login(
        @Body request: FirebaseTokenRequest
    ): Response<ApiResponse<AuthResponse>>

    /**
     * Register new user with Firebase ID token
     *
     * Request: POST /api/auth/register
     * Body: { "firebaseToken": "firebase-id-token-here..." }
     *
     * Response: ApiResponse<AuthResponse>
     */
    @POST(ApiConstants.Auth.REGISTER)
    suspend fun register(
        @Body request: FirebaseTokenRequest
    ): Response<ApiResponse<AuthResponse>>

    /**
     * Logout user
     *
     * Request: POST /api/auth/logout
     * Headers: Authorization: Bearer <token>
     *
     * Response: ApiResponse<Unit>
     *
     * Note: Token automatically added by AuthInterceptor
     */
    @POST(ApiConstants.Auth.LOGOUT)
    suspend fun logout(): Response<ApiResponse<Unit>>

    /**
     * Send email verification
     *
     * Request: POST /api/auth/send-email-verification
     * Body: { "firebaseToken": "firebase-id-token-here..." }
     *
     * Response: ApiResponse<Unit>
     */
    @POST(ApiConstants.Auth.SEND_EMAIL_VERIFICATION)
    suspend fun sendEmailVerification(
        @Body request: FirebaseTokenRequest
    ): Response<ApiResponse<Unit>>
}