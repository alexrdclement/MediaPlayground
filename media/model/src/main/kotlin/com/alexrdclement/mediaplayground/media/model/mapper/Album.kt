package com.alexrdclement.mediaplayground.media.model.mapper

import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import kotlinx.collections.immutable.PersistentList
import kotlin.time.Instant

fun SimpleAlbum.toAlbum(
    tracks: PersistentList<AudioTrack>,
    createdAt: Instant = Instant.DISTANT_PAST,
    modifiedAt: Instant = Instant.DISTANT_PAST,
) = AudioAlbum(
    id = AudioAlbumId(id.value),
    title = name,
    artists = artists,
    images = images,
    items = tracks,
    notes = null,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
)

fun AudioAlbum.toSimpleAlbum() = SimpleAlbum(
    id = id,
    name = title,
    artists = artists,
    images = images,
)
