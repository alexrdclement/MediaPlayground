package com.alexrdclement.mediaplayground.feature.playback.control

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alexrdclement.palette.components.core.Button
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.components.core.copy
import com.alexrdclement.palette.theme.PaletteTheme

interface NudgeControl {
    val value: Float
    val incrementEnabled: Boolean
    val decrementEnabled: Boolean
}

@Composable
fun NudgeControlRow(
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
        Text(text = label, style = PaletteTheme.component.core.text.bodyMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(PaletteTheme.semantic.dimension.spacing.small),
        ) {
            Button(
                onClick = onDecrease,
                enabled = control.decrementEnabled,
                style = PaletteTheme.component.core.button.primary,
            ) {
                Text("-", style = PaletteTheme.component.core.text.bodyMedium.copy(color = PaletteTheme.semantic.color.onPrimary))
            }
            Text("%.1f".format(control.value), style = PaletteTheme.component.core.text.bodyMedium)
            Button(
                onClick = onIncrease,
                enabled = control.incrementEnabled,
                style = PaletteTheme.component.core.button.primary,
            ) {
                Text("+", style = PaletteTheme.component.core.text.bodyMedium.copy(color = PaletteTheme.semantic.color.onPrimary))
            }
        }
    }
}
