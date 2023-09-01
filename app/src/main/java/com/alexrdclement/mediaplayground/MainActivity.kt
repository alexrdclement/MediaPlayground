@file:OptIn(ExperimentalMaterial3Api::class)

package com.alexrdclement.mediaplayground

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyLoginActivity
import com.alexrdclement.ui.components.MediaSourcePickerBottomSheet
import com.alexrdclement.ui.components.MediaSource
import com.alexrdclement.ui.theme.MediaPlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mediaPicker = registerForActivityResult(ActivityResultContracts.GetContent()) {
            viewModel.onMediaItemSelected(it)
        }
        setContent {
            MediaPlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val player by viewModel.player.collectAsStateWithLifecycle()
                    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
                    val bottomSheet by viewModel.bottomSheet.collectAsStateWithLifecycle()
                    MainScreen(
                        player = player,
                        isPlaying = isPlaying,
                        onLogInClick = {
                            val intent = Intent(this, SpotifyLoginActivity::class.java)
                            this.startActivity(intent)
                        },
                        onPickMediaClick = viewModel::onPickMediaClick,
                        onPlayPauseClick = viewModel::onPlayPauseClick,
                    )

                    when (bottomSheet) {
                        MainBottomSheet.MediaSourceChooserBottomSheet -> {
                            MediaSourcePickerBottomSheet(
                                onDismissRequest = viewModel::onPickMediaBottomSheetDismiss,
                                onMediaSourcePicked = {
                                    // TODO: route event through view model
                                    when (it) {
                                        MediaSource.DeviceAudio -> mediaPicker.launch("audio/*")
                                        MediaSource.DeviceVideo -> mediaPicker.launch("video/*")
                                        MediaSource.Spotify -> viewModel.onSpotifyMediaSourceSelected()
                                    }
                                    viewModel.onPickMediaBottomSheetDismiss()
                                }
                            )
                        }

                        null -> {}
                    }
                }
            }
        }
    }
}
