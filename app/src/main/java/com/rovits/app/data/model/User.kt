package com.rovits.app.data.model

data class User(
    val uid: String,
    val fullName: String,
    val email: String,
    val photoUrl: String? = null,
    val isPasswordProvider: Boolean = false,
    val isAnonymous: Boolean = false
)

