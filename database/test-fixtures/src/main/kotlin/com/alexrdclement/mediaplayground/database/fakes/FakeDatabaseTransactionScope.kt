package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionScope
import kotlinx.coroutines.CoroutineScope

class FakeDatabaseTransactionScope(
    coroutineScope: CoroutineScope,
    override val albumDao: FakeAlbumDao = FakeAlbumDao(),
    override val albumArtistDao: FakeAlbumArtistDao = FakeAlbumArtistDao(),
    override val albumImageDao: FakeAlbumImageDao = FakeAlbumImageDao(),
    override val artistDao: FakeArtistDao = FakeArtistDao(),
    override val audioFileDao: FakeAudioFileDao = FakeAudioFileDao(),
    override val clipDao: FakeClipDao = FakeClipDao(),
    override val imageFileDao: FakeImageFileDao = FakeImageFileDao(),
    override val trackClipDao: FakeTrackClipDao = FakeTrackClipDao(),
    override val trackDao: FakeTrackDao = FakeTrackDao(),
) : DatabaseTransactionScope {
    override val simpleAlbumDao: FakeSimpleAlbumDao = FakeSimpleAlbumDao(
        albumDao = albumDao,
        artistDao = artistDao,
        imageDao = imageFileDao,
        albumArtistDao = albumArtistDao,
        albumImageDao = albumImageDao,
    )
    override val completeAudioClipDao: FakeCompleteAudioClipDao = FakeCompleteAudioClipDao(
        coroutineScope = coroutineScope,
        clipDao = clipDao,
        audioFileDao = audioFileDao,
    )
    override val completeTrackDao: FakeCompleteTrackDao = FakeCompleteTrackDao(
        coroutineScope = coroutineScope,
        albumDao = albumDao,
        artistDao = artistDao,
        albumArtistDao = albumArtistDao,
        albumImageDao = albumImageDao,
        imageDao = imageFileDao,
        trackDao = trackDao,
        clipDao = clipDao,
        audioFileDao = audioFileDao,
        trackClipDao = trackClipDao,
    )
    override val completeAlbumDao: FakeCompleteAlbumDao = FakeCompleteAlbumDao(
        coroutineScope = coroutineScope,
        albumDao = albumDao,
        artistDao = artistDao,
        albumArtistDao = albumArtistDao,
        albumImageDao = albumImageDao,
        imageDao = imageFileDao,
        trackDao = trackDao,
        clipDao = clipDao,
        audioFileDao = audioFileDao,
        trackClipDao = trackClipDao,
    )
}
