package com.rovits.app.ui.theme

import androidx.compose.ui.graphics.Color

// ========================================
// MARKA RENKLERİ (Brand Colors)
// ========================================

/**
 * RovitsOrange - Ana Marka Rengi (#83331D)
 * Kullanım: Ana butonlar, FAB (Floating Action Button), NavigationBar aktif öğeler,
 *           Logo rengi, vurgulu başlıklar
 * Light Mode: Primary renk olarak kullanılır
 * Dark Mode: Primary renk olarak kullanılır, beyaz metin ile kontrast sağlar
 */
val RovitsOrange = Color(0xFF4CAF50)

/**
 * RovitsOrangeDark - Koyu Ton Marka Rengi (#E56B47)
 * Kullanım: PrimaryContainer, Secondary buton rengi, hover efektleri,
 *           Aktif durum göstergeleri
 * Light Mode: Secondary renk ve container arka planları
 * Dark Mode: PrimaryContainer olarak kullanılır
 */
val RovitsOrangeDark = Color(0xFFE56B47)

/**
 * RovitsOrangeLight - Açık Ton Marka Rengi (#FF9A7F)
 * Kullanım: İkincil vurgular, pasif durumlar, arka plan tonları
 * Light Mode: Hafif vurgular ve arka planlar
 * Dark Mode: Secondary renk olarak kullanılır
 */
val RovitsOrangeLight = Color(0xFFFF9A7F)

// ========================================
// SEMANTİK RENKLER (Semantic Colors)
// ========================================

/**
 * SuccessGreen - Başarı Rengi (#388E3C)
 * Kullanım: Başarılı işlem mesajları, onay ikon ve butonları, pozitif durum göstergeleri,
 *           "Kayıt Başarılı" bildirimleri
 * Light Mode: Tertiary renk olarak kullanılır
 * Dark Mode: Tertiary renk olarak kullanılır
 */
val SuccessGreen = Color(0xFF388E3C)

/**
 * WarningOrange - Uyarı Rengi (#F57C00)
 * Kullanım: Uyarı mesajları, dikkat gerektiren durumlar, önemli bildirimler
 * Light Mode: Uyarı badge ve banner'ları
 * Dark Mode: Tertiary renk olarak kullanılır, dikkat çekmek için
 */
val WarningOrange = Color(0xFFF57C00)

/**
 * ErrorRed - Hata Rengi (#D32F2F)
 * Kullanım: Hata mesajları, form validasyon hataları, silme işlemi butonları,
 *           Başarısız işlem bildirimleri
 * Light Mode: Error renk olarak kullanılır
 * Dark Mode: Hafif tonlamayla (#CF6679) error rengi olarak kullanılır
 */
val ErrorRed = Color(0xFFD32F2F)

// ========================================
// NÖTR RENKLER (Neutral Colors)
// ========================================

/**
 * Gray50 - En Açık Gri (#F5F5F5)
 * Kullanım: SurfaceVariant, Card arka planları, bölüm ayırıcıları,
 *           Input field arka planları, seçili olmayan tab arka planları
 * Light Mode: SurfaceVariant, SecondaryContainer
 * Dark Mode: OnBackground ve OnSurface text rengi
 */
val Gray50 = Color(0xFFF5F5F5)

/**
 * Gray200 - Açık Gri (#E0E0E0)
 * Kullanım: Outline, border, divider (ayırıcı çizgiler),
 *           Disabled durumdaki elementlerin kenarlıkları
 * Light Mode: Outline rengi, border'lar
 * Dark Mode: Kullanılmaz
 */
val Gray200 = Color(0xFFE0E0E0)

/**
 * Gray400 - Orta Açık Gri (#B3B3B3)
 * Kullanım: Placeholder text, ikincil bilgiler, disabled text,
 *           Hint metinleri, yardımcı açıklamalar
 * Light Mode: Kullanılmaz
 * Dark Mode: OnSurfaceVariant text rengi
 */
val Gray400 = Color(0xFFB3B3B3)

/**
 * Gray600 - Orta Koyu Gri (#666666)
 * Kullanım: İkincil metinler, açıklamalar, etiketler,
 *           Timestamp, meta bilgiler
 * Light Mode: OnSurfaceVariant text rengi
 * Dark Mode: Kullanılmaz
 */
val Gray600 = Color(0xFF666666)

/**
 * Gray900 - En Koyu Gri (#1A1A1A)
 * Kullanım: Ana text rengi, başlıklar, önemli bilgiler
 * Light Mode: OnBackground, OnSurface ana text rengi, OnPrimaryContainer, OnSecondaryContainer
 * Dark Mode: OnSecondary text rengi
 */
val Gray900 = Color(0xFF1A1A1A)

// ========================================
// DARK MODE ÖZEL RENKLERİ (Dark Mode Specific Colors)
// ========================================

/**
 * DarkBackground - Dark Mode Ana Arka Plan (#121212)
 * Kullanım: Dark Mode'da background rengi olarak kullanılır
 */
val DarkBackground = Color(0xFF121212)

/**
 * DarkSurface - Dark Mode Yüzey Rengi (#1E1E1E)
 * Kullanım: Dark Mode'da Card, Dialog, BottomSheet gibi yüzey arka planları
 */
val DarkSurface = Color(0xFF1E1E1E)

/**
 * DarkSurfaceVariant - Dark Mode Varyant Yüzey Rengi (#2C2C2C)
 * Kullanım: Dark Mode'da alternatif yüzey rengi, seçili öğe arka planları
 */
val DarkSurfaceVariant = Color(0xFF2C2C2C)

/**
 * DarkOutline - Dark Mode Kenarlık Rengi (#3E3E3E)
 * Kullanım: Dark Mode'da border, divider, outline rengi
 */
val DarkOutline = Color(0xFF3E3E3E)

/**
 * DarkError - Dark Mode Hata Rengi (#CF6679)
 * Kullanım: Dark Mode'da error rengi (ErrorRed'in daha yumuşak tonu)
 */
val DarkError = Color(0xFFCF6679)

/**
 * LightPrimaryContainer - Light Mode Primary Container (#FFE5DC)
 * Kullanım: Light Mode'da primary container arka planı
 */
val LightPrimaryContainer = Color(0xFFFFE5DC)

/**
 * LightErrorContainer - Light Mode Error Container (#FFEBEE)
 * Kullanım: Light Mode'da error container arka planı
 */
val LightErrorContainer = Color(0xFFFFEBEE)

/**
 * LightOnErrorContainer - Light Mode Error Container Text (#B71C1C)
 * Kullanım: Light Mode'da error container üzerindeki text rengi
 */
val LightOnErrorContainer = Color(0xFFB71C1C)

/**
 * LightOutlineVariant - Light Mode Outline Variant (#CCCCCC)
 * Kullanım: Light Mode'da alternatif outline/border rengi
 */
val LightOutlineVariant = Color(0xFFCCCCCC)

