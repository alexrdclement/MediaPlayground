package com.alexrdclement.mediaplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.navigation.MediaPlaygroundNavHost
import com.alexrdclement.ui.theme.MediaPlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MediaPlaygroundTheme {
                val mediaItem by viewModel.loadedMediaItem.collectAsStateWithLifecycle()
                val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()

                Scaffold(
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                ) { contentPadding ->
                    MediaPlaygroundNavHost(
                        contentPadding = contentPadding,
                        currentMediaItem = mediaItem,
                        isPlaying = isPlaying,
                        onPlayPauseClick = viewModel::onPlayPauseClick,
                    )
                }
            }
        }
    }
}
