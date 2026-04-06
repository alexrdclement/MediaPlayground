package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.dao.AlbumArtistDao
import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.dao.AlbumImageDao
import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.dao.AudioFileDao
import com.alexrdclement.mediaplayground.database.dao.ClipDao
import com.alexrdclement.mediaplayground.database.dao.CompleteAlbumDao
import com.alexrdclement.mediaplayground.database.dao.CompleteAudioClipDao
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.dao.ImageFileDao
import com.alexrdclement.mediaplayground.database.dao.SimpleAlbumDao
import com.alexrdclement.mediaplayground.database.dao.TrackClipDao
import com.alexrdclement.mediaplayground.database.dao.TrackDao

interface DatabaseTransactionScope {
    val albumDao: AlbumDao
    val albumArtistDao: AlbumArtistDao
    val albumImageDao: AlbumImageDao
    val artistDao: ArtistDao
    val audioFileDao: AudioFileDao
    val clipDao: ClipDao
    val completeAlbumDao: CompleteAlbumDao
    val completeAudioClipDao: CompleteAudioClipDao
    val completeTrackDao: CompleteTrackDao
    val imageFileDao: ImageFileDao
    val simpleAlbumDao: SimpleAlbumDao
    val trackClipDao: TrackClipDao
    val trackDao: TrackDao
}
