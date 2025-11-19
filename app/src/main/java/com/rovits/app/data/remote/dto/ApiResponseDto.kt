package com.rovits.app.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Wrapper for all API responses
 * Matches backend ApiResponse<T> structure
 */
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: T? = null,

    @SerializedName("error")
    val error: ErrorDetail? = null,

    @SerializedName("timestamp")
    val timestamp: String
)

/**
 * Error detail structure
 * Matches backend ErrorDetail
 */
data class ErrorDetail(
    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("field")
    val field: String? = null,

    @SerializedName("details")
    val details: Map<String, Any>? = null
)

/**
 * Validation error response
 * Matches backend ValidationErrorResponse
 */
data class ValidationErrorResponse(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("errors")
    val errors: List<FieldError>,

    @SerializedName("timestamp")
    val timestamp: String
)

/**
 * Field-specific validation error
 */
data class FieldError(
    @SerializedName("field")
    val field: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("rejectedValue")
    val rejectedValue: Any? = null
)