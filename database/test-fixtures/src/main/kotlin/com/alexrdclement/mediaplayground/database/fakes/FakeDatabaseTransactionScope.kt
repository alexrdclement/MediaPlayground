package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionScope
import kotlinx.coroutines.CoroutineScope

class FakeDatabaseTransactionScope(
    coroutineScope: CoroutineScope,
    override val albumDao: FakeAlbumDao = FakeAlbumDao(),
    override val albumArtistDao: FakeAlbumArtistDao = FakeAlbumArtistDao(),
    override val albumImageDao: FakeAlbumImageDao = FakeAlbumImageDao(),
    override val albumTrackDao: FakeAlbumTrackDao = FakeAlbumTrackDao(),
    override val artistDao: FakeArtistDao = FakeArtistDao(),
    override val audioAssetArtistDao: FakeAudioAssetArtistDao = FakeAudioAssetArtistDao(),
    override val audioAssetImageDao: FakeAudioAssetImageDao = FakeAudioAssetImageDao(),
    override val mediaAssetDao: FakeMediaAssetDao = FakeMediaAssetDao(),
    override val mediaAssetSyncStateDao: FakeMediaAssetSyncStateDao = FakeMediaAssetSyncStateDao(),
    override val mediaCollectionDao: FakeMediaCollectionDao = FakeMediaCollectionDao(),
    override val mediaItemDao: FakeMediaItemDao = FakeMediaItemDao(),
    override val audioClipDao: FakeAudioClipDao = FakeAudioClipDao(),
    override val clipDao: FakeClipDao = FakeClipDao(),
    override val imageAssetDao: FakeImageAssetDao = FakeImageAssetDao(mediaAssetDao),
    override val audioAssetDao: FakeAudioAssetDao = FakeAudioAssetDao(
        mediaAssetDao = mediaAssetDao,
        audioAssetArtistDao = audioAssetArtistDao,
        artistDao = artistDao,
        audioAssetImageDao = audioAssetImageDao,
        imageAssetDao = imageAssetDao,
    ),
    override val trackClipDao: FakeTrackClipDao = FakeTrackClipDao(),
    override val trackDao: FakeTrackDao = FakeTrackDao(),
) : DatabaseTransactionScope {
    override val simpleAlbumDao: FakeSimpleAlbumDao = FakeSimpleAlbumDao(
        albumDao = albumDao,
        mediaCollectionDao = mediaCollectionDao,
        artistDao = artistDao,
        imageDao = imageAssetDao,
        albumArtistDao = albumArtistDao,
        albumImageDao = albumImageDao,
    )
    override val completeAudioClipDao: FakeCompleteAudioClipDao = FakeCompleteAudioClipDao(
        coroutineScope = coroutineScope,
        clipDao = clipDao,
        audioClipDao = audioClipDao,
        audioAssetDao = audioAssetDao,
    )
    override val completeTrackDao: FakeCompleteTrackDao = FakeCompleteTrackDao(
        coroutineScope = coroutineScope,
        albumDao = albumDao,
        mediaCollectionDao = mediaCollectionDao,
        artistDao = artistDao,
        albumArtistDao = albumArtistDao,
        albumImageDao = albumImageDao,
        albumTrackDao = albumTrackDao,
        imageDao = imageAssetDao,
        mediaAssetDao = mediaAssetDao,
        trackDao = trackDao,
        clipDao = clipDao,
        audioClipDao = audioClipDao,
        audioAssetDao = audioAssetDao,
        trackClipDao = trackClipDao,
    )

    override val completeAlbumDao: FakeCompleteAlbumDao = FakeCompleteAlbumDao(
        coroutineScope = coroutineScope,
        albumDao = albumDao,
        mediaCollectionDao = mediaCollectionDao,
        artistDao = artistDao,
        albumArtistDao = albumArtistDao,
        albumImageDao = albumImageDao,
        albumTrackDao = albumTrackDao,
        imageDao = imageAssetDao,
        mediaAssetDao = mediaAssetDao,
        trackDao = trackDao,
        clipDao = clipDao,
        audioClipDao = audioClipDao,
        audioAssetDao = audioAssetDao,
        trackClipDao = trackClipDao,
    )
}
