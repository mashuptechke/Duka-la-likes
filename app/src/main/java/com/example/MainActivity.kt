package com.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        AppScreen()
      }
    }
  }
}

@Composable
fun AppScreen() {
  var showSplash by remember { mutableStateOf(true) }

  LaunchedEffect(Unit) {
    delay(2500)
    showSplash = false
  }

  Box(modifier = Modifier.fillMaxSize()) {
    AnimatedVisibility(
      visible = !showSplash,
      enter = fadeIn(animationSpec = tween(500)),
      exit = fadeOut()
    ) {
      MainWrapper()
    }

    AnimatedVisibility(
      visible = showSplash,
      enter = fadeIn(),
      exit = fadeOut(animationSpec = tween(500))
    ) {
      SplashScreen()
    }
  }
}

@Composable
fun SplashScreen() {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFF232526)), // Dark modern background
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Image(
        painter = painterResource(id = R.drawable.app_icon_fg),
        contentDescription = "App Logo",
        modifier = Modifier.size(120.dp)
      )
      Spacer(modifier = Modifier.height(24.dp))
      Text(
        text = "Duka La Likes",
        color = Color.White,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )
    }
  }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MainWrapper() {
  var isLoading by remember { mutableStateOf(true) }

  Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
          WebView(context).apply {
            settings.apply {
              javaScriptEnabled = true
              domStorageEnabled = true
              loadWithOverviewMode = true
              useWideViewPort = true
              mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
              setSupportZoom(true)
              builtInZoomControls = true
              displayZoomControls = false
              
              // Remove '; wv' from the user agent to allow Google Login
              userAgentString = userAgentString.replace("; wv", "")
            }
            webViewClient = object : WebViewClient() {
              override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                isLoading = false
              }
            }
            webChromeClient = WebChromeClient()
            loadUrl("https://dukalalikes.co.ke")
          }
        },
        update = { webView ->
          // Web view updates handled internally.
        }
      )

      if (isLoading) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
          contentAlignment = Alignment.Center
        ) {
          CircularProgressIndicator()
        }
      }
    }
  }
}
