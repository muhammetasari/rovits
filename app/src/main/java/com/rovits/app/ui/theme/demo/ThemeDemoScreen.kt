package com.rovits.app.ui.theme.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rovits.app.ui.theme.RovitsAppTheme

/**
 * Theme Demo Screen
 *
 * Bu ekran, Rovits uygulamasının tema sistemini gösterir.
 * Tüm renk ve tipografi stillerinin nasıl göründüğünü test etmek için kullanılır.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeDemoScreen(
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tema Demosu") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Typography Section
            TypographySection()

            HorizontalDivider()

            // Colors Section
            ColorsSection()

            HorizontalDivider()

            // Components Section
            ComponentsSection()
        }
    }
}

@Composable
private fun TypographySection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "📝 Tipografi Örnekleri",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        // Display Styles
        Text(
            text = "Display Large",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Display Medium",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Display Small",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Headline Styles
        Text(
            text = "Headline Large",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Headline Medium",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Headline Small",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Title Styles
        Text(
            text = "Title Large",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Title Medium",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Title Small",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Body Styles
        Text(
            text = "Body Large - Bu metin büyük gövde stili kullanır",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Body Medium - Bu metin orta gövde stili kullanır",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Body Small - Bu metin küçük gövde stili kullanır",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Label Styles
        Text(
            text = "Label Large",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Label Medium",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Label Small",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun ColorsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "🎨 Renk Paleti",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        // Primary Colors
        ColorItem("Primary", MaterialTheme.colorScheme.primary)
        ColorItem("On Primary", MaterialTheme.colorScheme.onPrimary)
        ColorItem("Primary Container", MaterialTheme.colorScheme.primaryContainer)
        ColorItem("On Primary Container", MaterialTheme.colorScheme.onPrimaryContainer)

        // Secondary Colors
        ColorItem("Secondary", MaterialTheme.colorScheme.secondary)
        ColorItem("On Secondary", MaterialTheme.colorScheme.onSecondary)
        ColorItem("Secondary Container", MaterialTheme.colorScheme.secondaryContainer)
        ColorItem("On Secondary Container", MaterialTheme.colorScheme.onSecondaryContainer)

        // Tertiary Colors
        ColorItem("Tertiary", MaterialTheme.colorScheme.tertiary)
        ColorItem("On Tertiary", MaterialTheme.colorScheme.onTertiary)

        // Surface Colors
        ColorItem("Surface", MaterialTheme.colorScheme.surface)
        ColorItem("On Surface", MaterialTheme.colorScheme.onSurface)
        ColorItem("Surface Variant", MaterialTheme.colorScheme.surfaceVariant)
        ColorItem("On Surface Variant", MaterialTheme.colorScheme.onSurfaceVariant)

        // Error Colors
        ColorItem("Error", MaterialTheme.colorScheme.error)
        ColorItem("On Error", MaterialTheme.colorScheme.onError)
        ColorItem("Error Container", MaterialTheme.colorScheme.errorContainer)
        ColorItem("On Error Container", MaterialTheme.colorScheme.onErrorContainer)
    }
}

@Composable
private fun ColorItem(name: String, color: androidx.compose.ui.graphics.Color) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        color = color,
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun ComponentsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "🧩 Bileşen Örnekleri",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        // Buttons
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Primary Button")
        }

        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Outlined Button")
        }

        TextButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Text Button")
        }

        // Cards
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Card Başlığı",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Bu bir card içeriği örneğidir. Card'lar içerik gruplamak için kullanılır.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Elevated Card
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Elevated Card",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Bu yükseltilmiş bir card örneğidir.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Outlined Card
        OutlinedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Outlined Card",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Bu çerçeveli bir card örneğidir.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// Preview for Light Theme
@Preview(name = "Light Theme", showBackground = true)
@Composable
private fun ThemeDemoLightPreview() {
    RovitsAppTheme(darkTheme = false) {
        ThemeDemoScreen()
    }
}

// Preview for Dark Theme
@Preview(name = "Dark Theme", showBackground = true)
@Composable
private fun ThemeDemoDarkPreview() {
    RovitsAppTheme(darkTheme = true) {
        ThemeDemoScreen()
    }
}

