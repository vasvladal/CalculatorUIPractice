package com.example.calculatoruipractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatoruipractice.ui.theme.YourCalculatorAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourCalculatorAppTheme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var firstNumber by remember { mutableDoubleStateOf(0.0) }
    var operation by remember { mutableStateOf("") }
    var isNewNumber by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1C1C1E)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "🧮 КАЛЬКУЛЯТОР",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )

            TextField(
                value = input,
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 32.sp,
                    color = Color.White,
                    textAlign = TextAlign.End
                ),
                placeholder = {
                    Text(
                        text = "0",
                        color = Color.Gray,
                        fontSize = 32.sp
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF2C2C2E),
                    unfocusedContainerColor = Color(0xFF2C2C2E),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                readOnly = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = result,
                color = Color(0xFFFF9500),
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFF2C2C2E), RoundedCornerShape(12.dp))
                    .padding(16.dp),
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = Color(0xFF3A3A3C),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // ===== РЯД 1 =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),  // 👈 ВЫСОТА ЗДЕСЬ
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CalculatorButton(
                    text = "7",
                    onClick = {
                        if (isNewNumber) {
                            input = "7"
                            isNewNumber = false
                        } else {
                            input += "7"
                        }
                    },
                    modifier = Modifier.weight(1f)  // 👈 ВЕС ПЕРЕДАЁТСЯ СЮДА
                )
                CalculatorButton(
                    text = "8",
                    onClick = {
                        if (isNewNumber) {
                            input = "8"
                            isNewNumber = false
                        } else {
                            input += "8"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                CalculatorButton(
                    text = "9",
                    onClick = {
                        if (isNewNumber) {
                            input = "9"
                            isNewNumber = false
                        } else {
                            input += "9"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                OperatorButton(
                    text = "÷",
                    onClick = {
                        if (input.isNotEmpty()) {
                            firstNumber = input.toDouble()
                            operation = "÷"
                            isNewNumber = true
                            result = "$firstNumber ÷"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ===== РЯД 2 =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CalculatorButton(
                    text = "4",
                    onClick = {
                        if (isNewNumber) {
                            input = "4"
                            isNewNumber = false
                        } else {
                            input += "4"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                CalculatorButton(
                    text = "5",
                    onClick = {
                        if (isNewNumber) {
                            input = "5"
                            isNewNumber = false
                        } else {
                            input += "5"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                CalculatorButton(
                    text = "6",
                    onClick = {
                        if (isNewNumber) {
                            input = "6"
                            isNewNumber = false
                        } else {
                            input += "6"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                OperatorButton(
                    text = "×",
                    onClick = {
                        if (input.isNotEmpty()) {
                            firstNumber = input.toDouble()
                            operation = "×"
                            isNewNumber = true
                            result = "$firstNumber ×"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ===== РЯД 3 =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CalculatorButton(
                    text = "1",
                    onClick = {
                        if (isNewNumber) {
                            input = "1"
                            isNewNumber = false
                        } else {
                            input += "1"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                CalculatorButton(
                    text = "2",
                    onClick = {
                        if (isNewNumber) {
                            input = "2"
                            isNewNumber = false
                        } else {
                            input += "2"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                CalculatorButton(
                    text = "3",
                    onClick = {
                        if (isNewNumber) {
                            input = "3"
                            isNewNumber = false
                        } else {
                            input += "3"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                OperatorButton(
                    text = "−",
                    onClick = {
                        if (input.isNotEmpty()) {
                            firstNumber = input.toDouble()
                            operation = "−"
                            isNewNumber = true
                            result = "$firstNumber −"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ===== РЯД 4 =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CalculatorButton(
                    text = "0",
                    onClick = {
                        if (isNewNumber) {
                            input = "0"
                            isNewNumber = false
                        } else {
                            input += "0"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                CalculatorButton(
                    text = ".",
                    onClick = {
                        if (!input.contains(".")) {
                            input += "."
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                EqualsButton(
                    onClick = {
                        if (operation.isNotEmpty() && input.isNotEmpty()) {
                            try {
                                val secondNumber = input.toDouble()
                                var calculationResult = 0.0

                                when (operation) {
                                    "÷" -> {
                                        if (secondNumber != 0.0) {
                                            calculationResult = firstNumber / secondNumber
                                        } else {
                                            result = "Ошибка: деление на 0"
                                            return@EqualsButton
                                        }
                                    }
                                    "×" -> calculationResult = firstNumber * secondNumber
                                    "−" -> calculationResult = firstNumber - secondNumber
                                    "+" -> calculationResult = firstNumber + secondNumber
                                }

                                val resultText = if (calculationResult == calculationResult.toLong().toDouble()) {
                                    calculationResult.toLong().toString()
                                } else {
                                    calculationResult.toString()
                                }

                                input = resultText
                                result = "$firstNumber $operation $secondNumber = $resultText"
                                firstNumber = calculationResult
                                operation = ""
                                isNewNumber = true
                            } catch (_: Exception) {
                                result = "Ошибка ввода"
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                OperatorButton(
                    text = "+",
                    onClick = {
                        if (input.isNotEmpty()) {
                            firstNumber = input.toDouble()
                            operation = "+"
                            isNewNumber = true
                            result = "$firstNumber +"
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    input = ""
                    result = ""
                    firstNumber = 0.0
                    operation = ""
                    isNewNumber = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF3B30)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "CLEAR",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ===== КОМПОНЕНТЫ КНОПОК (БЕЗ weight ВНУТРИ) =====

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxHeight()  // 👈 ЗАПОЛНЯЕМ ВСЮ ВЫСОТУ ROW
            .height(70.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF333335)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun OperatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxHeight()
            .height(70.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2C2C2E)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFFFF9500),
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EqualsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxHeight()
            .height(70.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF9500)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(
            text = "=",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    YourCalculatorAppTheme {
        CalculatorApp()
    }
}