package com.rovits.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rovits.app.data.repository.AuthRepository
import com.rovits.app.data.repository.PlacesRepository
import com.rovits.app.data.repository.LocationSyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EndpointTestResult {
    object Idle : EndpointTestResult()
    object Loading : EndpointTestResult()
    data class Success(val data: String) : EndpointTestResult()
    data class Error(val message: String) : EndpointTestResult()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val placesRepository: PlacesRepository,
    private val locationSyncRepository: LocationSyncRepository
) : ViewModel() {

    private val _logoutCompleted = MutableStateFlow(false)
    val logoutCompleted: StateFlow<Boolean> = _logoutCompleted.asStateFlow()

    private val _testResult = MutableStateFlow<EndpointTestResult>(EndpointTestResult.Idle)
    val testResult: StateFlow<EndpointTestResult> = _testResult.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutCompleted.value = true
        }
    }

    fun onLogoutCompleted() {
        _logoutCompleted.value = false
    }

    fun clearTestResult() {
        _testResult.value = EndpointTestResult.Idle
    }

    // Places API Tests
    fun testNearbyPlaces(lat: Double = 41.0082, lng: Double = 28.9784) {
        viewModelScope.launch {
            _testResult.value = EndpointTestResult.Loading
            try {
                val response = placesRepository.getNearbyPlaces(lat, lng)
                if (response.isSuccessful) {
                    _testResult.value = EndpointTestResult.Success(
                        "✅ Nearby Places: ${response.code()}\n${response.body()}"
                    )
                } else {
                    _testResult.value = EndpointTestResult.Error(
                        "❌ Error ${response.code()}: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                _testResult.value = EndpointTestResult.Error("❌ Exception: ${e.message}")
            }
        }
    }

    fun testTextSearch(query: String = "restaurant") {
        viewModelScope.launch {
            _testResult.value = EndpointTestResult.Loading
            try {
                val response = placesRepository.textSearch(query)
                if (response.isSuccessful) {
                    _testResult.value = EndpointTestResult.Success(
                        "✅ Text Search: ${response.code()}\n${response.body()}"
                    )
                } else {
                    _testResult.value = EndpointTestResult.Error(
                        "❌ Error ${response.code()}: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                _testResult.value = EndpointTestResult.Error("❌ Exception: ${e.message}")
            }
        }
    }

    fun testPlaceDetails(placeId: String = "ChIJnePCdZ3ZuxQRqB86s6BsfrA") {
        viewModelScope.launch {
            _testResult.value = EndpointTestResult.Loading
            try {
                val response = placesRepository.getPlaceDetails(placeId)
                if (response.isSuccessful) {
                    _testResult.value = EndpointTestResult.Success(
                        "✅ Place Details: ${response.code()}\n${response.body()}"
                    )
                } else {
                    _testResult.value = EndpointTestResult.Error(
                        "❌ Error ${response.code()}: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                _testResult.value = EndpointTestResult.Error("❌ Exception: ${e.message}")
            }
        }
    }

    // Location Sync API Test
    fun testLocationSync(lat: Double = 41.0082, lng: Double = 28.9784) {
        viewModelScope.launch {
            _testResult.value = EndpointTestResult.Loading
            try {
                val response = locationSyncRepository.syncLocations(lat, lng)
                if (response.isSuccessful) {
                    _testResult.value = EndpointTestResult.Success(
                        "✅ Location Sync: ${response.code()}\n${response.body()}"
                    )
                } else {
                    _testResult.value = EndpointTestResult.Error(
                        "❌ Error ${response.code()}: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                _testResult.value = EndpointTestResult.Error("❌ Exception: ${e.message}")
            }
        }
    }
}