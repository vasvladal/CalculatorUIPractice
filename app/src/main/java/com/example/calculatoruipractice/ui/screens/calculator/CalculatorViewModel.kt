package com.example.calculatoruipractice.ui.screens.calculator

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.*

// ✅ Режимы углов для тригонометрии
enum class AngleMode { DEG, RAD }

data class CalculatorState(
    val input: String = "",
    val result: String = "",
    val firstNumber: Double = 0.0,
    val operation: String = "",
    val isNewNumber: Boolean = true,
    val history: List<String> = emptyList(),
    val showHistory: Boolean = false,
    val isScientific: Boolean = false,
    val memory: Double = 0.0,
    val angleMode: AngleMode = AngleMode.DEG // ✅ Режим углов
)

class CalculatorViewModel : ViewModel() {
    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    private val _historyList = mutableStateListOf<String>()
    val historyList: List<String> = _historyList

    // ===== ОСНОВНЫЕ ОПЕРАЦИИ =====
    fun onDigitClick(digit: String) {
        _state.update { state ->
            // ✅ Поддержка скобок: если последний символ "(", начинаем новое число
            val currentInput = state.input
            val lastChar = currentInput.lastOrNull()

            val newInput = when {
                state.isNewNumber && digit == "." -> "0."
                state.isNewNumber -> digit
                digit == "." && currentInput.contains(".") -> currentInput
                else -> currentInput + digit
            }
            state.copy(
                input = newInput,
                isNewNumber = false
            )
        }
    }

    fun onOperatorClick(operator: String) {
        val currentState = _state.value
        if (currentState.input.isNotEmpty()) {
            _state.update { state ->
                state.copy(
                    firstNumber = state.input.toDoubleOrNull() ?: state.firstNumber,
                    operation = operator,
                    isNewNumber = true,
                    result = "${state.input} $operator"
                )
            }
        }
    }

    fun onEqualsClick() {
        val state = _state.value
        if (state.operation.isNotEmpty() && state.input.isNotEmpty()) {
            try {
                val secondNumber = state.input.toDouble()
                val result = calculate(state.firstNumber, secondNumber, state.operation)
                val resultText = formatResult(result)
                val opSymbol = state.operation
                val historyEntry = "${formatResult(state.firstNumber)} $opSymbol ${formatResult(secondNumber)} = $resultText"
                _historyList.add(0, historyEntry)
                _state.update {
                    it.copy(
                        input = resultText,
                        result = historyEntry,
                        firstNumber = result,
                        operation = "",
                        isNewNumber = true,
                        history = _historyList.toList()
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(result = "Ошибка ввода")
                }
            }
        }
    }

    // ===== НАУЧНЫЕ ФУНКЦИИ =====
    fun onScientificFunction(function: String) {
        val currentState = _state.value

        // ✅ π и e работают как вставка числа, а не как функция от текущего ввода
        when (function) {
            "π" -> {
                _state.update { state ->
                    state.copy(
                        input = formatResult(PI),
                        isNewNumber = true,
                        result = "π = ${formatResult(PI)}"
                    )
                }
                return
            }
            "e" -> {
                _state.update { state ->
                    state.copy(
                        input = formatResult(E),
                        isNewNumber = true,
                        result = "e = ${formatResult(E)}"
                    )
                }
                return
            }
        }

        // Для остальных функций нужен ввод
        if (currentState.input.isEmpty()) return

        try {
            val value = currentState.input.toDouble()
            val mode = currentState.angleMode

            val result = when (function) {
                "sin" -> sin(toRadians(value, mode))
                "cos" -> cos(toRadians(value, mode))
                "tan" -> tan(toRadians(value, mode))
                "sin⁻¹" -> fromRadians(asin(value), mode)
                "cos⁻¹" -> fromRadians(acos(value), mode)
                "tan⁻¹" -> fromRadians(atan(value), mode)
                "log" -> log10(value)
                "ln" -> ln(value)
                "10ˣ" -> 10.0.pow(value)
                "eˣ" -> E.pow(value)
                "√" -> sqrt(value)
                "∛" -> cbrt(value)
                "x²" -> value.pow(2)
                "x³" -> value.pow(3)
                "1/x" -> if (value != 0.0) 1.0 / value else Double.NaN
                "x!" -> factorial(value)
                "%" -> value / 100.0
                "±" -> -value
                else -> value
            }

            val resultText = formatResult(result)
            val modeSuffix = if (function in listOf("sin", "cos", "tan", "sin⁻¹", "cos⁻¹", "tan⁻¹")) {
                if (mode == AngleMode.DEG) "°" else " rad"
            } else ""

            val historyEntry = "$function(${formatResult(value)}$modeSuffix) = $resultText"
            _historyList.add(0, historyEntry)
            _state.update {
                it.copy(
                    input = resultText,
                    result = historyEntry,
                    firstNumber = result,
                    isNewNumber = true,
                    history = _historyList.toList()
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(result = "Ошибка: ${e.message ?: "Недопустимое значение"}")
            }
        }
    }

    // ✅ Возведение в степень работает как оператор (ждёт второе число)
    fun onPowerClick() {
        val currentState = _state.value
        if (currentState.input.isNotEmpty()) {
            _state.update { state ->
                state.copy(
                    firstNumber = state.input.toDouble(),
                    operation = "^",
                    isNewNumber = true,
                    result = "${state.input} ^"
                )
            }
        }
    }

    // ===== РЕЖИМ УГЛОВ =====
    fun toggleAngleMode() {
        _state.update { state ->
            val newMode = if (state.angleMode == AngleMode.DEG) AngleMode.RAD else AngleMode.DEG
            state.copy(angleMode = newMode)
        }
    }

    // ===== ОПЕРАЦИИ С ПАМЯТЬЮ =====
    fun memoryStore() {
        val currentState = _state.value
        if (currentState.input.isNotEmpty()) {
            val value = currentState.input.toDoubleOrNull() ?: return
            _state.update { state ->
                state.copy(memory = value)
            }
        }
    }

    fun memoryRecall() {
        val currentState = _state.value
        if (currentState.memory != 0.0) {
            _state.update { state ->
                state.copy(
                    input = formatResult(currentState.memory),
                    isNewNumber = true
                )
            }
        }
    }

    fun memoryClear() {
        _state.update { state ->
            state.copy(memory = 0.0)
        }
    }

    fun memoryAdd() {
        val currentState = _state.value
        if (currentState.input.isNotEmpty()) {
            val value = currentState.input.toDoubleOrNull() ?: return
            _state.update { state ->
                state.copy(memory = state.memory + value)
            }
        }
    }

    // ===== УПРАВЛЕНИЕ ИСТОРИЕЙ =====
    fun toggleHistory() {
        _state.update { state ->
            state.copy(showHistory = !state.showHistory)
        }
    }

    fun clearHistory() {
        _historyList.clear()
        _state.update { state ->
            state.copy(history = emptyList())
        }
    }

    fun onBackspaceClick() {
        _state.update { state ->
            val newInput = if (state.input.isNotEmpty()) {
                state.input.dropLast(1)
            } else {
                ""
            }
            state.copy(input = newInput)
        }
    }

    fun onClearClick() {
        _state.value = CalculatorState(angleMode = _state.value.angleMode)
    }

    fun toggleScientificMode() {
        _state.update { state ->
            state.copy(isScientific = !state.isScientific)
        }
    }

    // ===== ЭКСПОРТ ИСТОРИИ =====
    fun exportHistory(context: Context) {
        if (_historyList.isEmpty()) {
            Toast.makeText(context, "История пуста", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val timestamp = System.currentTimeMillis()
            val fileName = "calculator_history_$timestamp.txt"
            val file = java.io.File(context.filesDir, fileName)
            file.bufferedWriter().use { writer ->
                writer.write("=== ИСТОРИЯ ВЫЧИСЛЕНИЙ КАЛЬКУЛЯТОРА ===\n")
                writer.write("Дата: ${java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}\n")
                writer.write("=".repeat(40) + "\n\n")
                _historyList.reversed().forEachIndexed { index, entry ->
                    writer.write("${index + 1}. $entry\n")
                }
                writer.write("\n" + "=".repeat(40) + "\n")
                writer.write("Всего операций: ${_historyList.size}")
            }
            Toast.makeText(
                context,
                "История сохранена: ${file.absolutePath}",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка сохранения: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // ===== ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ =====
    private fun toRadians(value: Double, mode: AngleMode): Double {
        return if (mode == AngleMode.DEG) Math.toRadians(value) else value
    }

    private fun fromRadians(value: Double, mode: AngleMode): Double {
        return if (mode == AngleMode.DEG) Math.toDegrees(value) else value
    }

    private fun factorial(value: Double): Double {
        if (value < 0) return Double.NaN
        if (value != value.toLong().toDouble()) return Double.NaN // только целые
        if (value > 170) return Double.POSITIVE_INFINITY
        var result = 1.0
        for (i in 2..value.toLong()) {
            result *= i
        }
        return result
    }

    private fun calculate(first: Double, second: Double, operation: String): Double {
        return when (operation) {
            "÷" -> if (second != 0.0) first / second else Double.NaN
            "×" -> first * second
            "−" -> first - second
            "+" -> first + second
            "^" -> first.pow(second) // ✅ Возведение в степень
            else -> 0.0
        }
    }

    private fun formatResult(value: Double): String {
        return when {
            value.isNaN() -> "Ошибка"
            value.isInfinite() -> if (value > 0) "∞" else "-∞"
            value == value.toLong().toDouble() -> value.toLong().toString()
            else -> {
                val rounded = round(value * 1_000_000_000) / 1_000_000_000
                val str = rounded.toString()
                if (str.endsWith(".0")) str.dropLast(2) else str
            }
        }
    }
}