package com.alexrdclement.mediaplayground.data.audio.local.mapper

import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlinx.io.files.Path
import kotlin.time.Clock
import com.alexrdclement.mediaplayground.database.model.CompleteTrack as CompleteTrackEntity
import com.alexrdclement.mediaplayground.database.model.Track as TrackEntity

fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
        id = id.value,
        fileName = fileNameFromUri(uri),
        title = title,
        albumId = simpleAlbum.id.value,
        durationMs = durationMs,
        trackNumber = trackNumber,
        modifiedDate = Clock.System.now(),
    )
}

fun TrackEntity.toSimpleTrack(
    mediaItemDir: Path,
    simpleArtists: List<SimpleArtist>,
): SimpleTrack {
    return SimpleTrack(
        id = TrackId(id),
        uri = uriFromFileName(mediaItemDir, fileName),
        name = title,
        artists = simpleArtists,
        durationMs = durationMs,
        trackNumber = trackNumber,
    )
}

fun CompleteTrackEntity.toTrack(
    albumDir: Path,
): Track {
    val simpleArtists = artists.map { it.toSimpleArtist() }
    return Track(
        id = TrackId(track.id),
        title = track.title,
        artists = simpleArtists,
        durationMs = track.durationMs,
        trackNumber = track.trackNumber,
        uri = uriFromFileName(albumDir, track.fileName),
        simpleAlbum = SimpleAlbum(
            id = AlbumId(album.id),
            name = album.title,
            artists = simpleArtists,
            images = images.map { it.toImage(albumDir = albumDir) },
        ),
    )
}
