package com.rovits.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rovits.app.data.model.User
import com.rovits.app.ui.theme.RovitsAppTheme

// Renk Paleti
private val DarkBlue = Color(0xFF1A1E3D)
private val SoftMint = Color(0xFFE0F7FA)
private val TextGray = Color(0xFF888888)
private val BgWhite = Color(0xFFF9F9F9)
private val IconBlue = Color(0xFF4A90E2)
private val ActiveGreen = Color(0xFF4CAF50)

@Composable
fun HomeScreen(
    user: User?,
    onLogout: () -> Unit
) {
    Scaffold(
        containerColor = BgWhite,
        // 1. Üst Kısım (App Bar)
        topBar = {
            CustomTopAppBar(onLogout = onLogout)
        },
        // 2. Alt Kısım (Bottom Bar)
        bottomBar = {
            CustomBottomBar()
        }
    ) { paddingValues ->
        // 3. İçerik (Content)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // App Bar olduğu için üst boşluğa gerek kalmadı, doğrudan içerik başlıyor.
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Kategoriler Başlığı
            item {
                SectionHeader(title = "Categories", actionText = "See All")
            }

            // Kategori Listesi
            item {
                CategoryList()
            }

            // Hotels Başlığı
            item {
                Text(
                    text = "Hotels",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // Tarih Seçimi
            item {
                DateSelectionRow()
            }

            // Lokasyon Arama (İkinci Arama Çubuğu)
            item {
                SecondarySearchBar()
            }

            // Otel Kartları
            item {
                DestinationList()
            }

            // Listenin en altına biraz boşluk
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

// --- App Bar (Arama ve Filtre) ---
@Composable
fun CustomTopAppBar(onLogout: () -> Unit) {
    // Arkaplanın beyaz görünmemesi için Surface kullanmıyoruz, doğrudan Row ile yerleştiriyoruz.
    // Scaffold background'ı zaten beyaz/gri.
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgWhite) // Scaffold rengiyle aynı
            .statusBarsPadding()
            .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Arama Çubuğu
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Search your destination",
                        color = TextGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Filtre Butonu
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = DarkBlue,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filter",
                        tint = Color.White
                    )
                }
            }

            // Logout Butonu (Test İçin)
            Surface(
                modifier = Modifier
                    .size(56.dp)
                    .clickable(onClick = onLogout),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFF5252), // Kırmızı renk belirgin olması için
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

// --- Bottom Bar ---
@Composable
fun CustomBottomBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(80.dp) // Görseldeki gibi biraz yüksek
    ) {
        val navItems = listOf(
            Icons.Outlined.Home,
            Icons.Outlined.FavoriteBorder,
            Icons.Outlined.Explore, // Pusula yerine
            Icons.Outlined.CalendarToday,
            Icons.Outlined.Person
        )

        // Sadece görsel amaçlı state, Home seçili varsayalım
        var selectedIndex by remember { mutableIntStateOf(0) }

        navItems.forEachIndexed { index, icon ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { selectedIndex = index },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = if (selectedIndex == index) DarkBlue else TextGray
                        )
                        // Seçili ise alttaki yeşil çizgi
                        if (selectedIndex == index) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(ActiveGreen)
                            )
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent // Arkaplan balonu olmasın
                )
            )
        }
    }
}

// --- Diğer Bileşenler (Değişmeyenler) ---

@Composable
fun SectionHeader(title: String, actionText: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = actionText,
            style = MaterialTheme.typography.bodyMedium,
            color = ActiveGreen
        )
    }
}

@Composable
fun CategoryList() {
    val categories = listOf(
        Icons.Default.Hotel to "Hotel",
        Icons.Default.Flight to "Flight",
        Icons.Default.Place to "Place",
        Icons.Default.Restaurant to "Food"
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(categories) { (icon, _) ->
            CategoryItem(icon = icon)
        }
    }
}

@Composable
fun CategoryItem(icon: ImageVector) {
    Surface(
        modifier = Modifier.size(72.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.background(SoftMint.copy(alpha = 0.3f))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = IconBlue
            )
        }
    }
}

@Composable
fun DateSelectionRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DateItem(date = "2022-08-06", modifier = Modifier.weight(1f))
        DateItem(date = "2022-08-09", modifier = Modifier.weight(1f))
    }
}

@Composable
fun DateItem(date: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(12.dp),
        color = SoftMint.copy(alpha = 0.5f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = DarkBlue
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = DarkBlue
            )
        }
    }
}

@Composable
fun SecondarySearchBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        color = SoftMint.copy(alpha = 0.4f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Location",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Search Location",
                color = TextGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun DestinationList() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(3) {
            DestinationCard()
        }
    }
}

@Composable
fun DestinationCard() {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(160.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(32.dp),
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.3f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenDesignPreview() {
    RovitsAppTheme {
        HomeScreen(user = null, onLogout = {})
    }
}
