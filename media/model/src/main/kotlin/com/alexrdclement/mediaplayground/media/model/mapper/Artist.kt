package com.alexrdclement.mediaplayground.media.model.mapper

import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import java.util.UUID
import kotlin.time.Clock

fun MediaMetadata.Audio.toArtist(name: String): Artist = Artist(
    id = ArtistId(UUID.randomUUID().toString()),
    name = name,
    notes = null,
    createdAt = Clock.System.now(),
    modifiedAt = Clock.System.now(),
)
