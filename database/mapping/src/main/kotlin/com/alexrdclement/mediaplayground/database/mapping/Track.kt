package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.database.model.MediaCollectionType
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.collections.immutable.persistentListOf
import com.alexrdclement.mediaplayground.database.model.CompleteTrack as CompleteTrackEntity
import com.alexrdclement.mediaplayground.database.model.MediaCollection as MediaCollectionEntity
import com.alexrdclement.mediaplayground.database.model.Track as TrackEntity

fun AudioTrack.toTrackEntity(): TrackEntity {
    return TrackEntity(
        id = id.value,
        notes = notes,
    )
}

fun AudioTrack.toMediaCollectionEntity(): MediaCollectionEntity {
    return MediaCollectionEntity(
        id = id.value,
        title = title,
        mediaCollectionType = MediaCollectionType.TRACK,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
    )
}

fun CompleteTrackEntity.toAudioTrack(): AudioTrack {
    val firstRef = albumRefs.firstOrNull() ?: error("Track ${track.id} has no album refs")
    val artists = firstRef.simpleAlbum.artists.map { it.toArtist() }.toPersistentList()
    val domainImages = firstRef.simpleAlbum.images.map { it.toImage() }.toPersistentList()
    return AudioTrack(
        id = TrackId(track.id),
        title = mediaCollection.title,
        artists = artists,
        trackNumber = firstRef.albumTrackCrossRef.trackNumber,
        clips = clips.map {
            it.toTrackClip(artists, domainImages)
        }.toPersistentSet(),
        albums = albumRefs.map { ref ->
            SimpleAlbum(
                id = AudioAlbumId(ref.simpleAlbum.album.id),
                name = ref.simpleAlbum.mediaCollection.title,
                artists = ref.simpleAlbum.artists.map { it.toArtist() }.toPersistentList(),
                images = ref.simpleAlbum.images.map { it.toImage() }.toPersistentList(),
            )
        }.toPersistentList(),
        notes = track.notes,
        createdAt = mediaCollection.createdAt,
        modifiedAt = mediaCollection.modifiedAt,
    )
}
