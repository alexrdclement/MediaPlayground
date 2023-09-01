package com.alexrdclement.mediaplaygroundtv

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.ui.tv.components.TracksRow

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MainScreen(
    savedTracks: List<Track>,
    onLoginClick: () -> Unit,
    onLoadClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RectangleShape
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize().align(Alignment.Center)
        ) {
            Button(
                onClick = onLoginClick
            ) {
                Text("Login")
            }
            Button(
                onClick = onLoadClick
            ) {
                Text("Load")
            }
            TracksRow(
                tracks = savedTracks,
                onTrackClick = { track ->

                }
            )
        }
    }
}
