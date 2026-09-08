package com.example.calculatoruipractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview  // ← ВАЖНО: импорт Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatoruipractice.ui.theme.CalculatorUIPracticeTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorUIPracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorV1()
                }
            }
        }
    }
}

// 👇 ДОБАВЛЕН PREVIEW
@Preview(
    showBackground = true,
    name = "Calculator Preview",
    device = "id:pixel_5"
)
@Composable
fun CalculatorV1() {
    // состояние калькулятора
    var displayText by remember { mutableStateOf("0") }
    var operand1 by remember { mutableStateOf<Double?>(null) }
    var pendingOperator by remember { mutableStateOf<String?>(null) }
    // true when the NEXT digit press should start a brand-new number
    // instead of appending to what's on screen (set after an operator or "=")
    var resetDisplayOnNextInput by remember { mutableStateOf(false) }

    fun applyOperator(op1: Double, op2: Double, operator: String): Double {
        return when (operator) {
            "+" -> op1 + op2
            "-" -> op1 - op2
            "×" -> op1 * op2
            "÷" -> if (op2 != 0.0) op1 / op2 else Double.NaN
            else -> op2
        }
    }

    fun formatResult(value: Double): String {
        if (value.isNaN()) return "Error"
        if (value == value.toLong().toDouble()) return value.toLong().toString()
        // Round to 8 decimal places, then trim trailing zeros / dangling dot
        val rounded = String.format(Locale.US, "%.8f", value)
            .trimEnd('0')
            .trimEnd('.')
        return rounded
    }

    fun onDigit(digit: String) {
        if (resetDisplayOnNextInput || displayText == "0") {
            displayText = digit
            resetDisplayOnNextInput = false
        } else {
            displayText += digit
        }
    }

    fun onDecimal() {
        if (resetDisplayOnNextInput) {
            displayText = "0."
            resetDisplayOnNextInput = false
        } else if (!displayText.contains(".")) {
            displayText += "."
        }
    }

    fun onClear() {
        displayText = "0"
        operand1 = null
        pendingOperator = null
        resetDisplayOnNextInput = false
    }

    fun onOperator(op: String) {
        val currentValue = displayText.toDoubleOrNull() ?: 0.0
        if (operand1 != null && pendingOperator != null && !resetDisplayOnNextInput) {
            // chain operations: evaluate what's pending first, e.g. 5 + 3 + 2
            val result = applyOperator(operand1!!, currentValue, pendingOperator!!)
            operand1 = result
            displayText = formatResult(result)
        } else {
            operand1 = currentValue
        }
        pendingOperator = op
        resetDisplayOnNextInput = true
    }

    fun onEquals() {
        val currentValue = displayText.toDoubleOrNull() ?: 0.0
        if (operand1 != null && pendingOperator != null) {
            val result = applyOperator(operand1!!, currentValue, pendingOperator!!)
            displayText = formatResult(result)
            operand1 = null
            pendingOperator = null
            resetDisplayOnNextInput = true
        }
    }

    // интерфейс калькулятора
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = displayText,
            fontSize = when {
                displayText.length > 12 -> 32.sp
                displayText.length > 9 -> 40.sp
                displayText.length > 6 -> 52.sp
                else -> 64.sp
            },
            textAlign = TextAlign.End,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .padding(bottom = 16.dp),
            maxLines = 1
        )

        Column(
            modifier = Modifier.weight(3f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                CalcButtonV1("7", Modifier.weight(1f)) { onDigit("7") }
                CalcButtonV1("8", Modifier.weight(1f)) { onDigit("8") }
                CalcButtonV1("9", Modifier.weight(1f)) { onDigit("9") }
                CalcButtonV1("÷", Modifier.weight(1f)) { onOperator("÷") }
            }
            // Row 2
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                CalcButtonV1("4", Modifier.weight(1f)) { onDigit("4") }
                CalcButtonV1("5", Modifier.weight(1f)) { onDigit("5") }
                CalcButtonV1("6", Modifier.weight(1f)) { onDigit("6") }
                CalcButtonV1("×", Modifier.weight(1f)) { onOperator("×") }
            }
            // Row 3
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                CalcButtonV1("1", Modifier.weight(1f)) { onDigit("1") }
                CalcButtonV1("2", Modifier.weight(1f)) { onDigit("2") }
                CalcButtonV1("3", Modifier.weight(1f)) { onDigit("3") }
                CalcButtonV1("-", Modifier.weight(1f)) { onOperator("-") }
            }
            // Row 4
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                CalcButtonV1("C", Modifier.weight(1f)) { onClear() }
                CalcButtonV1("0", Modifier.weight(1f)) { onDigit("0") }
                CalcButtonV1(".", Modifier.weight(1f)) { onDecimal() }
                CalcButtonV1("+", Modifier.weight(1f)) { onOperator("+") }
            }
            // Row 5
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                CalcButtonV1("=", Modifier.fillMaxWidth()) { onEquals() }
            }
        }
    }
}

@Composable
fun CalcButtonV1(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxHeight(),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = label,
            fontSize = 24.sp
        )
    }
}