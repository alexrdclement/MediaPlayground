package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.database.model.AudioFile
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.TrackClip
import kotlinx.collections.immutable.PersistentList
import kotlinx.io.files.Path
import com.alexrdclement.mediaplayground.database.model.Clip as ClipEntity
import com.alexrdclement.mediaplayground.database.model.CompleteAudioClip as CompleteAudioClipEntity
import com.alexrdclement.mediaplayground.database.model.CompleteTrackClip as CompleteTrackClipEntity

fun Clip.toClipEntity(): ClipEntity {
    return ClipEntity(
        id = id.value,
        title = title,
        assetId = mediaAsset.id.value,
        startFrameInFile = startFrameInFile,
        endFrameInFile = endFrameInFile,
    )
}

fun ClipEntity.toClip(audioFile: AudioFile): Clip = toClip(audioFile.toMediaAsset())

fun ClipEntity.toClip(mediaAsset: MediaAsset): Clip {
    return Clip(
        id = ClipId(id),
        title = title,
        mediaAsset = mediaAsset,
        startFrameInFile = startFrameInFile,
        endFrameInFile = endFrameInFile,
    )
}

fun CompleteAudioClipEntity.toClip(): Clip = clip.toClip(audioFile.toMediaAsset())

fun CompleteAudioClipEntity.toClip(
    albumDir: Path,
    artists: PersistentList<Artist>,
    images: PersistentList<Image>,
): Clip = clip.toClip(audioFile.toMediaAsset(albumDir, artists, images))

fun CompleteTrackClipEntity.toTrackClip(audioFile: AudioFile): TrackClip = toTrackClip(audioFile.toMediaAsset())

fun CompleteTrackClipEntity.toTrackClip(
    albumDir: Path,
    artists: PersistentList<Artist>,
    images: PersistentList<Image>,
): TrackClip = toTrackClip(completeAudioClip.audioFile.toMediaAsset(albumDir, artists, images))

private fun CompleteTrackClipEntity.toTrackClip(mediaAsset: MediaAsset): TrackClip {
    return TrackClip(
        clip = completeAudioClip.clip.toClip(mediaAsset),
        startFrameInTrack = trackClipCrossRef.startFrameInTrack,
    )
}
