package com.alexrdclement.mediaplayground.media.model.deletion

data class DeleteTrackPolicy(
    val deleteOrphanedClips: Boolean = true,
    val clipPolicy: DeleteClipPolicy = DeleteClipPolicy(),
)
