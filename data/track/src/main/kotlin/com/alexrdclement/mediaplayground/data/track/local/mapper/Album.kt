package com.alexrdclement.mediaplayground.data.track.local.mapper

import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.audio.Image
import com.alexrdclement.mediaplayground.media.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.media.model.audio.Source
import kotlinx.collections.immutable.PersistentList
import kotlinx.io.files.Path
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
