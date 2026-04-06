package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.io.files.Path
import kotlin.time.Clock
import com.alexrdclement.mediaplayground.database.model.CompleteTrack as CompleteTrackEntity
import com.alexrdclement.mediaplayground.database.model.Track as TrackEntity

fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
        id = id.value,
        title = title,
        albumId = simpleAlbum.id.value,
        trackNumber = trackNumber,
        modifiedDate = Clock.System.now(),
        notes = notes,
    )
}

fun CompleteTrackEntity.toTrack(
    mediaItemDir: Path,
    imagesDir: Path,
): Track {
    val artists = this@toTrack.artists.map { it.toArtist() }.toPersistentList()
    val domainImages = images.map { it.toImage(imagesDir = imagesDir) }.toPersistentList()
    return Track(
        id = TrackId(track.id),
        title = track.title,
        artists = artists,
        trackNumber = track.trackNumber,
        clips = clips.map {
            it.toTrackClip(mediaItemDir, artists, domainImages)
        }.toPersistentSet(),
        simpleAlbum = SimpleAlbum(
            id = AlbumId(album.id),
            name = album.title,
            artists = artists,
            images = domainImages,
            source = album.source.toDomainSource(),
        ),
        notes = track.notes,
    )
}
