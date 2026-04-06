package com.alexrdclement.mediaplayground.media.mediaimport.factory

import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.Source
import kotlinx.collections.immutable.PersistentList
import kotlinx.io.files.Path
import java.util.UUID

internal fun makeMediaAsset(
    filePath: Path,
    mediaMetadata: MediaMetadata.Audio,
    source: Source,
    artists: PersistentList<Artist>,
    images: PersistentList<Image>,
): MediaAsset {
    return MediaAsset(
        id = MediaAssetId(UUID.randomUUID().toString()),
        uri = filePath.toString(),
        source = source,
        artists = artists,
        images = images,
        metadata = mediaMetadata,
    )
}
