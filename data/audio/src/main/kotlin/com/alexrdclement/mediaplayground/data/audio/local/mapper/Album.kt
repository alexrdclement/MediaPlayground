package com.alexrdclement.mediaplayground.data.audio.local.mapper

import com.alexrdclement.mediaplayground.database.model.CompleteAlbum
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Image
import com.alexrdclement.mediaplayground.model.audio.SimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.Source
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.io.files.Path
import kotlin.time.Clock
import com.alexrdclement.mediaplayground.database.model.Album as AlbumEntity
import com.alexrdclement.mediaplayground.database.model.SimpleAlbum as SimpleAlbumEntity

fun SimpleAlbum.toAlbumEntity(source: Source): AlbumEntity {
    return AlbumEntity(
        id = id.value,
        title = name,
        modifiedDate = Clock.System.now(),
        source = source.toEntitySource(),
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

fun AlbumEntity.toAlbum(
    artists: PersistentList<SimpleArtist>,
    images: PersistentList<Image>,
    simpleTracks: PersistentList<SimpleTrack>,
): Album {
    return Album(
        id = AlbumId(id),
        title = title,
        artists = artists,
        images = images,
        tracks = simpleTracks,
        source = source.toDomainSource(),
    )
}

fun SimpleAlbumEntity.toSimpleAlbum(
    mediaItemDir: Path,
): SimpleAlbum {
    return SimpleAlbum(
        id = AlbumId(album.id),
        name = album.title,
        artists = artists.map { it.toSimpleArtist() }.toPersistentList(),
        images = images.map { it.toImage(albumDir = mediaItemDir) }.toPersistentList(),
        source = album.source.toDomainSource(),
    )
}

fun CompleteAlbum.toAlbum(
    mediaItemDir: Path,
): Album {
    val simpleArtists = simpleAlbum.artists.map { it.toSimpleArtist() }.toPersistentList()
    return simpleAlbum.album.toAlbum(
        artists = simpleArtists,
        images = simpleAlbum.images.map { it.toImage(albumDir = mediaItemDir) }.toPersistentList(),
        simpleTracks = tracks.map { it.toSimpleTrack(mediaItemDir, simpleArtists) }.toPersistentList(),
    )
}
