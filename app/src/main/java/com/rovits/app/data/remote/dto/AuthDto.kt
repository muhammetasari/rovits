package com.rovits.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Tek tip istek modeli: Firebase ID token
// Hem login hem register için kullanılır

data class FirebaseTokenRequest(
    @SerializedName("firebaseToken")
    val firebaseToken: String
)

// Auth Response (backend'e uygun şekilde güncellendi)
data class AuthResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("refreshToken")
    val refreshToken: String?,
    @SerializedName("user")
    val user: UserDto
)

// User DTO (backend'e uygun şekilde güncellendi)
data class UserDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("firebaseUid")
    val firebaseUid: String?
)