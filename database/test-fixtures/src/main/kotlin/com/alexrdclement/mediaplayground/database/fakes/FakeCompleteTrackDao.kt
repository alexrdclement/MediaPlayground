package com.alexrdclement.mediaplayground.database.fakes

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteAudioClip
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import com.alexrdclement.mediaplayground.database.model.CompleteTrackClip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class FakeCompleteTrackDao(
    val coroutineScope: CoroutineScope,
    val albumDao: FakeAlbumDao,
    val artistDao: FakeArtistDao,
    val albumArtistDao: FakeAlbumArtistDao,
    val albumImageDao: FakeAlbumImageDao = FakeAlbumImageDao(),
    val imageDao: FakeImageFileDao,
    val trackDao: FakeTrackDao,
    val clipDao: FakeClipDao = FakeClipDao(),
    val audioFileDao: FakeAudioFileDao = FakeAudioFileDao(),
    val trackClipDao: FakeTrackClipDao = FakeTrackClipDao(),
) : CompleteTrackDao {

    val completeTracks = combine(
        albumDao.albums,
        artistDao.artists,
        trackDao.tracks,
        clipDao.clips,
        audioFileDao.audioFiles,
    ) { albums, artists, tracks, clips, audioFiles ->
        val albumArtists = albumArtistDao.albumArtists
        tracks.mapNotNull { track ->
            val album = albums.find { it.id == track.albumId } ?: return@mapNotNull null
            val albumImageIds = albumImageDao.albumImages
                .filter { ref -> ref.albumId == track.albumId }
                .map { ref -> ref.imageId }
            val trackClipCrossRefs = trackClipDao.trackClips.filter { it.trackId == track.id }
            val completeTrackClips = trackClipCrossRefs.mapNotNull { crossRef ->
                val clip = clips.find { it.id == crossRef.clipId } ?: return@mapNotNull null
                val audioFile = audioFiles.find { it.id == clip.assetId } ?: return@mapNotNull null
                CompleteTrackClip(
                    trackClipCrossRef = crossRef,
                    completeAudioClip = CompleteAudioClip(
                        clip = clip,
                        audioFile = audioFile,
                    ),
                )
            }
            CompleteTrack(
                track = track,
                album = album,
                artists = artists.filter { artist ->
                    albumArtists.contains(AlbumArtistCrossRef(track.albumId, artist.id))
                },
                images = imageDao.images.value.filter { it.id in albumImageIds },
                clips = completeTrackClips,
            )
        }
    }

    override fun getTrackCountFlow(): Flow<Int> = completeTracks.map { it.size }

    @SuppressLint("VisibleForTests")
    override fun getTracksPagingSource(): PagingSource<Int, CompleteTrack> {
        return completeTracks.asPagingSourceFactory(coroutineScope).invoke()
    }

    override suspend fun getTrack(id: String): CompleteTrack? {
        return completeTracks.firstOrNull()?.find { it.track.id == id }
    }

    override fun getTrackFlow(id: String): Flow<CompleteTrack?> {
        return completeTracks.map { tracks -> tracks.find { it.track.id == id } }
    }

    override suspend fun getTrackByClipId(clipId: String): CompleteTrack? {
        return completeTracks.firstOrNull()?.find { track ->
            track.clips.any { it.completeAudioClip.clip.id == clipId }
        }
    }
}
