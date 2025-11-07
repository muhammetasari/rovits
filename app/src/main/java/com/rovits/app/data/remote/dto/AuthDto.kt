package com.rovits.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Login Request
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

// Social Login Request (Firebase token)
data class SocialLoginRequest(
    @SerializedName("firebaseToken")
    val firebaseToken: String,
    @SerializedName("provider")
    val provider: String // "google" veya "apple"
)

// Register Request
data class RegisterRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

// Auth Response
data class AuthResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("refreshToken")
    val refreshToken: String?,
    @SerializedName("user")
    val user: UserDto
)

// User DTO
data class UserDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String?
)