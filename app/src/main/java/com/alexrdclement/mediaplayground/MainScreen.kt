package com.alexrdclement.mediaplayground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.alexrdclement.ui.components.MediaPickerButton
import com.alexrdclement.ui.components.MediaPlayer
import com.alexrdclement.ui.components.PlayerControls

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    player: Player?,
    isPlaying: Boolean,
    onLogInClick: () -> Unit,
    onPickMediaClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            TopAppBar(
                title = {},
                actions = {
                    Button(
                        onClick = onLogInClick,
                    ) {
                        Text("Log In")
                    }
                }
            )
            if (player != null) {
                MediaPlayer(
                    player = player,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            ) {
                MediaPickerButton(onClick = onPickMediaClick)
                PlayerControls(
                    isPlaying = isPlaying,
                    onPlayPauseClicked = onPlayPauseClick,
                )
            }
        }
    }
}
