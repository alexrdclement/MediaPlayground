package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList

data class AlbumTrack(
    val track: AudioTrack,
    val albumId: AudioAlbumId,
    val trackNumber: Int?,
    override val artists: PersistentList<Artist>,
) : AudioItem by track
