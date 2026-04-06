package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.Source
import kotlinx.collections.immutable.PersistentList
import kotlinx.io.files.Path
import java.util.UUID

internal fun makeClip(
    filePath: Path,
    mediaMetadata: MediaMetadata.Audio,
    source: Source,
    artists: PersistentList<Artist>,
    images: PersistentList<Image>,
): Clip {
    val durationUs = mediaMetadata.durationUs ?: 0L
    val sampleRate = mediaMetadata.sampleRate ?: 0
    val totalFrames = if (durationUs > 0L && sampleRate > 0) {
        durationUs * sampleRate / 1_000_000L
    } else {
        0L
    }

    val mediaAsset = makeMediaAsset(
        filePath = filePath,
        mediaMetadata = mediaMetadata,
        source = source,
        artists = artists,
        images = images,
    )

    val title = mediaMetadata.title ?: filePath.name
    return Clip(
        id = ClipId(UUID.randomUUID().toString()),
        title = title,
        mediaAsset = mediaAsset,
        startFrameInFile = 0L,
        endFrameInFile = totalFrames,
    )
}
