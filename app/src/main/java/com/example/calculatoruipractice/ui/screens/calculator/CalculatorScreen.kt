package com.example.calculatoruipractice.ui.screens.calculator

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculatoruipractice.ui.components.AnimatedCalculatorButton
import com.example.calculatoruipractice.ui.theme.CalculatorTheme
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val colors = CalculatorTheme.colors
    val context = LocalContext.current

    var dragOffset by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Заголовок
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🧮 КАЛЬКУЛЯТОР",
                color = colors.textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onToggleTheme, modifier = Modifier.size(40.dp)) {
                    Text(
                        text = if (isDarkTheme) "☀️" else "🌙",
                        fontSize = 20.sp
                    )
                }

                IconButton(onClick = { viewModel.toggleScientificMode() }, modifier = Modifier.size(40.dp)) {
                    Text(
                        text = if (state.isScientific) "🔬" else "🔭",
                        fontSize = 20.sp
                    )
                }

                IconButton(onClick = { viewModel.toggleHistory() }, modifier = Modifier.size(40.dp)) {
                    Icon(
                        imageVector = if (state.showHistory) {
                            Icons.Default.Close
                        } else {
                            Icons.Default.History
                        },
                        contentDescription = "История",
                        tint = colors.primary
                    )
                }
            }
        }

        // Основное содержимое с возможностью скролла
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // История
            AnimatedVisibility(
                visible = state.showHistory,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(animationSpec = tween(300)),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(200))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(bottom = 4.dp)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { isDragging = true },
                                onDragEnd = {
                                    isDragging = false
                                    if (dragOffset < -100f) {
                                        viewModel.toggleHistory()
                                    }
                                    dragOffset = 0f
                                },
                                onDragCancel = {
                                    isDragging = false
                                    dragOffset = 0f
                                }
                            ) { change, dragAmount ->
                                change.consume()
                                dragOffset += dragAmount.y
                            }
                        }
                ) {
                    val offsetY = if (isDragging) dragOffset.roundToInt() else 0

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .offset { IntOffset(0, offsetY) }
                    ) {
                        HistoryPanel(
                            history = viewModel.historyList,
                            onClearHistory = { viewModel.clearHistory() },
                            onExportHistory = { viewModel.exportHistory(context) },
                            colors = colors
                        )
                    }
                }
            }

            // Поля ввода - уменьшенные
            TextField(
                value = state.input,
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 28.sp,
                    color = colors.textPrimary,
                    textAlign = TextAlign.End
                ),
                placeholder = {
                    Text(
                        text = "0",
                        color = colors.textSecondary,
                        fontSize = 28.sp
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colors.textPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                readOnly = true
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = state.result,
                color = colors.primary,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(colors.surface, RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                textAlign = TextAlign.End,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ===== НАУЧНЫЕ КНОПКИ С ПРОКРУТКОЙ =====
            if (state.isScientific) {
                // Ограниченная высота для научных кнопок с прокруткой
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    androidx.compose.foundation.lazy.LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        item {
                            ScientificButtonsContent(
                                viewModel = viewModel,
                                state = state,
                                colors = colors
                            )
                        }
                    }
                }
            } else {
                // ===== ОСНОВНЫЕ КНОПКИ =====
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    MainButtonsContent(viewModel = viewModel, colors = colors)
                }
            }
        }
    }
}

@Composable
fun ScientificButtonsContent(
    viewModel: CalculatorViewModel,
    state: CalculatorState,
    colors: com.example.calculatoruipractice.ui.theme.CalculatorColors
) {
    Column {
        // Индикатор режима углов
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = { viewModel.toggleAngleMode() },
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp), // Увеличено с 30.dp до 36.dp
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.angleMode == AngleMode.DEG)
                        colors.primary else colors.secondary
                ),
                shape = RoundedCornerShape(8.dp), // Увеличено с 6.dp до 8.dp
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
            ) {
                Text(
                    text = if (state.angleMode == AngleMode.DEG) "DEG" else "RAD",
                    color = if (state.angleMode == AngleMode.DEG)
                        Color.White else colors.textPrimary,
                    fontSize = 12.sp, // Увеличено с 10.sp до 12.sp
                    fontWeight = FontWeight.Bold
                )
            }
            ScientificButton("sin⁻¹", { viewModel.onScientificFunction("sin⁻¹") }, colors, Modifier.weight(1f))
            ScientificButton("cos⁻¹", { viewModel.onScientificFunction("cos⁻¹") }, colors, Modifier.weight(1f))
            ScientificButton("tan⁻¹", { viewModel.onScientificFunction("tan⁻¹") }, colors, Modifier.weight(1f))
            ScientificButton("π", { viewModel.onScientificFunction("π") }, colors, Modifier.weight(1f))
            ScientificButton("e", { viewModel.onScientificFunction("e") }, colors, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(4.dp)) // Увеличено с 2.dp до 4.dp

        // Ряд 1: Тригонометрия + логарифмы
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ScientificButton("sin", { viewModel.onScientificFunction("sin") }, colors, Modifier.weight(1f))
            ScientificButton("cos", { viewModel.onScientificFunction("cos") }, colors, Modifier.weight(1f))
            ScientificButton("tan", { viewModel.onScientificFunction("tan") }, colors, Modifier.weight(1f))
            ScientificButton("log", { viewModel.onScientificFunction("log") }, colors, Modifier.weight(1f))
            ScientificButton("ln", { viewModel.onScientificFunction("ln") }, colors, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Ряд 2: Степени и корни
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ScientificButton("x²", { viewModel.onScientificFunction("x²") }, colors, Modifier.weight(1f))
            ScientificButton("x³", { viewModel.onScientificFunction("x³") }, colors, Modifier.weight(1f))
            ScientificButton("xʸ", { viewModel.onPowerClick() }, colors, Modifier.weight(1f))
            ScientificButton("√", { viewModel.onScientificFunction("√") }, colors, Modifier.weight(1f))
            ScientificButton("∛", { viewModel.onScientificFunction("∛") }, colors, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Ряд 3: Прочие функции
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ScientificButton("x!", { viewModel.onScientificFunction("x!") }, colors, Modifier.weight(1f))
            ScientificButton("1/x", { viewModel.onScientificFunction("1/x") }, colors, Modifier.weight(1f))
            ScientificButton("%", { viewModel.onScientificFunction("%") }, colors, Modifier.weight(1f))
            ScientificButton("±", { viewModel.onScientificFunction("±") }, colors, Modifier.weight(1f))
            ScientificButton("10ˣ", { viewModel.onScientificFunction("10ˣ") }, colors, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Ряд 4: Память
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MemoryButton("MC", { viewModel.memoryClear() }, colors, Modifier.weight(1f))
            MemoryButton("MR", { viewModel.memoryRecall() }, colors, Modifier.weight(1f))
            MemoryButton("M+", { viewModel.memoryAdd() }, colors, Modifier.weight(1f))
            MemoryButton("MS", { viewModel.memoryStore() }, colors, Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp) // Увеличено с 30.dp до 36.dp
                    .background(
                        color = if (state.memory != 0.0)
                            colors.primary.copy(alpha = 0.15f)
                        else
                            colors.surface,
                        shape = RoundedCornerShape(8.dp) // Увеличено с 6.dp до 8.dp
                    )
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (state.memory != 0.0) {
                        val memStr = if (state.memory == state.memory.toLong().toDouble()) {
                            state.memory.toLong().toString()
                        } else {
                            String.format("%.2f", state.memory)
                        }
                        "M=$memStr"
                    } else {
                        "M"
                    },
                    color = if (state.memory != 0.0) colors.primary else colors.textSecondary,
                    fontSize = 12.sp, // Увеличено с 10.sp до 12.sp
                    fontWeight = if (state.memory != 0.0) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp)) // Увеличено с 4.dp до 6.dp
        HorizontalDivider(
            color = colors.divider,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 4.dp) // Увеличено с 2.dp до 4.dp
        )

        // ===== ОСНОВНЫЕ КНОПКИ (в научном режиме они тоже нужны) =====
        MainButtonsContent(viewModel = viewModel, colors = colors)
    }
}

@Composable
fun MainButtonsContent(
    viewModel: CalculatorViewModel,
    colors: com.example.calculatoruipractice.ui.theme.CalculatorColors
) {
    Column {
        // Ряд 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AnimatedCalculatorButton(
                text = "7",
                onClick = { viewModel.onDigitClick("7") },
                backgroundColor = colors.buttonNumber,
                textColor = colors.textPrimary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            AnimatedCalculatorButton(
                text = "8",
                onClick = { viewModel.onDigitClick("8") },
                backgroundColor = colors.buttonNumber,
                textColor = colors.textPrimary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            AnimatedCalculatorButton(
                text = "9",
                onClick = { viewModel.onDigitClick("9") },
                backgroundColor = colors.buttonNumber,
                textColor = colors.textPrimary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            AnimatedCalculatorButton(
                text = "÷",
                onClick = { viewModel.onOperatorClick("÷") },
                backgroundColor = colors.buttonOperator,
                textColor = colors.primary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Ряд 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AnimatedCalculatorButton(
                text = "4",
                onClick = { viewModel.onDigitClick("4") },
                backgroundColor = colors.buttonNumber,
                textColor = colors.textPrimary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            AnimatedCalculatorButton(
                text = "5",
                onClick = { viewModel.onDigitClick("5") },
                backgroundColor = colors.buttonNumber,
                textColor = colors.textPrimary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            AnimatedCalculatorButton(
                text = "6",
                onClick = { viewModel.onDigitClick("6") },
                backgroundColor = colors.buttonNumber,
                textColor = colors.textPrimary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            AnimatedCalculatorButton(
                text = "×",
                onClick = { viewModel.onOperatorClick("×") },
                backgroundColor = colors.buttonOperator,
                textColor = colors.primary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Ряд 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AnimatedCalculatorButton(
                text = "1",
                onClick = { viewModel.onDigitClick("1") },
                backgroundColor = colors.buttonNumber,
                textColor = colors.textPrimary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            AnimatedCalculatorButton(
                text = "2",
                onClick = { viewModel.onDigitClick("2") },
                backgroundColor = colors.buttonNumber,
                textColor = colors.textPrimary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            AnimatedCalculatorButton(
                text = "3",
                onClick = { viewModel.onDigitClick("3") },
                backgroundColor = colors.buttonNumber,
                textColor = colors.textPrimary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            AnimatedCalculatorButton(
                text = "−",
                onClick = { viewModel.onOperatorClick("−") },
                backgroundColor = colors.buttonOperator,
                textColor = colors.primary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Ряд 4
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AnimatedCalculatorButton(
                text = "0",
                onClick = { viewModel.onDigitClick("0") },
                backgroundColor = colors.buttonNumber,
                textColor = colors.textPrimary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            AnimatedCalculatorButton(
                text = ".",
                onClick = { viewModel.onDigitClick(".") },
                backgroundColor = colors.buttonNumber,
                textColor = colors.textPrimary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            AnimatedCalculatorButton(
                text = "=",
                onClick = { viewModel.onEqualsClick() },
                backgroundColor = colors.primary,
                textColor = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            AnimatedCalculatorButton(
                text = "+",
                onClick = { viewModel.onOperatorClick("+") },
                backgroundColor = colors.buttonOperator,
                textColor = colors.primary,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Нижняя панель
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = { viewModel.onClearClick() },
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.error
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("CLEAR", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.onBackspaceClick() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.secondary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("⌫", color = colors.textPrimary, fontSize = 20.sp)
            }

            val context = LocalContext.current
            if (viewModel.historyList.isNotEmpty()) {
                Button(
                    onClick = { viewModel.exportHistory(context) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("💾", fontSize = 18.sp)
                }
            }
        }
    }
}

// ===== КОМПОНЕНТЫ =====

@Composable
fun HistoryPanel(
    history: List<String>,
    onClearHistory: () -> Unit,
    onExportHistory: () -> Unit,
    colors: com.example.calculatoruipractice.ui.theme.CalculatorColors
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surface)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📜 История",
                color = colors.textPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Row {
                TextButton(
                    onClick = onExportHistory,
                    enabled = history.isNotEmpty()
                ) {
                    Text("💾", fontSize = 20.sp)
                }

                TextButton(
                    onClick = onClearHistory,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colors.error
                    )
                ) {
                    Text("Очистить")
                }
            }
        }

        HorizontalDivider(color = colors.divider, thickness = 1.dp)

        if (history.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "История пуста\nСмахните вверх для закрытия",
                    color = colors.textSecondary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(history) { entry ->
                    Text(
                        text = entry,
                        color = colors.textPrimary,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    HorizontalDivider(
                        color = colors.divider.copy(alpha = 0.3f),
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

@Composable
fun ScientificButton(
    text: String,
    onClick: () -> Unit,
    colors: com.example.calculatoruipractice.ui.theme.CalculatorColors,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(36.dp), // Увеличено с 30.dp до 36.dp
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.secondary
        ),
        shape = RoundedCornerShape(8.dp), // Увеличено с 6.dp до 8.dp
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp) // Увеличено с 2.dp до 4.dp
    ) {
        Text(
            text = text,
            color = colors.textPrimary,
            fontSize = 12.sp, // Увеличено с 10.sp до 12.sp
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Composable
fun MemoryButton(
    text: String,
    onClick: () -> Unit,
    colors: com.example.calculatoruipractice.ui.theme.CalculatorColors,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(36.dp), // Увеличено с 30.dp до 36.dp
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.buttonOperator.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(8.dp), // Увеличено с 6.dp до 8.dp
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp) // Увеличено с 2.dp до 4.dp
    ) {
        Text(
            text = text,
            color = colors.primary,
            fontSize = 12.sp, // Увеличено с 10.sp до 12.sp
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

// ===== PREVIEW =====

@Preview(
    name = "Light - Basic",
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
fun CalculatorScreenLightPreview() {
    CalculatorTheme(darkTheme = false) {
        CalculatorScreen(
            isDarkTheme = false,
            onToggleTheme = {}
        )
    }
}

@Preview(
    name = "Dark - Basic",
    showBackground = true,
    backgroundColor = 0xFF1C1C1E
)
@Composable
fun CalculatorScreenDarkPreview() {
    CalculatorTheme(darkTheme = true) {
        CalculatorScreen(
            isDarkTheme = true,
            onToggleTheme = {}
        )
    }
}

@Preview(
    name = "Light - Scientific",
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
fun CalculatorScreenLightScientificPreview() {
    CalculatorTheme(darkTheme = false) {
        val viewModel = remember { CalculatorViewModel() }
        LaunchedEffect(Unit) {
            viewModel.toggleScientificMode()
        }
        CalculatorScreen(
            viewModel = viewModel,
            isDarkTheme = false,
            onToggleTheme = {}
        )
    }
}

@Preview(
    name = "Dark - Scientific",
    showBackground = true,
    backgroundColor = 0xFF1C1C1E
)
@Composable
fun CalculatorScreenDarkScientificPreview() {
    CalculatorTheme(darkTheme = true) {
        val viewModel = remember { CalculatorViewModel() }
        LaunchedEffect(Unit) {
            viewModel.toggleScientificMode()
        }
        CalculatorScreen(
            viewModel = viewModel,
            isDarkTheme = true,
            onToggleTheme = {}
        )
    }
}

@Preview(
    name = "With History",
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
fun CalculatorScreenWithHistoryPreview() {
    CalculatorTheme(darkTheme = false) {
        val viewModel = remember { CalculatorViewModel() }
        LaunchedEffect(Unit) {
            // Добавляем тестовые записи через публичный метод onEqualsClick
            // Вместо прямого доступа к _historyList
            viewModel.onDigitClick("5")
            viewModel.onOperatorClick("+")
            viewModel.onDigitClick("3")
            viewModel.onEqualsClick()

            viewModel.onDigitClick("12")
            viewModel.onOperatorClick("×")
            viewModel.onDigitClick("4")
            viewModel.onEqualsClick()

            viewModel.toggleHistory()
        }
        CalculatorScreen(
            viewModel = viewModel,
            isDarkTheme = false,
            onToggleTheme = {}
        )
    }
}