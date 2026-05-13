package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.model.AlbumTrack
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.TimeUnit
import com.alexrdclement.mediaplayground.media.model.TrackClip
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.toPersistentSet
import java.util.UUID

internal fun makeTrack(
    id: UUID,
    trackClips: PersistentSet<TrackClip<TimeUnit.Samples>>,
    mediaMetadata: MediaMetadata.Audio,
    simpleAlbum: SimpleAlbum,
): AlbumTrack {
    val title = mediaMetadata.title ?: "Unknown track"
    return AlbumTrack(
        track = AudioTrack(
            id = TrackId(id.toString()),
            title = title,
            clips = trackClips.toPersistentSet(),
            notes = null,
        ),
        albumId = AudioAlbumId(simpleAlbum.id.value),
        trackNumber = mediaMetadata.trackNumber,
        artists = simpleAlbum.artists,
    )
}
