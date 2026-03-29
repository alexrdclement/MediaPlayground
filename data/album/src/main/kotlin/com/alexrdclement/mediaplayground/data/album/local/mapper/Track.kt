package com.alexrdclement.mediaplayground.data.album.local.mapper

import com.alexrdclement.mediaplayground.data.disk.mapper.uriFromFileName
import com.alexrdclement.mediaplayground.media.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.media.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.media.model.audio.TrackId
import kotlinx.collections.immutable.PersistentList
import kotlinx.io.files.Path
import kotlin.time.Duration.Companion.milliseconds
import com.alexrdclement.mediaplayground.database.model.Track as TrackEntity

fun TrackEntity.toSimpleTrack(
    mediaItemDir: Path,
    simpleArtists: PersistentList<SimpleArtist>,
): SimpleTrack {
    return SimpleTrack(
        id = TrackId(id),
        uri = uriFromFileName(mediaItemDir, fileName),
        name = title,
        artists = simpleArtists,
        duration = durationMs.milliseconds,
        trackNumber = trackNumber,
        source = source.toDomainSource(),
    )
}
