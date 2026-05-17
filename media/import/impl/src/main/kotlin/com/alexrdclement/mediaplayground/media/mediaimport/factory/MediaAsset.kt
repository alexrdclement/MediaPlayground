package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import kotlinx.collections.immutable.PersistentList
import kotlin.time.Clock
import kotlin.time.Instant

internal fun makeAudioAsset(
    id: AudioAssetId,
    uri: MediaAssetUri,
    originUri: MediaAssetOriginUri,
    mediaMetadata: MediaMetadata.Audio,
    artists: PersistentList<Artist>,
    images: PersistentList<Image>,
    createdAt: Instant = Clock.System.now(),
    modifiedAt: Instant = createdAt,
): AudioAsset {
    return AudioAsset(
        id = id,
        uri = uri,
        originUri = originUri,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
        artists = artists,
        images = images,
        metadata = mediaMetadata,
    )
}
