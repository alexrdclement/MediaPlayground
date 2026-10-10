package com.alexrdclement.mediaplayground.feature.audio.library.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.ButtonDefaults
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle

data class AudioLibraryContentStyle(
    val contentPadding: PaddingValues = PaddingValues(vertical = 8.dp),
    val contentSpacing: Dp = 8.dp,
    val headerPadding: PaddingValues = PaddingValues(0.dp),
    val headerMinHeight: Dp = ButtonDefaults.MinHeight,
    val headerTextStyle: TextStyle = TextStyle(),
)

@Composable
fun AudioLibraryContent(
    headerText: String,
    style: AudioLibraryContentStyle = AudioLibraryContentStyle(),
    headerAction: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(style.contentSpacing),
        modifier = Modifier
            .fillMaxWidth()
            .padding(style.contentPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(minHeight = style.headerMinHeight)
                .padding(style.headerPadding)
        ) {
            Text(
                text = headerText,
                style = style.headerTextStyle,
            )
            headerAction?.invoke()
        }
        content()
    }
}
