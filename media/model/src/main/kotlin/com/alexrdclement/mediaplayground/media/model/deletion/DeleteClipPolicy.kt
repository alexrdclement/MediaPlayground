package com.alexrdclement.mediaplayground.media.model.deletion

data class DeleteClipPolicy(
    val deleteOrphanedAssets: Boolean = true,
)
