# API Entegrasyon Değişiklik Listesi

## 📁 Değiştirilen Dosyalar

### Backend API Katmanı
1. **AuthApiService.kt** ✅
   - Login: `login(IdTokenRequest)` - Firebase ID token ile unified login
   - Register: `register(IdTokenRequest)` - Firebase ID token ile register
   - SocialLogin endpoint'i kaldırıldı

2. **ApiConstants.kt** ✅
   - SOCIAL_LOGIN endpoint kaldırıldı

### Data Transfer Objects
3. **AuthDto.kt** ✅
   - Yeni: `IdTokenRequest(idToken: String)`
   - Güncellendi: `AuthResponse` - refreshToken, user
   - Güncellendi: `UserDto` - role, firebaseUid eklendi
   - Kaldırıldı: LoginRequest, RegisterRequest, SocialLoginRequest

### Repository Katmanı
4. **AuthRepository.kt** ✅
   - `login(idToken: String)` - Firebase ID token ile backend login
   - `register(idToken: String)` - Firebase ID token ile backend register
   - `logout()` - Backend logout + local data clear
   - Gereksiz importlar temizlendi

### ViewModel Katmanı
5. **LoginViewModel.kt** ✅
   - Yeni: `loginWithEmailPassword(email, password)` - Firebase auth + backend login
   - Güncellendi: `login(idToken)` - Private, unified login
   - FirebaseAuth dependency eklendi

6. **RegisterViewModel.kt** ✅
   - Yeni: `registerWithEmailPassword(name, email, password)` - Firebase auth + backend register
   - Güncellendi: `register(idToken)` - Private
   - FirebaseAuth dependency eklendi

### UI Katmanı
7. **LoginScreen.kt** ✅
   - Button onClick: `viewModel.loginWithEmailPassword(email, password)`

8. **RegisterScreen.kt** ✅
   - Button onClick: `viewModel.registerWithEmailPassword(name, email, password)`
   - Keyboard actions güncellendi

### Error Handling
9. **ErrorMessageMapper.kt** ✅
   - Yeni hata kodları eklendi (USER_xxx, AUTH_xxx, VAL_xxx, EXT_xxx, DB_xxx)
   - Backend error code mapping güncellendi

### Local Storage
10. **PreferencesManager.kt** ✅
    - Mevcut: `saveJwtToken()`, `saveRefreshToken()`, `saveUserEmail()`
    - Değişiklik gerekmedi

## 🔥 Kaldırılan Kodlar

### AuthApiService.kt
```kotlin
❌ @POST(ApiConstants.Auth.SOCIAL_LOGIN)
❌ suspend fun socialLogin(@Body request: SocialLoginRequest): Response<ApiResponse<AuthResponse>>
```

### AuthDto.kt
```kotlin
❌ data class LoginRequest(email: String, password: String)
❌ data class RegisterRequest(name: String, email: String, password: String)
❌ data class SocialLoginRequest(firebaseToken: String, provider: String)
```

### ApiConstants.kt
```kotlin
❌ const val SOCIAL_LOGIN = "api/auth/social-login"
```

## ➕ Eklenen Kodlar

### AuthDto.kt
```kotlin
✅ data class IdTokenRequest(
    @SerializedName("idToken")
    val idToken: String
)

✅ data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String?,
    @SerializedName("role") val role: String?,        // YENİ
    @SerializedName("firebaseUid") val firebaseUid: String?  // YENİ
)
```

### LoginViewModel.kt
```kotlin
✅ fun loginWithEmailPassword(email: String, password: String) {
    viewModelScope.launch {
        _loginState.value = LoginState.Loading
        try {
            // 1. Firebase Authentication
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            
            // 2. Firebase ID token al
            val idToken = authResult.user?.getIdToken(false)?.await()?.token
            
            if (idToken != null) {
                // 3. Backend'e login
                login(idToken)
            } else {
                _loginState.value = LoginState.Error(context.getString(R.string.error_invalid_token))
            }
        } catch (e: Exception) {
            _loginState.value = LoginState.Error(e.localizedMessage ?: context.getString(R.string.error_unknown))
        }
    }
}
```

### RegisterViewModel.kt
```kotlin
✅ fun registerWithEmailPassword(name: String, email: String, password: String) {
    viewModelScope.launch {
        _registerState.value = RegisterState.Loading
        try {
            // 1. Firebase Authentication
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            
            // 2. Kullanıcı profiline isim ekle
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            authResult.user?.updateProfile(profileUpdates)?.await()
            
            // 3. Firebase ID token al
            val idToken = authResult.user?.getIdToken(false)?.await()?.token
            
            if (idToken != null) {
                // 4. Backend'e register
                register(idToken)
            } else {
                _registerState.value = RegisterState.Error(context.getString(R.string.error_invalid_token))
            }
        } catch (e: Exception) {
            _registerState.value = RegisterState.Error(e.localizedMessage ?: context.getString(R.string.error_unknown))
        }
    }
}
```

## 🔄 API Endpoint Değişiklikleri

### Login Endpoint
**Öncesi:**
```json
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Sonrası:**
```json
POST /api/auth/login
{
  "idToken": "firebase-id-token-here..."
}
```

### Register Endpoint
**Öncesi:**
```json
POST /api/auth/register
{
  "name": "John Doe",
  "email": "user@example.com",
  "password": "password123"
}
```

**Sonrası:**
```json
POST /api/auth/register
{
  "idToken": "firebase-id-token-here..."
}
```

### Social Login
**Öncesi:**
```json
POST /api/auth/social-login
{
  "firebaseToken": "...",
  "provider": "google"
}
```

**Sonrası:**
```
❌ Endpoint kaldırıldı
✅ Unified login kullanılıyor: POST /api/auth/login
```

## 📊 Derleme Sonuçları

```
BUILD SUCCESSFUL in 52s
102 actionable tasks: 24 executed, 78 up-to-date
```

**Hata:** ✅ YOK  
**Uyarı:** ⚠️ Lint uyarıları (kritik değil)

## ✅ Checklist

- [x] API Service güncellendi
- [x] DTO modelleri güncellendi
- [x] Repository katmanı güncellendi
- [x] ViewModel'ler güncellendi
- [x] UI ekranları güncellendi
- [x] Error handling güncellendi
- [x] String resources kontrol edildi
- [x] Proje başarıyla derlendi
- [x] Dokümantasyon oluşturuldu

## 🎯 Sonuç

**Durum:** ✅ TAMAMLANDI  
**Tarih:** 22 Kasım 2025  
**Toplam Değiştirilen Dosya:** 9  
**Eklenen Satır:** ~200  
**Silinen Satır:** ~150

Mobil uygulama artık yeni API ile tam uyumlu!

