package com.alexrdclement.mediaplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.ui.components.MediaTypePickerBottomSheet
import com.alexrdclement.mediaplayground.ui.theme.MediaPlaygroundTheme
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
                        onPickMediaClick = viewModel::onPickMediaClick,
                        onPlayPauseClick = viewModel::onPlayPauseClick,
                    )

                    when (bottomSheet) {
                        MainBottomSheet.MediaTypeChooserBottomSheet -> {
                            MediaTypePickerBottomSheet(
                                onDismissRequest = viewModel::onPickMediaBottomSheetDismiss,
                                onMediaTypePicked = {
                                    // TODO: route event through view model
                                    when (it) {
                                        PickMediaType.Audio -> mediaPicker.launch("audio/*")
                                        PickMediaType.Video -> mediaPicker.launch("video/*")
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
