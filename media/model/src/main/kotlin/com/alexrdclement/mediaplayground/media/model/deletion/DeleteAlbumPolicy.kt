package com.alexrdclement.mediaplayground.media.model.deletion

data class DeleteAlbumPolicy(
    val deleteOrphanedTracks: Boolean = true,
    val trackPolicy: DeleteTrackPolicy = DeleteTrackPolicy(),
)
