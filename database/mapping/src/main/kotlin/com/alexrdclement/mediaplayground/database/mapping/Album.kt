package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.SimpleTrack
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.io.files.Path
import kotlin.time.Clock
import com.alexrdclement.mediaplayground.database.model.Album as AlbumEntity
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum as CompleteAlbumEntity
import com.alexrdclement.mediaplayground.database.model.SimpleAlbum as SimpleAlbumEntity

fun SimpleAlbum.toAlbumEntity(): AlbumEntity {
    return AlbumEntity(
        id = id.value,
        title = name,
        modifiedDate = Clock.System.now(),
        source = source.toEntitySource(),
        notes = null,
    )
}

fun AlbumEntity.toAlbum(
    artists: PersistentList<Artist>,
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
        notes = notes,
    )
}

fun SimpleAlbumEntity.toSimpleAlbum(
    mediaItemDir: Path,
    imagesDir: Path,
): SimpleAlbum {
    return SimpleAlbum(
        id = AlbumId(album.id),
        name = album.title,
        artists = artists.map { it.toArtist() }.toPersistentList(),
        images = images.map { it.toImage(imagesDir = imagesDir) }.toPersistentList(),
        source = album.source.toDomainSource(),
    )
}

fun CompleteAlbumEntity.toAlbum(
    mediaItemDir: Path,
    imagesDir: Path,
): Album {
    val artists = simpleAlbum.artists.map { it.toArtist() }.toPersistentList()
    val domainImages = simpleAlbum.images.map { it.toImage(imagesDir = imagesDir) }.toPersistentList()
    return simpleAlbum.album.toAlbum(
        artists = artists,
        images = domainImages,
        simpleTracks = orderedTracks
            .map { it.toSimpleTrack(mediaItemDir, artists, domainImages) }
            .toPersistentList(),
    )
}
