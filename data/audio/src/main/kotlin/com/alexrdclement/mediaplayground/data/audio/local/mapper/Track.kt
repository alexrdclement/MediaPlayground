package com.alexrdclement.mediaplayground.data.audio.local.mapper

import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Image
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
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
    val simpleArtist = SimpleArtist(id = artistId, name = artistName)
    val images = listOf(Image(uri = imageUri))
    val simpleAlbum = SimpleAlbum(
        id = AlbumId(albumId),
        name = albumTitle,
        artists = listOf(simpleArtist),
        images = images,
    )
    return Track(
        id = TrackId(id),
        title = title,
        artists = listOf(simpleArtist),
        durationMs = durationMs,
        trackNumber = trackNumber,
        uri = uri,
        simpleAlbum = simpleAlbum,
    )
}
