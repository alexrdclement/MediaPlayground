package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.model.AlbumTrack
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.TimeUnit
import com.alexrdclement.mediaplayground.media.model.TrackClip
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.alexrdclement.mediaplayground.media.model.toKotlinDuration
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.toPersistentList
import java.util.UUID
import kotlin.time.Clock

internal fun makeTrack(
    id: UUID,
    trackClips: PersistentSet<TrackClip<TimeUnit.Samples>>,
    mediaMetadata: MediaMetadata.Audio,
    simpleAlbum: SimpleAlbum,
): AlbumTrack {
    val title = mediaMetadata.title ?: "Unknown track"
    val now = Clock.System.now()
    return AlbumTrack(
        track = AudioTrack(
            id = TrackId(id.toString()),
            title = title,
            items = trackClips.sortedBy { it.trackOffset.toKotlinDuration() }.toPersistentList(),
            notes = null,
            createdAt = now,
            modifiedAt = now,
        ),
        albumId = AudioAlbumId(simpleAlbum.id.value),
        trackNumber = mediaMetadata.trackNumber,
    )
}
