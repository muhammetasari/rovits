# GitHub Copilot Instructions - Rovits Projesi

Bu belge, Rovits Android uygulaması için GitHub Copilot'ın kod önerilerinde kullanacağı kuralları ve standartları tanımlar.

## 🎯 Proje Genel Bakış

Rovits, Jetpack Compose ve Material Design 3 kullanan modern bir Android uygulamasıdır. Firebase Authentication entegrasyonu ve çoklu dil desteği içerir.

## 📋 Genel Kodlama Kuralları

### Kotlin Standartları
- **Kotlin Coding Conventions**'a tam uyum sağla
- Değişken isimleri `camelCase` formatında olmalı
- Sınıf isimleri `PascalCase` formatında olmalı
- Sabitler `UPPER_SNAKE_CASE` formatında olmalı
- Paket isimleri tamamen küçük harf olmalı

### Null Safety
- Mümkün olduğunca nullable olmayan tipler (`String`, `Int`) kullan
- Null kontrollerinde elvis operator (`? :`) tercih et
- Safe call operator (`?.`) kullan
- `!!` operatörünü yalnızca kesinlikle gerekli durumlarda kullan

```kotlin
// İyi ✅
val name = user?.fullName ?: "Misafir"

// Kötü ❌
val name = user!! .fullName
```

## 🏗️ Mimari ve Yapı

### Katmanlı Mimari
Proje aşağıdaki katmanları takip eder:
- **UI Layer:** `ui/screens`, `ui/components`, `ui/theme`, `ui/common`
- **ViewModel Layer:** `ui/viewmodel`
- **Data Layer:** `data/model`, `data/repository`
- **Navigation:** `navigation`
- **Utils:** `util`, `utils`

### Paket Yapısı
```
com.rovits.app/
├── data/
│   ├── model/          # Veri modelleri
│   └── repository/     # Repository sınıfları
├── navigation/         # Navigation Graph'lar
├── ui/
│   ├── common/         # Standart layout bileşenleri
│   ├── components/     # Yeniden kullanılabilir bileşenler
│   ├── screens/        # Ekran Composable'ları
│   ├── theme/          # Tema ve stil tanımları
│   └── viewmodel/      # ViewModel sınıfları
├── util/              # Yardımcı sınıflar
└── utils/             # Utility fonksiyonlar
```

## 🎨 Jetpack Compose Kuralları

### Composable Fonksiyonlar
- Composable fonksiyon isimleri PascalCase olmalı
- Her Composable için `@Preview` annotasyonu ekle
- Modifier parametresini her zaman ilk parametre olarak al
- State hoisting prensibini uygula

```kotlin
// İyi ✅
@Composable
fun UserProfile(
    modifier: Modifier = Modifier,
    user: User,
    onLogout: () -> Unit
) {
    // İçerik
}

@Preview(showBackground = true)
@Composable
fun UserProfilePreview() {
    RovitsAppTheme {
        UserProfile(
            user = User("1", "Test", "test@example.com", null),
            onLogout = {}
        )
    }
}
```

### State Management
- `remember` ile lokal state yönet
- `rememberSaveable` ile configuration değişikliklerinde state'i koru
- ViewModel'den gelen state'leri `collectAsState()` ile topla
- Yan etkiler için `LaunchedEffect`, `DisposableEffect` kullan

```kotlin
// İyi ✅
val authState by viewModel.authState.collectAsState()
var showDialog by remember { mutableStateOf(false) }
```

### Material Design 3
- Tüm renkler için `MaterialTheme. colorScheme` kullan
- Typography için `MaterialTheme.typography` kullan
- Spacing için `MaterialTheme.shapes` kullan
- Özel renkler yerine tema renklerini tercih et

```kotlin
// İyi ✅
Text(
    text = "Başlık",
    style = MaterialTheme.typography.headlineLarge,
    color = MaterialTheme. colorScheme.primary
)

// Kötü ❌
Text(
    text = "Başlık",
    fontSize = 32.sp,
    color = Color(0xFF6200EE)
)
```

## 📐 Standart Layout Bileşenleri

### YENİ EKRAN OLUŞTURURKEN ZORUNLU KURALLAR

**Her yeni ekran için `StandardLayout`, `StandartTopAppBar` ve `StandardBottomBar` bileşenlerini kullan.**

### StandardLayout Kullanımı
Yeni bir ekran oluştururken **mutlaka** `StandardLayout` bileşenini kullan.  Bu bileşen otomatik olarak TopBar ve BottomBar'ı yönetir.

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YeniEkran(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    StandardLayout(
        navController = navController,
        title = "Ekran Başlığı",
        showTopBar = true,              // TopBar gösterilsin mi? 
        showBackButton = true,           // Geri butonu gösterilsin mi? 
        showBottomBar = true,            // BottomBar gösterilsin mi?
        onNavigateBack = { navController.popBackStack() },
        topAppBarActions = {             // Sağ üst köşe butonları (opsiyonel)
            IconButton(onClick = { /* Aksiyon */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Ayarlar")
            }
        },
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    ) { paddingValues ->
        // Ekran içeriği buraya gelir
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // İçerik
        }
    }
}
```

### StandartTopAppBar Kullanımı
Eğer özel bir TopBar tasarımı gerekiyorsa, `StandartTopAppBar` kullan:

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OzelTopBarluEkran(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            StandartTopAppBar(
                title = "Özel Başlık",
                showBackButton = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { /* Paylaş */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Paylaş")
                    }
                    IconButton(onClick = { /* Favorile */ }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorile")
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    ) { paddingValues ->
        // İçerik
    }
}
```

### StandardBottomBar
BottomBar otomatik olarak `StandardLayout` içinde yönetilir.  Manuel kullanım gerekmez, ancak gerekirse:

```kotlin
Scaffold(
    bottomBar = {
        StandardBottomBar(navController = navController)
    }
) { paddingValues ->
    // İçerik
}
```

### Ekran Tipleri ve Layout Konfigürasyonları

#### 1. Ana Sayfa (Home Screen)
```kotlin
StandardLayout(
    onNavigateBack = { /* Ana sayfa, geri gitmez */ },
    topAppBarTitle = "Rovits",
    showTopBar = false,        // Ana sayfada genelde TopBar gösterilmez
    showBackButton = false,
    showBottomBar = true,      // BottomBar gösterilir
    navController = navController
) { paddingValues ->
    // Ana sayfa içeriği
}
```

#### 2. Detay Sayfaları
```kotlin
StandardLayout(
    onNavigateBack = { navController.popBackStack() },
    topAppBarTitle = "Detay Başlığı",
    showTopBar = true,
    showBackButton = true,     // Geri butonu zorunlu
    showBottomBar = false,     // Detay sayfalarında genelde BottomBar gizlenir
    navController = navController,
    topAppBarActions = {
        IconButton(onClick = { /* Paylaş */ }) {
            Icon(Icons.Default.Share, contentDescription = "Paylaş")
        }
    }
) { paddingValues ->
    // Detay sayfası içeriği
}
```

#### 3. Ayarlar Sayfası
```kotlin
StandardLayout(
    onNavigateBack = { navController.popBackStack() },
    topAppBarTitle = stringResource(R.string.settings_title),
    showTopBar = true,
    showBackButton = true,
    showBottomBar = true,      // Ayarlar sayfasında BottomBar gösterilebilir
    navController = navController
) { paddingValues ->
    // Ayarlar içeriği
}
```

#### 4. Profil Sayfası
```kotlin
StandardLayout(
    navController = navController,
    title = stringResource(R.string.profile_title),
    showTopBar = true,
    showBackButton = false,    // Profil ana navigasyon öğesi ise geri butonu olmaz
    showBottomBar = true,
    onNavigateBack = { navController.popBackStack() },
    topAppBarActions = {
        IconButton(onClick = { /* Düzenle */ }) {
            Icon(Icons.Default.Edit, contentDescription = "Düzenle")
        }
    }
) { paddingValues ->
    // Profil içeriği
}
```

### Scroll Behavior
TopBar'ın kaydırma davranışını özelleştir:

```kotlin
// Scroll sırasında TopBar kaybolur
scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

// Scroll sırasında TopBar sabit kalır
scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

// TopBar scroll ile birlikte hareket eder
scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
```

Scroll behavior kullanırken içeriği `nestedScroll` modifier ile bağla:

```kotlin
val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

StandardLayout(
    // ... diğer parametreler
    scrollBehavior = scrollBehavior
) { paddingValues ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
    ) {
        // Scroll edilebilir içerik
    }
}
```

## 🔄 Asenkron İşlemler

### Coroutines
- Asenkron işlemler için Kotlin Coroutines kullan
- ViewModel içinde `viewModelScope` kullan
- Composable içinde `rememberCoroutineScope()` kullan
- Flow'lar için `collectAsState()` kullan

```kotlin
// ViewModel içinde
fun loadData() {
    viewModelScope. launch {
        try {
            val result = repository.fetchData()
            _uiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _uiState. value = UiState.Error(e. message)
        }
    }
}
```

## 🗺️ Navigation

### Navigation Compose
- Her ekran için `Screen` sealed class'ında route tanımla
- Navigation graph'ları modüler olarak ayır (auth, home, profile)
- Deep link desteği için route'lara parametre ekle
- Back stack yönetimi için `popUpTo` kullan

```kotlin
// İyi ✅
navController.navigate(Screen.Profile.route) {
    popUpTo(Screen. Home.route) { inclusive = false }
    launchSingleTop = true
}
```

## 🔥 Firebase Entegrasyonu

### Authentication
- Firebase Authentication için `AuthRepository` kullan
- Google Sign-In için Google Identity Services (GIS) kullan
- Tüm auth işlemlerini Flow olarak döndür
- Hata yönetimi için `try-catch` blokları kullan

```kotlin
// Repository pattern
suspend fun signInWithEmail(email: String, password: String): Flow<AuthResult> = flow {
    try {
        val result = auth.signInWithEmailAndPassword(email, password). await()
        emit(AuthResult.Success(result. user?. toUser()))
    } catch (e: FirebaseAuthException) {
        emit(AuthResult.Error(e. toAppException()))
    }
}
```

## 📱 UI Bileşenleri

### Özel Bileşenler
- Yeniden kullanılabilir bileşenler `ui/components` klasöründe olmalı
- Standart layout bileşenleri `ui/common` klasöründe olmalı
- Her bileşen için varsayılan parametreler tanımla
- Bileşenler tek sorumluluk prensibi ile tasarlanmalı
- Özel bileşen isimleri projeyi yansıtmalı (örn: `RovitsLogo`)

### Standart Bileşenler
Projede kullanılması gereken standart bileşenler:
- **StandardLayout** - Tüm ekranlar için temel layout
- **StandartTopAppBar** - Üst navigasyon çubuğu
- **StandardBottomBar** - Alt navigasyon çubuğu
- **ListMenuItem** - Liste öğeleri için
- **RovitsLogo** - Uygulama logosu

### Liste Öğeleri
- Liste öğeleri için `ListMenuItem` bileşenini kullan
- Tutarlılık için `ListMenuItemStyle` enum'ını kullan
- Leading/trailing content için lambda parametreleri sağla

## 🌍 Çoklu Dil Desteği

### Lokalizasyon
- Tüm metinler `strings.xml` dosyasında tanımlanmalı
- Composable içinde `stringResource()` kullan
- `LocaleHelper` utility'si ile dil değişimlerini yönet
- Hard-coded string kullanma

```kotlin
// İyi ✅
Text(text = stringResource(id = R.string.welcome_message))

// Kötü ❌
Text(text = "Hoş geldiniz")
```

## 🎨 Tema Yönetimi

### Theme Configuration
- `ThemeViewModel` ile tema durumunu yönet
- Tema tercihleri DataStore ile saklanmalı
- Dark/Light mode desteği sağla
- Dinamik renk desteği ekle (Material You)

```kotlin
RovitsAppTheme(themeConfig = themeConfig) {
    // Uygulama içeriği
}
```
## 📐 Kod Düzeni ve Format

### Dosya Yapısı
1. Package declaration
2. Import statements (alfabetik sıralı)
3. Class/Object declaration
4. Companion object (varsa)
5. Properties
6. Init block (varsa)
7. Functions

### Fonksiyon Sıralaması
1. Public functions
2. Private functions
3.  Composable functions (alfabetik)
4. Preview functions

### Yorum ve Dokümantasyon
- Public API'ler için KDoc açıklamaları yaz
- Karmaşık iş mantığı için açıklayıcı yorumlar ekle
- TODO yorumları için task referansı ekle
- Türkçe veya İngilizce tutarlı kullan

```kotlin
/**
 * Kullanıcı kimlik doğrulama işlemlerini yöneten ViewModel. 
 *
 * @property authRepository Kimlik doğrulama repository'si
 */
class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    // İçerik
}
```

## 🔒 Güvenlik

### Hassas Bilgiler
- API key'leri `local.properties` veya `BuildConfig` ile yönet
- Hassas bilgileri loglama
- ProGuard/R8 kurallarını tanımla
- Firebase Security Rules'ı uygula

## ⚡ Performans

### Optimization
- LazyColumn/LazyRow kullanırken `key` parametresi belirle
- Gereksiz recomposition'ları önle
- `derivedStateOf` ile hesaplanan state'leri optimize et
- Büyük listeler için pagination uygula

```kotlin
// İyi ✅
LazyColumn {
    items(
        items = userList,
        key = { user -> user.uid }
    ) { user ->
        UserItem(user = user)
    }
}
```

## 📦 Dependency Injection

### Manuel DI
- Constructor injection tercih et
- Repository instance'larını tekrar kullan
- ViewModel factory pattern'i kullan
- Gelecekte Hilt/Koin geçişine hazır kod yaz

## 🚫 Kaçınılması Gerekenler

❌ Hard-coded string'ler  
❌ Magic number'lar  
❌ God class'lar (çok fazla sorumluluk)  
❌ Tight coupling  
❌ Null pointer risk'i (`!! ` operatörü)  
❌ Memory leak'ler (lifecycle'ı dikkate alma)  
❌ Main thread'de ağır işlemler  
❌ Gereksiz nested Composable'lar  
❌ StandardLayout yerine manuel Scaffold kullanımı  
❌ Özel TopBar/BottomBar yerine standart bileşenleri kullanmamak

## ✅ Best Practices

✅ Single Responsibility Principle  
✅ DRY (Don't Repeat Yourself)  
✅ KISS (Keep It Simple, Stupid)  
✅ Clean Code prensipleri  
✅ Meaningful naming  
✅ Consistent code style  
✅ Error handling  
✅ Null safety  
✅ StandardLayout kullanımı  
✅ Standart bileşenleri tercih etme

## 🔧 Proje Özellikleri

### Minimum Gereksinimler
- **minSdk:** 26 (Android 8.0)
- **targetSdk:** 35 (Android 15)
- **compileSdk:** 35
- **JVM Target:** 17
- **Kotlin Version:** Latest stable

### Kullanılan Kütüphaneler
- Jetpack Compose
- Material Design 3
- Firebase (Auth, Analytics, Crashlytics, Performance)
- Navigation Compose
- Kotlin Coroutines
- DataStore Preferences
- Google Play Services Auth

## 🎯 Kod Örnekleri

### ViewModel Pattern
```kotlin
class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    
    private val _authState = MutableStateFlow<AuthState>(AuthState. Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signInWithEmail(email, password)
                .collect { result ->
                    _authState.value = when (result) {
                        is AuthResult.Success -> AuthState. Authenticated(result.user)
                        is AuthResult.Error -> AuthState. Error(result.exception. message)
                    }
                }
        }
    }
}
```

### Screen Composable Pattern (StandardLayout ile)
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    user: User?,
    onLogout: () -> Unit
) {
    StandardLayout(
        navController = navController,
        title = stringResource(R.string.profile_title),
        showTopBar = true,
        showBackButton = true,
        showBottomBar = true,
        onNavigateBack = { navController.popBackStack() },
        topAppBarActions = {
            IconButton(onClick = { /* Ayarlar */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Ayarlar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Profil içeriği
            user?.let {
                Text(
                    text = it.fullName,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = it.email,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = onLogout,
                modifier = Modifier. fillMaxWidth()
            ) {
                Text(stringResource(R.string.logout))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    RovitsAppTheme {
        ProfileScreen(
            navController = rememberNavController(),
            user = User("1", "Test User", "test@example.com", null),
            onLogout = {}
        )
    }
}
```

---

**Not:** Bu kurallar projenin mevcut kod yapısına dayanarak oluşturulmuştur.  Copilot bu kuralları otomatik olarak tüm kod önerilerinde kullanacaktır.  **Özellikle yeni ekran oluştururken StandardLayout, StandartTopAppBar ve StandardBottomBar bileşenlerinin kullanılması zorunludur.**