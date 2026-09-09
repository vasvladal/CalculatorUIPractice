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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculatoruipractice.data.PreferencesManager
import com.example.calculatoruipractice.ui.screens.about.AboutScreen
import com.example.calculatoruipractice.ui.screens.calculator.CalculatorScreen
import com.example.calculatoruipractice.ui.screens.guide.GuideScreen
import com.example.calculatoruipractice.ui.screens.settings.SettingsScreen
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
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "calculator"
                    ) {
                        composable("calculator") {
                            CalculatorScreen(
                                isDarkTheme = isDarkTheme,
                                onToggleTheme = onToggleTheme,
                                onOpenSettings = { navController.navigate("settings") }
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                isDarkTheme = isDarkTheme,
                                onToggleTheme = onToggleTheme,
                                onOpenAbout = { navController.navigate("about") },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("about") {
                            AboutScreen(
                                onBack = { navController.popBackStack() },
                                onOpenGuide = { navController.navigate("guide") }
                            )
                        }
                        composable("guide") {
                            GuideScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}