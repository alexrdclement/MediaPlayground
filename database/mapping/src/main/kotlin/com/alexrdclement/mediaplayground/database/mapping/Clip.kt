package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.TimeUnit
import com.alexrdclement.mediaplayground.media.model.AudioAsset as DomainAudioAsset
import com.alexrdclement.mediaplayground.media.model.TrackClip
import com.alexrdclement.mediaplayground.media.model.TrackClipId
import com.alexrdclement.mediaplayground.database.model.Clip as ClipEntity
import com.alexrdclement.mediaplayground.database.model.CompleteAudioAsset
import com.alexrdclement.mediaplayground.database.model.CompleteAudioClip as CompleteAudioClipEntity
import com.alexrdclement.mediaplayground.database.model.CompleteTrackClip as CompleteTrackClipEntity

fun Clip.toClipEntity(): ClipEntity {
    return ClipEntity(
        id = id.value,
        title = title,
        assetId = mediaAsset.id.value,
        startSampleInAsset = when (val assetOffset = assetOffset) {
            is TimeUnit.Samples -> assetOffset.samples
            is TimeUnit.Frames -> assetOffset.frames
        },
        durationSamples = when (val duration = duration) {
            is TimeUnit.Samples -> duration.samples
            is TimeUnit.Frames -> duration.frames
        },
        createdAt = createdAt,
        modifiedAt = modifiedAt,
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
        createdAt = createdAt,
        modifiedAt = modifiedAt,
    )
}

fun CompleteAudioClipEntity.toClip(): Clip =
    clip.toClip(toAudioAsset())

fun CompleteAudioClipEntity.toAudioAsset(): DomainAudioAsset =
    CompleteAudioAsset(
        audioAsset = audioAsset,
        mediaAsset = mediaAsset,
        artists = artists,
        images = images,
    ).toAudioAsset()

fun CompleteTrackClipEntity.toTrackClip(): TrackClip<TimeUnit.Samples> =
    toTrackClip(completeAudioClip.toAudioAsset())

private fun CompleteTrackClipEntity.toTrackClip(audioAsset: DomainAudioAsset): TrackClip<TimeUnit.Samples> {
    val sampleRate = audioAsset.metadata.sampleRate
    return TrackClip(
        id = TrackClipId(trackClipCrossRef.id),
        clip = completeAudioClip.clip.toClip(audioAsset),
        trackOffset = TimeUnit.Samples(trackClipCrossRef.startSampleInTrack, sampleRate),
        createdAt = trackClipCrossRef.createdAt,
        modifiedAt = trackClipCrossRef.modifiedAt,
    )
}
