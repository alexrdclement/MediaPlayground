package com.alexrdclement.mediaplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.alexrdclement.mediaplayground.navigation.MediaPlaygroundNavHost
import com.alexrdclement.uiplayground.theme.PlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            PlaygroundTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                ) { contentPadding ->
                    MediaPlaygroundNavHost(
                        contentPadding = contentPadding,
                    )
                }
            }
        }
    }
}
