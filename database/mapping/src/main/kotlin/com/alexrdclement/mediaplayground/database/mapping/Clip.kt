package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.media.model.AudioClip as DomainAudioClip
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.TimeUnit
import com.alexrdclement.mediaplayground.media.model.AudioAsset as DomainAudioAsset
import com.alexrdclement.mediaplayground.media.model.TrackClip
import com.alexrdclement.mediaplayground.media.model.TrackClipId
import com.alexrdclement.mediaplayground.database.model.AudioClip as AudioClipEntity
import com.alexrdclement.mediaplayground.database.model.Clip as ClipEntity
import com.alexrdclement.mediaplayground.database.model.CompleteAudioAsset
import com.alexrdclement.mediaplayground.database.model.CompleteAudioClip as CompleteAudioClipEntity
import com.alexrdclement.mediaplayground.database.model.CompleteTrackClip as CompleteTrackClipEntity

fun DomainAudioClip.toClipEntity(): ClipEntity {
    return ClipEntity(
        id = id.value,
        title = title,
        assetId = mediaAsset.id.value,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
    )
}

fun DomainAudioClip.toAudioClipEntity(): AudioClipEntity {
    return AudioClipEntity(
        id = id.value,
        startSampleInAsset = assetOffset.samples,
        durationSamples = duration.samples,
    )
}

fun ClipEntity.toAudioClip(audioClipEntity: AudioClipEntity, audioAsset: DomainAudioAsset): DomainAudioClip {
    val sampleRate = audioAsset.metadata.sampleRate
    return DomainAudioClip(
        id = ClipId(id),
        title = title,
        mediaAsset = audioAsset,
        assetOffset = TimeUnit.Samples(audioClipEntity.startSampleInAsset, sampleRate),
        duration = TimeUnit.Samples(audioClipEntity.durationSamples, sampleRate),
        createdAt = createdAt,
        modifiedAt = modifiedAt,
    )
}

fun CompleteAudioClipEntity.toAudioClip(): DomainAudioClip =
    clip.toAudioClip(audioClip, toAudioAsset())

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
        clip = completeAudioClip.clip.toAudioClip(completeAudioClip.audioClip, audioAsset),
        trackOffset = TimeUnit.Samples(trackClipCrossRef.startSampleInTrack, sampleRate),
        createdAt = trackClipCrossRef.createdAt,
        modifiedAt = trackClipCrossRef.modifiedAt,
    )
}
