package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@JvmInline
@Serializable
value class AudioAssetId(override val value: String) : MediaAssetId

@Serializable
data class AudioAsset(
    override val id: AudioAssetId,
    override val uri: MediaAssetUri,
    override val originUri: MediaAssetOriginUri,
    override val createdAt: Instant,
    override val modifiedAt: Instant,
    val artists: PersistentList<Artist>,
    override val images: PersistentList<Image>,
    val metadata: MediaMetadata.Audio,
) : MediaAsset {
    override val title: String get() = metadata.title ?: uri.toUriString().substringAfterLast('/')
    override val isPlayable: Boolean get() = true
    override val duration: TimeUnit get() = metadata.durationUs?.let { durationUs ->
        TimeUnit.Samples(durationUs * metadata.sampleRate / 1_000_000L, metadata.sampleRate)
    } ?: TimeUnit.Samples(0L, metadata.sampleRate)
}
