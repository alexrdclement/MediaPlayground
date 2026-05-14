package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioClip
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.TimeUnit
import kotlinx.io.files.Path
import java.util.UUID
import kotlin.time.Clock

internal fun makeClip(
    filePath: Path,
    mediaMetadata: MediaMetadata.Audio,
    audioFile: AudioAsset,
): AudioClip {
    val durationUs = mediaMetadata.durationUs ?: 0L
    val sampleRate = mediaMetadata.sampleRate
    val totalSamples = if (durationUs > 0L && sampleRate > 0) {
        durationUs * sampleRate / 1_000_000L
    } else {
        0L
    }
    val title = mediaMetadata.title ?: filePath.name
    val now = Clock.System.now()
    return AudioClip(
        id = ClipId(UUID.randomUUID().toString()),
        title = title,
        mediaAsset = audioFile,
        assetOffset = TimeUnit.Samples(0L, sampleRate),
        duration = TimeUnit.Samples(totalSamples, sampleRate),
        createdAt = now,
        modifiedAt = now,
    )
}
