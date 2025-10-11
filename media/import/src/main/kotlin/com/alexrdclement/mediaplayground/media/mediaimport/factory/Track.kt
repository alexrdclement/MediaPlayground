package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlinx.io.files.Path
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

internal fun makeTrack(
    id: UUID,
    filePath: Path,
    mediaMetadata: MediaMetadata,
    simpleArtist: SimpleArtist,
    simpleAlbum: SimpleAlbum,
): Track {
    return Track(
        id = TrackId(id.toString()),
        title = mediaMetadata.title ?: filePath.name,
        artists = listOf(simpleArtist),
        duration = mediaMetadata.durationMs?.milliseconds ?: 0.milliseconds,
        trackNumber = mediaMetadata.trackNumber,
        uri = filePath.toString(),
        simpleAlbum = simpleAlbum,
    )
}
