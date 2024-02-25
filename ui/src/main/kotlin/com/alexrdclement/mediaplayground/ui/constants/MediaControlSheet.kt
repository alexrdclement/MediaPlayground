package com.alexrdclement.mediaplayground.ui.constants

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val MediaControlSheetPartialExpandHeight = 64.dp

fun Modifier.mediaControlSheetPadding(isMediaItemLoaded: Boolean) = this.then(
    if (isMediaItemLoaded) {
        Modifier.padding(bottom = MediaControlSheetPartialExpandHeight)
    } else {
        Modifier
    }
)
