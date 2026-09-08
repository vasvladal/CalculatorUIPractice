package com.example.calculatoruipractice.ui.theme // ✅ ПРАВИЛЬНЫЙ ПАКЕТ

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF9500),
    secondary = Color(0xFF333335),
    background = Color(0xFF1C1C1E),
    surface = Color(0xFF2C2C2E)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF9500),
    secondary = Color(0xFFE0E0E0),
    background = Color.White,
    surface = Color(0xFFF5F5F5)
)

@Composable
fun YourCalculatorAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}