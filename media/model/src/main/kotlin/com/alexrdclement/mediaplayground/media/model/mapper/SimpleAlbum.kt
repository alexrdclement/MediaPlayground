package com.alexrdclement.mediaplayground.media.model.mapper

import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.Source
import kotlinx.collections.immutable.PersistentList

fun MediaMetadata.Audio.toSimpleAlbum(
    id: AlbumId,
    title: String,
    artists: PersistentList<Artist>,
    images: PersistentList<Image>,
    source: Source,
): SimpleAlbum = SimpleAlbum(
    id = id,
    name = title,
    artists = artists,
    images = images,
    source = source,
)
