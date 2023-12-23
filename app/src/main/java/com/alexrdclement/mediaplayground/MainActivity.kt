package com.alexrdclement.mediaplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import com.alexrdclement.mediaplayground.navigation.MediaPlaygroundNavHost
import com.alexrdclement.ui.theme.MediaPlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MediaPlaygroundTheme {
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
