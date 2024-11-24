package com.alexrdclement.mediaplayground.data.audio.local.mapper

import com.alexrdclement.mediaplayground.model.audio.Image
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
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

fun CompleteTrackEntity.toTrack(): Track {
    val simpleArtists = emptyList<SimpleArtist>()// album.map { it.toSimpleArtist() }
    val images = emptyList<Image>()
    return Track(
        id = TrackId(track.id),
        title = track.title,
        artists = simpleArtists,
        durationMs = track.durationMs,
        trackNumber = track.trackNumber,
        uri = track.uri,
        simpleAlbum = album.toSimpleAlbum(artists = simpleArtists, images = images),
    )
}
