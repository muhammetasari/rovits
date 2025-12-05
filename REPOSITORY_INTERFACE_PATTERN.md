# Repository Interface Pattern Implementation

Bu belge, Rovits projesinde uygulanan Repository Interface Pattern'ini açıklar.

## 📋 Yapılan Değişiklikler

### 1. Interface Tanımlamaları
Tüm Repository'ler için Interface tanımlandı:
- ✅ `IAuthRepository` - Authentication işlemleri için
- ✅ `IUserPreferencesRepository` - User preferences işlemleri için

### 2. Repository Implementasyonları
Mevcut Repository'ler Interface'leri implement edecek şekilde güncellendi:
- ✅ `AuthRepository implements IAuthRepository`
- ✅ `UserPreferencesRepository implements IUserPreferencesRepository`

### 3. Fake Repository'ler
Preview ve test için mock implementasyonlar oluşturuldu:
- ✅ `FakeAuthRepository` - Mock authentication
- ✅ `FakeUserPreferencesRepository` - Mock preferences

### 4. ViewModel Güncellemeleri
ViewModel'ler Interface tipini kabul edecek şekilde güncellendi:
- ✅ `AuthViewModel(repository: IAuthRepository)`
- ✅ `ThemeViewModel(repository: IUserPreferencesRepository)`

### 5. Preview Güncellemeleri
Tüm Preview fonksiyonları Fake Repository kullanacak şekilde güncellendi:
- ✅ `LoginScreenPreview` - FakeAuthRepository kullanıyor
- ✅ `RegisterScreenPreview` - FakeAuthRepository kullanıyor
- ✅ `ForgotPasswordScreenPreview` - FakeAuthRepository kullanıyor

### 6. Dokümantasyon
Copilot instructions dosyası yeni pattern ile güncellendi:
- ✅ Repository Interface Pattern bölümü eklendi
- ✅ Fake Repository Pattern anlatımı eklendi
- ✅ Preview kullanım örnekleri eklendi
- ✅ Best practices ve kaçınılacak durumlar güncellendi

## 🎯 Faydaları

### 1. Test Kolaylığı
```kotlin
// Test için mock repository enjekte edilebilir
val testViewModel = AuthViewModel(
    repository = FakeAuthRepository()
)
```

### 2. Preview Desteği
```kotlin
// Preview'lar Firebase bağımlılığı olmadan çalışır
@Preview
@Composable
fun MyScreenPreview() {
    MyScreen(
        viewModel = AuthViewModel(
            repository = FakeAuthRepository()
        )
    )
}
```

### 3. Gevşek Bağlantı (Loose Coupling)
- ViewModel'ler concrete implementasyona bağımlı değil
- Repository değiştirmek kolay (örn: Firebase → Supabase)
- Dependency Injection için hazır yapı

### 4. SOLID Prensipleri
- **Dependency Inversion:** ViewModel üst seviye modül, Repository alt seviye
- **Open/Closed:** Yeni repository implementasyonu eklemek kolay
- **Interface Segregation:** Her repository kendi interface'ine sahip

## 📁 Dosya Yapısı

```
com.rovits.app/
├── data/
│   ├── repository/
│   │   ├── IAuthRepository.kt              ✅ YENİ
│   │   ├── AuthRepository.kt               ✅ GÜNCELLENDİ
│   │   ├── IUserPreferencesRepository.kt   ✅ YENİ
│   │   ├── UserPreferencesRepository.kt    ✅ GÜNCELLENDİ
│   │   └── fake/                           ✅ YENİ KLASÖR
│   │       ├── FakeAuthRepository.kt       ✅ YENİ
│   │       └── FakeUserPreferencesRepository.kt ✅ YENİ
├── ui/
│   ├── viewmodel/
│   │   ├── AuthViewModel.kt                ✅ GÜNCELLENDİ
│   │   └── ThemeViewModel.kt               ✅ GÜNCELLENDİ
│   └── screens/
│       └── authscreen/
│           ├── LoginScreen.kt              ✅ GÜNCELLENDİ
│           ├── RegisterScreen.kt           ✅ GÜNCELLENDİ
│           └── ForgotPasswordScreen.kt     ✅ GÜNCELLENDİ
└── .github/
    └── copilot-instructions.md             ✅ GÜNCELLENDİ
```

## 🔨 Kullanım Örnekleri

### Yeni Repository Oluşturma

```kotlin
// 1. Interface tanımla
interface IPostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun createPost(post: Post): Result<Post>
}

// 2. Concrete implementasyon
class FirebasePostRepository : IPostRepository {
    override suspend fun getPosts(): List<Post> {
        // Firebase implementasyonu
    }
    
    override suspend fun createPost(post: Post): Result<Post> {
        // Firebase implementasyonu
    }
}

// 3. Fake implementasyon (Preview/Test için)
class FakePostRepository : IPostRepository {
    private val mockPosts = listOf(
        Post("1", "Test Post", "Content"),
        Post("2", "Another Post", "More content")
    )
    
    override suspend fun getPosts(): List<Post> = mockPosts
    
    override suspend fun createPost(post: Post): Result<Post> {
        return Result.success(post.copy(id = "new_id"))
    }
}

// 4. ViewModel'de kullan
class PostViewModel(
    private val repository: IPostRepository = FirebasePostRepository()
) : ViewModel() {
    // ViewModel logic
}

// 5. Preview'da kullan
@Preview
@Composable
fun PostScreenPreview() {
    PostScreen(
        viewModel = PostViewModel(
            repository = FakePostRepository()
        )
    )
}
```

## ⚠️ Önemli Notlar

1. **Her yeni Repository için Interface oluştur**
2. **Preview'larda MUTLAKA Fake Repository kullan**
3. **ViewModel constructor'da Interface tipi kullan**
4. **Fake Repository'leri `data/repository/fake/` klasöründe tut**
5. **Mock data için anlamlı değerler kullan**

## 🚀 Gelecek İyileştirmeler

- [ ] Unit test'ler için Fake Repository kullanımı
- [ ] Hilt/Koin entegrasyonu için hazırlık
- [ ] Repository Factory pattern implementasyonu
- [ ] Daha fazla Repository için Interface/Fake implementasyonları

## ✅ Sonuç

Repository Interface Pattern implementasyonu başarıyla tamamlandı. Artık:
- ✅ Preview'lar Firebase olmadan çalışıyor
- ✅ Test yazmak daha kolay
- ✅ Kod daha modüler ve bakımı kolay
- ✅ SOLID prensiplerine uygun yapı
- ✅ Gelecek değişikliklere hazır mimari

---

**Son Güncelleme:** 2025-12-05  
**Pattern:** Repository Interface + Fake Implementation  
**Etkilenen Dosyalar:** 13 dosya güncellendi, 4 yeni dosya eklendi

