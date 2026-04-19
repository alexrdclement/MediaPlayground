package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.TimeUnit
import com.alexrdclement.mediaplayground.media.model.TrackClip
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.toPersistentSet
import java.util.UUID

internal fun makeTrack(
    id: UUID,
    trackClips: PersistentSet<TrackClip<TimeUnit.Samples>>,
    mediaMetadata: MediaMetadata.Audio,
    artists: PersistentList<Artist>,
    simpleAlbum: SimpleAlbum,
): AudioTrack {
    val title = mediaMetadata.title ?: "Unknown track"

    return AudioTrack(
        id = TrackId(id.toString()),
        title = title,
        artists = artists,
        trackNumber = mediaMetadata.trackNumber,
        clips = trackClips.toPersistentSet(),
        simpleAlbum = simpleAlbum,
        notes = null,
    )
}
