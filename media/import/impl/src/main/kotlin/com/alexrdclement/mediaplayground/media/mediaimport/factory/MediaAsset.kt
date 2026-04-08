package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.Source
import kotlinx.collections.immutable.PersistentList
import java.util.UUID

internal fun makeMediaAsset(
    uri: String,
    mediaMetadata: MediaMetadata.Audio,
    source: Source,
    artists: PersistentList<Artist>,
    images: PersistentList<Image>,
): MediaAsset {
    return MediaAsset(
        id = MediaAssetId(UUID.randomUUID().toString()),
        uri = uri,
        source = source,
        artists = artists,
        images = images,
        metadata = mediaMetadata,
    )
}
