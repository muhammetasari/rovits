package com.rovits.app.data.remote

object ApiConstants {
    const val BASE_URL = "https://poi-sync-service.onrender.com/"

    // Headers
    const val HEADER_API_KEY = "X-API-Key"
    const val HEADER_AUTHORIZATION = "Authorization"
    const val HEADER_CONTENT_TYPE = "Content-Type"
    const val HEADER_ACCEPT_LANGUAGE = "Accept-Language"

    // Endpoints
    object Auth {
        const val LOGIN = "auth/login"
        const val REGISTER = "auth/register"
        const val SOCIAL_LOGIN = "auth/social-login"
        const val REFRESH = "auth/refresh"
    }

    object Places {
        const val NEARBY = "api/places/nearby"
        const val TEXT_SEARCH = "api/places/text-search"
        const val DETAILS = "api/places/details/{placeId}"
    }

    object LocationSync {
        const val SYNC_LOCATIONS = "api/sync/locations"
    }
}