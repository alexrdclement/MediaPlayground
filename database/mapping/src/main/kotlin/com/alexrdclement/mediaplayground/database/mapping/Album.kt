package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.database.model.MediaCollectionType
import com.alexrdclement.mediaplayground.database.model.id
import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import kotlinx.collections.immutable.toPersistentList
import com.alexrdclement.mediaplayground.database.model.Album as AlbumEntity
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum as CompleteAlbumEntity
import com.alexrdclement.mediaplayground.database.model.MediaCollection as MediaCollectionEntity
import com.alexrdclement.mediaplayground.database.model.SimpleAlbum as SimpleAlbumEntity

fun SimpleAlbum.toMediaCollectionEntity(): MediaCollectionEntity {
    return MediaCollectionEntity(
        id = id.value,
        title = name,
        mediaCollectionType = MediaCollectionType.ALBUM,
        createdAt = Clock.System.now(),
        modifiedAt = Clock.System.now(),
    )
}

fun SimpleAlbum.toAlbumEntity(): AlbumEntity {
    return AlbumEntity(
        id = id.value,
        notes = null,
    )
}

fun SimpleAlbumEntity.toSimpleAlbum(): SimpleAlbum {
    return SimpleAlbum(
        id = AudioAlbumId(album.id),
        name = mediaCollection.title,
        artists = artists.map { it.toArtist() }.toPersistentList(),
        images = images.map { it.toImage() }.toPersistentList(),
    )
}

fun CompleteAlbumEntity.toAlbum(): AudioAlbum {
    val albumId = this.id
    val artists = simpleAlbum.artists.map { it.toArtist() }.toPersistentList()
    val domainImages = simpleAlbum.images.map { it.toImage() }.toPersistentList()
    return AudioAlbum(
        id = AudioAlbumId(albumId),
        title = simpleAlbum.mediaCollection.title,
        artists = artists,
        images = domainImages,
        items = orderedTracks
            .map { it.toAudioTrack() }
            .toPersistentList(),
        notes = simpleAlbum.album.notes,
    )
}
