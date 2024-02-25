package com.alexrdclement.mediaplayground.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.ui.tv.theme.MediaPlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MediaPlaygroundTheme {
                val savedTracks by viewModel.savedTracks.collectAsStateWithLifecycle()
                MainScreen(
                    savedTracks = savedTracks,
                    onLoginClick = viewModel::onLoginClick,
                    onLoadClick = viewModel::loadData
                )
            }
        }
    }
}
