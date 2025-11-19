package com.rovits.app.data.remote

object ApiConstants {
    const val BASE_URL = "https://poi-sync-service.onrender.com/"

    // Headers
    const val HEADER_API_KEY = "X-API-Key"
    const val HEADER_AUTHORIZATION = "Authorization"
    const val HEADER_CONTENT_TYPE = "Content-Type"

    // Endpoints - "/api" prefix'i eklendi
    object Auth {
        const val LOGIN = "api/auth/login"
        const val REGISTER = "api/auth/register"
        const val SOCIAL_LOGIN = "api/auth/social-login"
        const val REFRESH = "api/auth/refresh"
    }

    object Places {
        const val NEARBY = "api/places/nearby"
        const val TEXT_SEARCH = "api/places/text-search"
        const val DETAILS = "api/places/details/{placeId}"
    }
}