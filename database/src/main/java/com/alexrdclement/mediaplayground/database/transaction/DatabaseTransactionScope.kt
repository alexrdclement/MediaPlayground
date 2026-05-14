package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.dao.AlbumArtistDao
import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.dao.AlbumImageDao
import com.alexrdclement.mediaplayground.database.dao.AlbumTrackDao
import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.dao.AudioAssetArtistDao
import com.alexrdclement.mediaplayground.database.dao.AudioAssetDao
import com.alexrdclement.mediaplayground.database.dao.AudioAssetImageDao
import com.alexrdclement.mediaplayground.database.dao.AudioClipDao
import com.alexrdclement.mediaplayground.database.dao.ClipDao
import com.alexrdclement.mediaplayground.database.dao.CompleteAlbumDao
import com.alexrdclement.mediaplayground.database.dao.CompleteAudioClipDao
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.dao.ImageAssetDao
import com.alexrdclement.mediaplayground.database.dao.MediaAssetDao
import com.alexrdclement.mediaplayground.database.dao.MediaAssetSyncStateDao
import com.alexrdclement.mediaplayground.database.dao.MediaCollectionDao
import com.alexrdclement.mediaplayground.database.dao.MediaItemDao
import com.alexrdclement.mediaplayground.database.dao.SimpleAlbumDao
import com.alexrdclement.mediaplayground.database.dao.TrackClipDao
import com.alexrdclement.mediaplayground.database.dao.TrackDao

interface DatabaseTransactionScope {
    val albumDao: AlbumDao
    val albumArtistDao: AlbumArtistDao
    val albumImageDao: AlbumImageDao
    val albumTrackDao: AlbumTrackDao
    val artistDao: ArtistDao
    val audioAssetArtistDao: AudioAssetArtistDao
    val audioAssetDao: AudioAssetDao
    val audioAssetImageDao: AudioAssetImageDao
    val audioClipDao: AudioClipDao
    val clipDao: ClipDao
    val completeAlbumDao: CompleteAlbumDao
    val completeAudioClipDao: CompleteAudioClipDao
    val completeTrackDao: CompleteTrackDao
    val imageAssetDao: ImageAssetDao
    val mediaAssetDao: MediaAssetDao
    val mediaAssetSyncStateDao: MediaAssetSyncStateDao
    val mediaCollectionDao: MediaCollectionDao
    val mediaItemDao: MediaItemDao
    val simpleAlbumDao: SimpleAlbumDao
    val trackClipDao: TrackClipDao
    val trackDao: TrackDao
}
