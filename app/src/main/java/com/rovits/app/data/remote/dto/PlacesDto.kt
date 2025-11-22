package com.rovits.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// ===== SEARCH NEARBY =====
data class SearchNearbyResponse(
    @SerializedName("places")
    val places: List<NearbyPlace>?
)

data class NearbyPlace(
    @SerializedName("id")
    val id: String,

    @SerializedName("displayName")
    val displayName: DisplayName?
)

// ===== TEXT SEARCH =====
data class SearchTextResponse(
    @SerializedName("places")
    val places: List<TextSearchPlace>?
)

data class TextSearchPlace(
    @SerializedName("id")
    val id: String,

    @SerializedName("displayName")
    val displayName: DisplayName?,

    @SerializedName("formattedAddress")
    val formattedAddress: String?
)

// ===== PLACE DETAILS =====
data class PlaceDetails(
    @SerializedName("id")
    val id: String,

    @SerializedName("displayName")
    val displayName: DisplayName?,

    @SerializedName("formattedAddress")
    val formattedAddress: String?,

    @SerializedName("regularOpeningHours")
    val openingHours: OpeningHours?
)

// ===== SHARED MODELS =====
data class DisplayName(
    @SerializedName("text")
    val text: String?,

    @SerializedName("languageCode")
    val languageCode: String?
)

data class OpeningHours(
    @SerializedName("openNow")
    val openNow: Boolean?,

    @SerializedName("weekdayDescriptions")
    val weekdayDescriptions: List<String>?
)