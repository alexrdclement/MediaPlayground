package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.database.model.MediaCollectionType
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlin.time.Clock
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
        createdAt = Clock.System.now(),
        modifiedAt = Clock.System.now(),
    )
}

fun CompleteTrackEntity.toAudioTrack(): AudioTrack {
    val firstRef = albumRefs.first()
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
        simpleAlbum = SimpleAlbum(
            id = AudioAlbumId(firstRef.simpleAlbum.album.id),
            name = firstRef.simpleAlbum.mediaCollection.title,
            artists = artists,
            images = domainImages,
        ),
        notes = track.notes,
    )
}
