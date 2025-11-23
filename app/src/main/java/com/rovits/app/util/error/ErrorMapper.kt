package com.rovits.app.util.error

import android.content.Context
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.rovits.app.R
import java.io.IOException

object ErrorMapper {

    fun mapToMessage(context: Context, exception: Exception): String {
        Firebase.crashlytics.recordException(exception)
        return when (exception) {
            is AppException.AuthError -> mapAuthError(context, exception.code)
            is AppException.NetworkError -> context.getString(R.string.error_network)
            is AppException.ValidationError -> mapValidationError(context, exception)
            is FirebaseAuthException -> mapFirebaseAuthError(context, exception.errorCode)
            is IOException -> context.getString(R.string.error_network)
            else -> exception.message ?: context.getString(R.string.error_unknown)
        }
    }

    private fun mapAuthError(context: Context, code: String): String {
        return when (code) {
            "ERROR_INVALID_EMAIL" -> context.getString(R.string.error_invalid_email)
            "ERROR_WRONG_PASSWORD" -> context.getString(R.string.error_wrong_password)
            "ERROR_USER_NOT_FOUND" -> context.getString(R.string.error_user_not_found)
            "ERROR_USER_DISABLED" -> context.getString(R.string.error_user_disabled)
            "ERROR_TOO_MANY_REQUESTS" -> context.getString(R.string.error_too_many_requests)
            "ERROR_EMAIL_ALREADY_IN_USE" -> context.getString(R.string.error_email_already_in_use)
            "ERROR_WEAK_PASSWORD" -> context.getString(R.string.error_weak_password)
            "ERROR_OPERATION_NOT_ALLOWED" -> context.getString(R.string.error_operation_not_allowed)
            else -> context.getString(R.string.error_auth_failed)
        }
    }

    private fun mapFirebaseAuthError(context: Context, errorCode: String): String {
        return when (errorCode) {
            "ERROR_INVALID_EMAIL" -> context.getString(R.string.error_invalid_email)
            "ERROR_WRONG_PASSWORD" -> context.getString(R.string.error_wrong_password)
            "ERROR_USER_NOT_FOUND" -> context.getString(R.string.error_user_not_found)
            "ERROR_USER_DISABLED" -> context.getString(R.string.error_user_disabled)
            "ERROR_TOO_MANY_REQUESTS" -> context.getString(R.string.error_too_many_requests)
            "ERROR_EMAIL_ALREADY_IN_USE" -> context.getString(R.string.error_email_already_in_use)
            "ERROR_WEAK_PASSWORD" -> context.getString(R.string.error_weak_password)
            "ERROR_OPERATION_NOT_ALLOWED" -> context.getString(R.string.error_operation_not_allowed)
            else -> context.getString(R.string.error_auth_failed)
        }
    }

    private fun mapValidationError(context: Context, exception: AppException.ValidationError): String {
        return when (exception.field) {
            "email" -> context.getString(R.string.error_invalid_email_format)
            "password" -> context.getString(R.string.error_password_too_short)
            "fullName" -> context.getString(R.string.error_full_name_required)
            else -> exception.message ?: context.getString(R.string.error_validation)
        }
    }
}
