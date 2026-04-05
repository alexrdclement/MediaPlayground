package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.metadata.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.SimpleArtist
import com.alexrdclement.mediaplayground.media.model.Source
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.collections.immutable.PersistentList
import kotlinx.io.files.Path
import java.util.UUID
import kotlin.time.Duration.Companion.microseconds

internal fun makeTrack(
    id: UUID,
    filePath: Path,
    mediaMetadata: MediaMetadata.Audio,
    simpleArtists: PersistentList<SimpleArtist>,
    simpleAlbum: SimpleAlbum,
    source: Source,
): Track {
    return Track(
        id = TrackId(id.toString()),
        title = mediaMetadata.title ?: filePath.name,
        artists = simpleArtists,
        duration = mediaMetadata.durationUs?.microseconds ?: 0.microseconds,
        trackNumber = mediaMetadata.trackNumber,
        uri = filePath.toString(),
        simpleAlbum = simpleAlbum,
        source = source,
        notes = null,
    )
}
