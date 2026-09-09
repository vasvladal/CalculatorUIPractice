package com.example.calculatoruipractice.ui.screens.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatoruipractice.BuildConfig
import com.example.calculatoruipractice.ui.screens.settings.SettingsClickRow
import com.example.calculatoruipractice.ui.screens.settings.SettingsTopBar
import com.example.calculatoruipractice.ui.theme.CalculatorTheme

@Composable
fun AboutScreen(
    onBack: () -> Unit,
    onOpenGuide: () -> Unit
) {
    val colors = CalculatorTheme.colors
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        SettingsTopBar("ℹ️ О программе", onBack, colors)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            // Карточка с информацией о приложении
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.surface, RoundedCornerShape(16.dp))
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🧮", fontSize = 64.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "CalculatorUIPractice",
                    color = colors.textPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Версия ${BuildConfig.VERSION_NAME} (код ${BuildConfig.VERSION_CODE})",
                    color = colors.textSecondary,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Научный калькулятор с историей вычислений, экспортом в файл, " +
                            "блоком памяти, режимами углов DEG/RAD и светлой/тёмной темой.",
                    color = colors.textSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text("© 2026, учебный проект", color = colors.textSecondary, fontSize = 12.sp)
            }

            Spacer(Modifier.height(12.dp))

            // Пункт, вызывающий руководство
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.surface, RoundedCornerShape(16.dp))
            ) {
                SettingsClickRow(
                    icon = "📖",
                    title = "Руководство пользователя",
                    subtitle = "Инструкции и примеры: ctg, sec, csc, память, история",
                    onClick = onOpenGuide,
                    colors = colors
                )
            }
        }
    }
}