package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.Source
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlinx.collections.immutable.PersistentList
import kotlinx.io.files.Path
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

internal fun makeTrack(
    id: UUID,
    filePath: Path,
    mediaMetadata: MediaMetadata,
    simpleArtists: PersistentList<SimpleArtist>,
    simpleAlbum: SimpleAlbum,
    source: Source,
): Track {
    return Track(
        id = TrackId(id.toString()),
        title = mediaMetadata.title ?: filePath.name,
        artists = simpleArtists,
        duration = mediaMetadata.durationMs?.milliseconds ?: 0.milliseconds,
        trackNumber = mediaMetadata.trackNumber,
        uri = filePath.toString(),
        simpleAlbum = simpleAlbum,
        source = source,
    )
}
