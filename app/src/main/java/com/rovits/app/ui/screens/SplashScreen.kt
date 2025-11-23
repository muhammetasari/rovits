package com.rovits.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rovits.app.ui.components.RovitsLogo
import com.rovits.app.ui.theme.PrimaryOrange
import com.rovits.app.ui.theme.RovitsAppTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit
) {
    // Launch effect to navigate after delay
    LaunchedEffect(Unit) {
        delay(2500) // 2.5 seconds
        onNavigateToLogin()
    }

    // Rotation animation for the progress indicator
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
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

        // Rotating circular progress indicator around logo
        CircularProgressIndicator(
            modifier = Modifier
                .size(240.dp)
                .rotate(rotation),
            color = PrimaryOrange,
            strokeWidth = 5.dp,
            trackColor = PrimaryOrange.copy(alpha = 0.2f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    RovitsAppTheme {
        SplashScreen(onNavigateToLogin = {})
    }
}

