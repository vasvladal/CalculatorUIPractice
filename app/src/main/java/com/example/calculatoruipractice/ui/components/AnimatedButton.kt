package com.example.calculatoruipractice.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedCalculatorButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    // ✅ Создаем InteractionSource для отслеживания состояния кнопки
    val interactionSource = remember { MutableInteractionSource() }

    // ✅ Автоматически получаем состояние "нажата ли кнопка прямо сейчас"
    val isPressed by interactionSource.collectIsPressedAsState()

    // Анимация масштаба на основе состояния isPressed
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Box(
        modifier = modifier.scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick, // ✅ Передаем onClick напрямую в Button
            modifier = Modifier.fillMaxSize(),
            interactionSource = interactionSource, // ✅ Подключаем наш interactionSource
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor
            ),
            shape = RoundedCornerShape(16.dp),
            // Настраиваем elevation: при нажатии тень исчезает
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 0.dp
            )
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}