# Rovits App - Tema Yapısı Dokümantasyonu

## 📋 Genel Bakış

Rovits uygulaması, Material Design 3 standartlarına uygun, modern ve esnek bir tema sistemi kullanmaktadır. Tema sistemi üç ana dosyadan oluşmaktadır:

1. **Color.kt** - Renk tanımları
2. **Type.kt** - Tipografi sistemi
3. **Theme.kt** - Ana tema yapılandırması

## 🎨 Color.kt - Renk Paleti

### Marka Renkleri
- `RovitsOrange` (#FF83331D) - Ana marka rengi
- `RovitsOrangeDark` (#FFE56B47) - Koyu ton marka rengi
- `RovitsOrangeLight` (#FFFF9A7F) - Açık ton marka rengi

### Semantik Renkler
- `SuccessGreen` (#FF388E3C) - Başarı durumları için
- `WarningOrange` (#FFF57C00) - Uyarı durumları için
- `ErrorRed` (#FFD32F2F) - Hata durumları için

### Nötr Renkler
- `Gray50` (#FFF5F5F5) - En açık gri
- `Gray200` (#FFE0E0E0) - Açık gri
- `Gray400` (#FFB3B3B3) - Orta gri
- `Gray600` (#FF666666) - Koyu gri
- `Gray900` (#FF1A1A1A) - En koyu gri

## 📝 Type.kt - Tipografi Sistemi

Material Design 3 tipografi hiyerarşisi tam olarak uygulanmıştır:

### Display Styles (Büyük Başlıklar)
- **displayLarge**: 57sp, Bold - Giriş ekranları, splash
- **displayMedium**: 45sp, Bold - Ana sayfa başlıkları
- **displaySmall**: 36sp, Bold - Özel vurgular

### Headline Styles (Bölüm Başlıkları)
- **headlineLarge**: 32sp, Bold - Sayfa başlıkları
- **headlineMedium**: 28sp, SemiBold - Bölüm başlıkları
- **headlineSmall**: 24sp, SemiBold - Alt bölüm başlıkları

### Title Styles (Kart/Liste Başlıkları)
- **titleLarge**: 22sp, SemiBold - Kart başlıkları
- **titleMedium**: 16sp, SemiBold - Liste öğesi başlıkları
- **titleSmall**: 14sp, Medium - Küçük başlıklar

### Body Styles (İçerik Metinleri)
- **bodyLarge**: 16sp, Normal - Ana içerik metinleri
- **bodyMedium**: 14sp, Normal - Standart metin
- **bodySmall**: 12sp, Normal - Yardımcı metin

### Label Styles (UI Öğeleri)
- **labelLarge**: 14sp, Medium - Butonlar, sekmeler
- **labelMedium**: 12sp, Medium - Etiketler
- **labelSmall**: 11sp, Medium - Küçük etiketler

## 🌗 Theme.kt - Tema Yapılandırması

### Karanlık Tema (Dark Theme)
```kotlin
primary = RovitsOrange
secondary = RovitsOrangeLight
tertiary = WarningOrange
background = #121212
surface = #1E1E1E
error = #CF6679
```

### Aydınlık Tema (Light Theme)
```kotlin
primary = RovitsOrange
secondary = RovitsOrangeDark
tertiary = SuccessGreen
background = White
surface = White
error = ErrorRed
```

### Özellikler
- ✅ Material Design 3 uyumlu
- ✅ Dynamic Color desteği (Android 12+)
- ✅ Karanlık/Aydınlık mod tam desteği
- ✅ Sistem tema otomatik algılama
- ✅ Tutarlı renk paleti

## 🚀 Kullanım

### Tema Uygulama
```kotlin
@Composable
fun MyApp() {
    RovitsAppTheme {
        // Uygulamanız buraya
    }
}
```

### Renk Kullanımı
```kotlin
Text(
    text = "Merhaba",
    color = MaterialTheme.colorScheme.primary
)
```

### Tipografi Kullanımı
```kotlin
Text(
    text = "Başlık",
    style = MaterialTheme.typography.headlineMedium
)
```

### Özel Ayarlar
```kotlin
RovitsAppTheme(
    darkTheme = true,           // Karanlık temayı zorla
    dynamicColor = false        // Dynamic color'ı kapat
) {
    // İçerik
}
```

## 📊 Tema Yapısı Şeması

```
ui/theme/
├── Color.kt          → Renk tanımları
├── Type.kt           → Tipografi sistemi
└── Theme.kt          → Ana tema yapılandırması
```

## 🎯 Best Practices

1. **Renk Kullanımı**
   - Doğrudan renk değerleri yerine `MaterialTheme.colorScheme` kullanın
   - Marka renkleri için Color.kt'deki tanımları kullanın
   - Tema değişikliklerinde otomatik uyum sağlar

2. **Tipografi Kullanımı**
   - `MaterialTheme.typography` ile tipografi stillerine erişin
   - Her metin türü için uygun stili seçin
   - Tutarlılık için özel TextStyle yerine tema stillerini tercih edin

3. **Tema Genişletme**
   - Yeni renkler eklemek için Color.kt'yi güncelleyin
   - Özel font eklemek için Type.kt'de FontFamily tanımlayın
   - Tema varyasyonları için Theme.kt'de yeni ColorScheme oluşturun

## 🔄 Güncellemeler

**Son Güncelleme**: 2025-11-28

### v1.0.0 (2025-11-28)
- ✅ Material Design 3 tam desteği
- ✅ Kapsamlı tipografi sistemi
- ✅ Organize edilmiş renk paleti
- ✅ Dynamic color desteği
- ✅ Tam dokümantasyon

## 📚 Kaynaklar

- [Material Design 3 Guidelines](https://m3.material.io/)
- [Compose Material3 Docs](https://developer.android.com/jetpack/compose/designsystems/material3)
- [Typography System](https://m3.material.io/styles/typography/overview)
- [Color System](https://m3.material.io/styles/color/overview)

---

*Bu dokümantasyon Rovits uygulaması için hazırlanmıştır.*

