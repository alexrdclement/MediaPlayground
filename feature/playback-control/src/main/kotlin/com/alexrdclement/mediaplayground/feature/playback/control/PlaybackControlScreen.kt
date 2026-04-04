package com.alexrdclement.mediaplayground.feature.playback.control

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.palette.components.layout.dialog.DialogContent
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun PlaybackControlScreen(
    onDismissRequest: () -> Unit,
) {
    val viewModel = metroViewModel<PlaybackControlViewModel>()
    val rateControl by viewModel.rateControl.collectAsStateWithLifecycle()
    val pitchControl by viewModel.pitchControl.collectAsStateWithLifecycle()

    PlaybackControlDialogContent(
        rateControl = rateControl,
        pitchControl = pitchControl,
        onDismissRequest = onDismissRequest,
        onRateDecrease = viewModel::onRateDecrease,
        onRateIncrease = viewModel::onRateIncrease,
        onPitchDecrease = viewModel::onPitchDecrease,
        onPitchIncrease = viewModel::onPitchIncrease,
    )
}

@Composable
fun PlaybackControlDialogContent(
    rateControl: PlaybackRateControl,
    pitchControl: PlaybackPitchControl,
    onDismissRequest: () -> Unit,
    onRateDecrease: () -> Unit,
    onRateIncrease: () -> Unit,
    onPitchDecrease: () -> Unit,
    onPitchIncrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DialogContent(
        title = "Playback",
        modifier = modifier,
    ) {
        NudgeControlRow(
            label = "Speed",
            control = rateControl,
            onDecrease = onRateDecrease,
            onIncrease = onRateIncrease,
        )
        NudgeControlRow(
            label = "Pitch",
            control = pitchControl,
            onDecrease = onPitchDecrease,
            onIncrease = onPitchIncrease,
        )
    }
}
