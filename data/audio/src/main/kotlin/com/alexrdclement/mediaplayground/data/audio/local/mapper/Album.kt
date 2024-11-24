package com.alexrdclement.mediaplayground.data.audio.local.mapper

import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Image
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import kotlinx.datetime.Clock
import com.alexrdclement.mediaplayground.database.model.Album as AlbumEntity

fun SimpleAlbum.toAlbumEntity(): AlbumEntity {
    return AlbumEntity(
        id = id.value,
        title = name,
        artistId = artists.first().id,
        modifiedDate = Clock.System.now(),
    )
}

fun AlbumEntity.toSimpleAlbum(
    artists: List<SimpleArtist>,
    images: List<Image>,
): SimpleAlbum {
    return SimpleAlbum(
        id = AlbumId(id),
        name = title,
        artists = artists,
        images = images,
    )
}
