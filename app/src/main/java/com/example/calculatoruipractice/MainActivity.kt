package com.example.calculatoruipractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.calculatoruipractice.data.PreferencesManager
import com.example.calculatoruipractice.ui.screens.calculator.CalculatorScreen
import com.example.calculatoruipractice.ui.theme.CalculatorTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferencesManager = PreferencesManager(this)

        setContent {
            val isDarkTheme by preferencesManager.themeFlow.collectAsState(initial = false)
            val onToggleTheme: () -> Unit = {
                lifecycleScope.launch {
                    preferencesManager.saveTheme(!isDarkTheme)
                }
            }

            CalculatorTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CalculatorScreen(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = onToggleTheme
                    )
                }
            }
        }
    }
}