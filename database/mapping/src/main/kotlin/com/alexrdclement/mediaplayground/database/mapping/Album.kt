package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.database.model.MediaCollectionType
import com.alexrdclement.mediaplayground.database.model.MediaItemType
import com.alexrdclement.mediaplayground.database.model.id
import com.alexrdclement.mediaplayground.media.model.AlbumTrack
import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import kotlinx.collections.immutable.toPersistentList
import kotlin.time.Clock
import com.alexrdclement.mediaplayground.database.model.Album as AlbumEntity
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum as CompleteAlbumEntity
import com.alexrdclement.mediaplayground.database.model.MediaCollection as MediaCollectionEntity
import com.alexrdclement.mediaplayground.database.model.MediaItem
import com.alexrdclement.mediaplayground.database.model.SimpleAlbum as SimpleAlbumEntity

fun SimpleAlbum.toMediaCollectionEntity(): MediaCollectionEntity {
    return MediaCollectionEntity(
        id = id.value,
        mediaCollectionType = MediaCollectionType.ALBUM,
    )
}

fun SimpleAlbum.toMediaItemEntity(): MediaItem {
    return MediaItem(
        id = id.value,
        itemType = MediaItemType.COLLECTION,
        title = name,
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
        name = mediaItem.title,
        artists = artists.map { it.toArtist() }.toPersistentList(),
        images = images.map { it.toImage() }.toPersistentList(),
    )
}

fun CompleteAlbumEntity.toAlbum(): AudioAlbum {
    val albumId = AudioAlbumId(this.id)
    val orderedAudioTracks = orderedTracks.map { it.toAudioTrack() }
    val albumArtists = simpleAlbum.artists.map { it.toArtist() }.toPersistentList()
    val artists = if (albumArtists.isNotEmpty()) albumArtists
                  else orderedAudioTracks.flatMap { it.artists }.distinct().toPersistentList()
    val albumImages = simpleAlbum.images.map { it.toImage() }.toPersistentList()
    val images = if (albumImages.isNotEmpty()) albumImages
                 else orderedAudioTracks.flatMap { it.images }.distinct().toPersistentList()
    return AudioAlbum(
        id = albumId,
        title = simpleAlbum.mediaItem.title,
        artists = artists,
        images = images,
        items = orderedTracks.mapIndexed { index, completeTrack ->
            val albumRef = completeTrack.albumRefs.find {
                it.albumTrackCrossRef.albumId == albumId.value
            }
            AlbumTrack(
                track = orderedAudioTracks[index],
                albumId = albumId,
                trackNumber = albumRef?.albumTrackCrossRef?.trackNumber,
            )
        }.toPersistentList(),
        notes = simpleAlbum.album.notes,
        createdAt = simpleAlbum.mediaItem.createdAt,
        modifiedAt = simpleAlbum.mediaItem.modifiedAt,
    )
}
