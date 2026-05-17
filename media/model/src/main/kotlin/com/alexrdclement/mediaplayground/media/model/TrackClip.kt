package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@JvmInline
@Serializable
value class TrackClipId(override val value: String) : MediaItemId

@Serializable
data class TrackClip<T : TimeUnit>(
    override val id: TrackClipId,
    val clip: AudioClip,
    val trackOffset: T,
    override val createdAt: Instant,
    override val modifiedAt: Instant,
) : MediaItem {
    override val title: String get() = clip.title
    override val images: PersistentList<Image> get() = clip.images
    override val isPlayable: Boolean get() = clip.isPlayable
    override val duration: TimeUnit get() = clip.duration
}

fun <T : TimeUnit> Iterable<TrackClip<T>>.duration(): TimeUnit =
    maxByOrNull { it.trackOffset.toKotlinDuration() }
        ?.let { it.trackOffset + it.clip.duration }
        ?: TimeUnit.Samples(0L, DEFAULT_SAMPLE_RATE)
