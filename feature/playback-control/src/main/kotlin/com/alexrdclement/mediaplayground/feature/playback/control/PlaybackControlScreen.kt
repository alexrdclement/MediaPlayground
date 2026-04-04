package com.alexrdclement.mediaplayground.feature.playback.control

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.palette.components.core.Button
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.components.layout.dialog.DialogContent
import com.alexrdclement.palette.theme.PaletteTheme
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

@Composable
private fun NudgeControlRow(
    label: String,
    control: NudgeControl,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text = label)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small),
        ) {
            Button(
                onClick = onDecrease,
                enabled = control.decrementEnabled,
            ) {
                Text("-")
            }
            Text("%.1f".format(control.value))
            Button(
                onClick = onIncrease,
                enabled = control.incrementEnabled,
            ) {
                Text("+")
            }
        }
    }
}
