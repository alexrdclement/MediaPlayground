package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.TimeUnit
import com.alexrdclement.mediaplayground.media.model.AudioAsset as DomainAudioAsset
import com.alexrdclement.mediaplayground.media.model.TrackClip
import kotlin.time.Clock
import kotlinx.collections.immutable.PersistentList
import com.alexrdclement.mediaplayground.database.model.Clip as ClipEntity
import com.alexrdclement.mediaplayground.database.model.CompleteAudioAsset
import com.alexrdclement.mediaplayground.database.model.CompleteAudioClip as CompleteAudioClipEntity
import com.alexrdclement.mediaplayground.database.model.CompleteTrackClip as CompleteTrackClipEntity

fun Clip.toClipEntity(): ClipEntity {
    return ClipEntity(
        id = id.value,
        title = title,
        assetId = mediaAsset.id.value,
        startSampleInAsset = when (val s = assetOffset) {
            is TimeUnit.Samples -> s.samples
            is TimeUnit.Frames -> s.frames
        },
        durationSamples = when (val t = duration) {
            is TimeUnit.Samples -> t.samples
            is TimeUnit.Frames -> t.frames
        },
        createdAt = Clock.System.now(),
        modifiedAt = Clock.System.now(),
    )
}

fun ClipEntity.toClip(audioAsset: DomainAudioAsset): Clip {
    val sampleRate = audioAsset.metadata.sampleRate
    return Clip(
        id = ClipId(id),
        title = title,
        mediaAsset = audioAsset,
        assetOffset = TimeUnit.Samples(startSampleInAsset, sampleRate),
        duration = TimeUnit.Samples(durationSamples, sampleRate),
    )
}

fun CompleteAudioClipEntity.toClip(): Clip =
    clip.toClip(toAudioAsset())

fun CompleteAudioClipEntity.toClip(
    artists: PersistentList<Artist>,
    images: PersistentList<Image>,
): Clip = clip.toClip(toAudioAsset(artists, images))

fun CompleteAudioClipEntity.toAudioAsset(): DomainAudioAsset =
    CompleteAudioAsset(audioAsset = audioAsset, mediaAsset = mediaAsset).toAudioAsset()

fun CompleteAudioClipEntity.toAudioAsset(
    artists: PersistentList<Artist>,
    images: PersistentList<Image>,
): DomainAudioAsset =
    CompleteAudioAsset(audioAsset = audioAsset, mediaAsset = mediaAsset).toAudioAsset(artists, images)

fun CompleteTrackClipEntity.toTrackClip(
    artists: PersistentList<Artist>,
    images: PersistentList<Image>,
): TrackClip<TimeUnit.Samples> = toTrackClip(completeAudioClip.toAudioAsset(artists, images))

private fun CompleteTrackClipEntity.toTrackClip(audioAsset: DomainAudioAsset): TrackClip<TimeUnit.Samples> {
    val sampleRate = audioAsset.metadata.sampleRate
    return TrackClip(
        clip = completeAudioClip.clip.toClip(audioAsset),
        trackOffset = TimeUnit.Samples(trackClipCrossRef.startSampleInTrack, sampleRate),
    )
}
