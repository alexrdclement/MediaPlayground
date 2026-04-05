package com.alexrdclement.mediaplayground.data.track.local.mapper

import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.SimpleArtist
import com.alexrdclement.mediaplayground.media.model.Source
import kotlinx.collections.immutable.PersistentList
import kotlin.time.Clock
import com.alexrdclement.mediaplayground.database.model.Album as AlbumEntity

fun SimpleAlbum.toAlbumEntity(source: Source): AlbumEntity {
    return AlbumEntity(
        id = id.value,
        title = name,
        modifiedDate = Clock.System.now(),
        source = source.toEntitySource(),
        notes = null,
    )
}

fun AlbumEntity.toSimpleAlbum(
    artists: PersistentList<SimpleArtist>,
    images: PersistentList<Image>,
): SimpleAlbum {
    return SimpleAlbum(
        id = AlbumId(id),
        name = title,
        artists = artists,
        images = images,
        source = source.toDomainSource(),
    )
}
