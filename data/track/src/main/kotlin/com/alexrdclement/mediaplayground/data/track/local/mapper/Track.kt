package com.alexrdclement.mediaplayground.data.track.local.mapper

import com.alexrdclement.mediaplayground.data.disk.mapper.fileNameFromUri
import com.alexrdclement.mediaplayground.data.disk.mapper.uriFromFileName
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.SimpleArtist
import com.alexrdclement.mediaplayground.media.model.SimpleTrack
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.io.files.Path
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import com.alexrdclement.mediaplayground.database.model.CompleteTrack as CompleteTrackEntity
import com.alexrdclement.mediaplayground.database.model.Track as TrackEntity

fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
        id = id.value,
        fileName = fileNameFromUri(uri),
        title = title,
        albumId = simpleAlbum.id.value,
        durationMs = duration.inWholeMilliseconds.toInt(),
        trackNumber = trackNumber,
        modifiedDate = Clock.System.now(),
        source = source.toEntitySource(),
        notes = notes,
    )
}

fun TrackEntity.toSimpleTrack(
    mediaItemDir: Path,
    simpleArtists: PersistentList<SimpleArtist>,
): SimpleTrack {
    return SimpleTrack(
        id = TrackId(id),
        uri = uriFromFileName(mediaItemDir, fileName),
        name = title,
        artists = simpleArtists,
        duration = durationMs.milliseconds,
        trackNumber = trackNumber,
        source = source.toDomainSource(),
    )
}

fun CompleteTrackEntity.toTrack(
    albumDir: Path,
    imagesDir: Path,
): Track {
    val simpleArtists = artists.map { it.toSimpleArtist() }.toPersistentList()
    return Track(
        id = TrackId(track.id),
        title = track.title,
        artists = simpleArtists,
        duration = track.durationMs.milliseconds,
        trackNumber = track.trackNumber,
        uri = uriFromFileName(albumDir, track.fileName),
        simpleAlbum = SimpleAlbum(
            id = AlbumId(album.id),
            name = album.title,
            artists = simpleArtists,
            images = images.map { it.toImage(imagesDir = imagesDir) }.toPersistentList(),
            source = album.source.toDomainSource(),
        ),
        source = track.source.toDomainSource(),
        notes = track.notes,
    )
}
