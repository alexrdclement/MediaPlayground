package com.alexrdclement.mediaplayground.database.dao

import com.alexrdclement.mediaplayground.database.model.Album
import com.alexrdclement.mediaplayground.database.model.Artist
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import com.alexrdclement.mediaplayground.database.model.SimpleAlbum
import com.alexrdclement.mediaplayground.database.model.Track
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal fun assertAlbumEquals(expected: Album, actual: Album?) {
    assertNotNull(actual)
    assertEquals(expected.id, actual.id)
    assertEquals(expected.title, actual.title)
    assertEquals(expected.modifiedDate.epochSeconds, actual.modifiedDate.epochSeconds)
}

internal fun assertArtistEquals(expected: Artist, actual: Artist?) {
    assertNotNull(actual)
    assertEquals(expected, actual)
}

internal fun assertTrackEquals(expected: Track, actual: Track?) {
    assertNotNull(actual)
    assertEquals(expected.id, actual.id)
    assertEquals(expected.albumId, actual.albumId)
    assertEquals(expected.title, actual.title)
    assertEquals(expected.durationMs, actual.durationMs)
    assertEquals(expected.trackNumber, actual.trackNumber)
    assertEquals(expected.fileName, actual.fileName)
    assertEquals(expected.modifiedDate.epochSeconds, actual.modifiedDate.epochSeconds)
}

internal fun assertSimpleAlbumEquals(expected: SimpleAlbum, actual: SimpleAlbum?) {
    assertNotNull(actual)
    assertAlbumEquals(expected.album, actual.album)
    for (artist in expected.artists) {
        assertNotNull(actual.artists.find { it.id == artist.id })
    }
    for (images in expected.images) {
        assertNotNull(actual.images.find { it.id == images.id })
    }
}

internal fun assertCompleteAlbumEquals(expected: CompleteAlbum, actual: CompleteAlbum?) {
    assertNotNull(actual)
    assertSimpleAlbumEquals(expected.simpleAlbum, actual.simpleAlbum)
    for (track in expected.tracks) {
        assertNotNull(actual.tracks.find { it.id == track.id })
    }
}

internal fun assertCompleteTrackEquals(expected: CompleteTrack, actual: CompleteTrack?) {
    assertNotNull(actual)
    assertAlbumEquals(expected.album, actual.album)
    for (artist in expected.artists) {
        assertNotNull(actual.artists.find { it.id == artist.id })
    }
    for (images in expected.images) {
        assertNotNull(actual.images.find { it.id == images.id })
    }
    assertTrackEquals(expected.track, actual.track)
}
