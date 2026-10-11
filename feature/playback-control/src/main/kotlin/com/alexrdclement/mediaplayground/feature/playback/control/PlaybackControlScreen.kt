package com.alexrdclement.mediaplayground.feature.playback.control

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.embarrasdf.palette.components.layout.dialog.DialogContent
import com.embarrasdf.palette.theme.PaletteTheme
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun PlaybackControlScreen(viewModel: PlaybackControlViewModel = metroViewModel()) {
    val rateControl by viewModel.rateControl.collectAsStateWithLifecycle()
    val pitchControl by viewModel.pitchControl.collectAsStateWithLifecycle()

    PlaybackControlDialogContent(
        rateControl = rateControl,
        pitchControl = pitchControl,
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
    onRateDecrease: () -> Unit,
    onRateIncrease: () -> Unit,
    onPitchDecrease: () -> Unit,
    onPitchIncrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DialogContent(
        title = "Playback",
        modifier = modifier,
        style = PaletteTheme.component.layout.dialogContent,
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
