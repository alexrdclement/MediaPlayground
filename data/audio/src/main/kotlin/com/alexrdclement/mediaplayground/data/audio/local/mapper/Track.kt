package com.alexrdclement.mediaplayground.data.audio.local.mapper

import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlinx.datetime.Clock
import com.alexrdclement.mediaplayground.database.model.Track as TrackEntity

fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
        id = id.value,
        title = title,
        durationMs = durationMs,
        trackNumber = trackNumber,
        uri = uri,
        modifiedDate = Clock.System.now(),
    )
}

fun TrackEntity.toTrack(): Track {
    return Track(
        id = TrackId(id),
        title = title,
        artists = emptyList(),
        durationMs = durationMs,
        trackNumber = trackNumber,
        uri = uri,
        simpleAlbum = SimpleAlbum(
            id = AlbumId(""), // TODO
            name = "",
            artists = emptyList(),
            images = emptyList(),
        ),
    )
}
