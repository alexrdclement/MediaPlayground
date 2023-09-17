package com.alexrdclement.mediaplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.navigation.MediaPlaygroundNavHost
import com.alexrdclement.ui.components.MediaControlBar
import com.alexrdclement.ui.theme.MediaPlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MediaPlaygroundTheme {
                val mediaItem by viewModel.loadedMediaItem.collectAsStateWithLifecycle()
                val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(
                            visible = mediaItem != null
                        ) {
                            mediaItem?.let {
                                MediaControlBar(
                                    mediaItem = it,
                                    isPlaying = isPlaying,
                                    onClick = {
                                        when(it) {
                                            is Album -> {}
                                            is Track -> {}
                                        }
                                    },
                                    onPlayPauseClick = viewModel::onPlayPauseClick
                                )
                            }
                        }

                    }
                ) { contentPadding ->
                    MediaPlaygroundNavHost(contentPadding = contentPadding)
                }
            }
        }
    }
}
