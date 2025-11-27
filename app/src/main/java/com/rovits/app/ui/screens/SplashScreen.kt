package com.rovits.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rovits.app.ui.components.RovitsLogo
import com.rovits.app.ui.theme.RovitsAppTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit
) {
    // Launch effect to navigate after delay
    LaunchedEffect(Unit) {
        delay(2750)
        onNavigateToLogin()
    }

    // Rotation animation for the progress indicator
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Logo in center
        RovitsLogo(size = 240.dp)

        // Rotating airplane icon around logo
        Box(
            modifier = Modifier
                .size(261.dp)
                .rotate(rotation),
            contentAlignment = Alignment.TopCenter
        ) {
            Icon(
                imageVector = Icons.Default.Flight,
                contentDescription = "Loading",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(32.dp)
                    .graphicsLayer(rotationZ = 90f) // Uçağı yörünge yönüne çevir
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    RovitsAppTheme {
        SplashScreen(onNavigateToLogin = {})
    }
}
