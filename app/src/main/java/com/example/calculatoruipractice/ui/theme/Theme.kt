package com.example.calculatoruipractice.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CalculatorColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val secondary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val buttonNumber: Color,
    val buttonOperator: Color,
    val divider: Color,
    val error: Color
)

val LightCalculatorColors = CalculatorColors(
    background = LightColors.Background,
    surface = LightColors.Surface,
    primary = LightColors.Primary,
    secondary = LightColors.Secondary,
    textPrimary = LightColors.TextPrimary,
    textSecondary = LightColors.TextSecondary,
    buttonNumber = LightColors.ButtonNumber,
    buttonOperator = LightColors.ButtonOperator,
    divider = LightColors.Divider,
    error = LightColors.Error
)

val DarkCalculatorColors = CalculatorColors(
    background = DarkColors.Background,
    surface = DarkColors.Surface,
    primary = DarkColors.Primary,
    secondary = DarkColors.Secondary,
    textPrimary = DarkColors.TextPrimary,
    textSecondary = DarkColors.TextSecondary,
    buttonNumber = DarkColors.ButtonNumber,
    buttonOperator = DarkColors.ButtonOperator,
    divider = DarkColors.Divider,
    error = DarkColors.Error
)

val LocalCalculatorColors = staticCompositionLocalOf { LightCalculatorColors }

@Composable
fun CalculatorTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkCalculatorColors else LightCalculatorColors
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = colors.primary,
            background = colors.background,
            surface = colors.surface
        )
    } else {
        lightColorScheme(
            primary = colors.primary,
            background = colors.background,
            surface = colors.surface
        )
    }
    CompositionLocalProvider(
        LocalCalculatorColors provides colors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = CalculatorTypography,
            content = content
        )
    }
}

object CalculatorTheme {
    val colors: CalculatorColors
        @Composable
        get() = LocalCalculatorColors.current
}