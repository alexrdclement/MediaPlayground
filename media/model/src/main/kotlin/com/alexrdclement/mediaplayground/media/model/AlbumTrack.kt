package com.alexrdclement.mediaplayground.media.model

data class AlbumTrack(
    val track: AudioTrack,
    val albumId: AudioAlbumId,
    val trackNumber: Int?,
) : AudioItem by track
