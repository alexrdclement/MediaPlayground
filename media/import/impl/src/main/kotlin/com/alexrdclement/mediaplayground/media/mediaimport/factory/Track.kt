package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.Source
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackClip
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.io.files.Path
import java.util.UUID

internal fun makeTrack(
    id: UUID,
    filePath: Path,
    mediaMetadata: MediaMetadata.Audio,
    artists: PersistentList<Artist>,
    simpleAlbum: SimpleAlbum,
    source: Source,
): Track {
    val title = mediaMetadata.title ?: filePath.name
    val clip = makeClip(
        filePath = filePath,
        mediaMetadata = mediaMetadata,
        source = source,
        artists = artists,
        images = simpleAlbum.images,
    )
    val trackClip = TrackClip(clip = clip, startFrameInTrack = 0L)

    return Track(
        id = TrackId(id.toString()),
        title = title,
        artists = artists,
        trackNumber = mediaMetadata.trackNumber,
        clips = persistentSetOf(trackClip),
        simpleAlbum = simpleAlbum,
        notes = null,
    )
}
