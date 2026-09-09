package com.example.calculatoruipractice.ui.screens.guide

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.calculatoruipractice.ui.screens.settings.SettingsTopBar
import com.example.calculatoruipractice.ui.theme.CalculatorTheme

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GuideScreen(onBack: () -> Unit) {
    val colors = CalculatorTheme.colors
    var webView by remember { mutableStateOf<WebView?>(null) }

    // Системная кнопка «Назад»: сначала назад по истории WebView, потом выход
    BackHandler {
        if (webView?.canGoBack() == true) webView?.goBack() else onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        SettingsTopBar(
            title = "📖 Руководство пользователя",
            onBack = {
                if (webView?.canGoBack() == true) webView?.goBack() else onBack()
            },
            colors = colors
        )
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true   // нужен для тренажёра ctg/sec/csc и темы страницы
                    settings.domStorageEnabled = true
                    settings.allowFileAccess = true
                    settings.useWideViewPort = true
                    settings.loadWithOverviewMode = true
                    webViewClient = WebViewClient()
                    loadUrl("file:///android_asset/user_guide.html")
                }
            },
            update = { webView = it }
        )
    }
}