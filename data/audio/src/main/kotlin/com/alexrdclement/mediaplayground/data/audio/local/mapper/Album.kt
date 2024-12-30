package com.alexrdclement.mediaplayground.data.audio.local.mapper

import com.alexrdclement.mediaplayground.database.model.CompleteAlbum
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Image
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import kotlinx.datetime.Clock
import com.alexrdclement.mediaplayground.database.model.Album as AlbumEntity

fun SimpleAlbum.toAlbumEntity(): AlbumEntity {
    return AlbumEntity(
        id = id.value,
        title = name,
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

fun AlbumEntity.toAlbum(
    artists: List<SimpleArtist>,
    images: List<Image>,
    simpleTracks: List<SimpleTrack>,
): Album {
    return Album(
        id = AlbumId(id),
        title = title,
        artists = artists,
        images = images,
        tracks = simpleTracks,
    )
}

fun CompleteAlbum.toAlbum(): Album {
    val simpleArtists = simpleAlbum.artists.map { it.toSimpleArtist() }
    return simpleAlbum.album.toAlbum(
        artists = simpleArtists,
        images = simpleAlbum.images.map { it.toImage() },
        simpleTracks = tracks.map { it.toSimpleTrack(simpleArtists) },
    )
}
