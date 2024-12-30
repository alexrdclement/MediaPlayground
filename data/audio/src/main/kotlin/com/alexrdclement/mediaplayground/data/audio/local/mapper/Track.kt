package com.alexrdclement.mediaplayground.data.audio.local.mapper

import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Image
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlinx.datetime.Clock
import com.alexrdclement.mediaplayground.database.model.CompleteTrack as CompleteTrackEntity
import com.alexrdclement.mediaplayground.database.model.Track as TrackEntity

fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
        id = id.value,
        title = title,
        albumId = simpleAlbum.id.value,
        durationMs = durationMs,
        trackNumber = trackNumber,
        uri = uri,
        modifiedDate = Clock.System.now(),
    )
}

fun TrackEntity.toSimpleTrack(
    simpleArtists: List<SimpleArtist>,
): SimpleTrack {
    return SimpleTrack(
        id = TrackId(id),
        name = title,
        artists = simpleArtists,
        durationMs = durationMs,
        trackNumber = trackNumber,
        uri = uri,
    )
}

fun CompleteTrackEntity.toTrack(): Track {
    val simpleArtists = artists.map { it.toSimpleArtist() }
    return Track(
        id = TrackId(track.id),
        title = track.title,
        artists = simpleArtists,
        durationMs = track.durationMs,
        trackNumber = track.trackNumber,
        uri = track.uri,
        simpleAlbum = SimpleAlbum(
            id = AlbumId(album.id),
            name = album.title,
            artists = simpleArtists,
            images = images.map { it.toImage() },
        ),
    )
}
