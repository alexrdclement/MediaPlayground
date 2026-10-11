package com.alexrdclement.mediaplayground.ui.components.metadata

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.TextFieldStyle
import com.embarrasdf.palette.components.core.TextStyle

/**
 * Shared by the metadata screens' content, which are the same scrolling list of labelled fields.
 * Lives here rather than in each feature so the four stay in step.
 */
data class MetadataContentStyle(
    val contentPadding: PaddingValues = PaddingValues(16.dp),
    val contentSpacing: Dp = 16.dp,
    val sectionSpacing: Dp = 8.dp,
    val labelStyle: TextStyle = TextStyle(),
    val textFieldStyle: TextFieldStyle = TextFieldStyle(),
)
