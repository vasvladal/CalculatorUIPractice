package com.example.calculatoruipractice.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatoruipractice.ui.theme.CalculatorColors
import com.example.calculatoruipractice.ui.theme.CalculatorTheme

// ===== Общая шапка с кнопкой «Назад» (используется всеми новыми экранами) =====
@Composable
fun SettingsTopBar(title: String, onBack: () -> Unit, colors: CalculatorColors) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Назад",
                tint = colors.primary
            )
        }
        Text(
            text = title,
            color = colors.textPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ===== Строка-пункт меню (клик → переход) =====
@Composable
fun SettingsClickRow(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    colors: CalculatorColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 22.sp)
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = colors.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, color = colors.textSecondary, fontSize = 13.sp)
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = colors.textSecondary
        )
    }
}

// ===== ЭКРАН «НАСТРОЙКИ» =====
@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onOpenAbout: () -> Unit,
    onBack: () -> Unit
) {
    val colors = CalculatorTheme.colors
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        SettingsTopBar("⚙️ Настройки", onBack, colors)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(colors.surface, RoundedCornerShape(16.dp))
        ) {
            // Пункт «Тема» с переключателем
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = if (isDarkTheme) "🌙" else "☀️", fontSize = 22.sp)
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text("Тема", color = colors.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.Medium)
                    Text(
                        text = if (isDarkTheme) "Тёмная" else "Светлая",
                        color = colors.textSecondary,
                        fontSize = 13.sp
                    )
                }
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onToggleTheme() },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = colors.primary,
                        checkedThumbColor = Color.White,
                        uncheckedTrackColor = colors.secondary
                    )
                )
            }
            HorizontalDivider(color = colors.divider, thickness = 1.dp)
            // Подменю «О программе»
            SettingsClickRow(
                icon = "ℹ️",
                title = "О программе",
                subtitle = "Версия и руководство пользователя",
                onClick = onOpenAbout,
                colors = colors
            )
        }

        Text(
            text = "Настройки сохраняются автоматически",
            color = colors.textSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
    }
}