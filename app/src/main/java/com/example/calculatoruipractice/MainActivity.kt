package com.example.calculatoruipractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calculatoruipractice.ui.theme.CalculatorUIPracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorUIPracticeTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    CalculatorV1()
                }
            }
        }
    }
}

@Composable
fun CalculatorV1() {
    // интерфейс калькулятора
}

