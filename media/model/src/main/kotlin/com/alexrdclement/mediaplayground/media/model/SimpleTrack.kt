package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlin.time.Duration
import kotlin.time.Duration.Companion.microseconds

data class SimpleTrack(
    val id: TrackId,
    val name: String,
    val artists: PersistentList<Artist>,
    val trackNumber: Int?,
    val clips: PersistentSet<TrackClip>,
) {
    val uri: String?
        get() = clips.firstOrNull()?.clip?.mediaAsset?.uri

    val duration: Duration
        get() = clips.firstOrNull()?.clip?.duration ?: 0.microseconds

    val source: Source?
        get() = clips.firstOrNull()?.clip?.source

    val isPlayable: Boolean
        get() = clips.isNotEmpty()
}
