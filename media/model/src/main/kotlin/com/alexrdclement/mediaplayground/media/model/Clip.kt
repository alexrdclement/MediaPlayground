package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@JvmInline
@Serializable
value class ClipId(override val value: String) : MediaItemId

@Serializable
data class Clip(
    override val id: ClipId,
    override val title: String,
    val mediaAsset: MediaAsset,
    val startFrameInFile: Long,
    val endFrameInFile: Long,
) : MediaItem {
    override val artists: PersistentList<Artist> = mediaAsset.artists

    override val images: PersistentList<Image> = mediaAsset.images

    override val isPlayable: Boolean = true

    override val duration: Duration = when (val metadata = mediaAsset.metadata) {
        is MediaMetadata.Audio -> {
            ((endFrameInFile - startFrameInFile).toDouble() / metadata.sampleRate).seconds
        }
        is MediaMetadata.Image -> 0.seconds
    }

    override val source: Source = mediaAsset.source

    val durationFrames: Long = endFrameInFile - startFrameInFile
}
