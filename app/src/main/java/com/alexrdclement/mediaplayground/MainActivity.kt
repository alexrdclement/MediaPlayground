package com.alexrdclement.mediaplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alexrdclement.mediaplayground.navigation.MediaPlaygroundNavHost
import com.alexrdclement.ui.theme.MediaPlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MediaPlaygroundTheme {
                MediaPlaygroundNavHost()
            }
        }
    }
}
