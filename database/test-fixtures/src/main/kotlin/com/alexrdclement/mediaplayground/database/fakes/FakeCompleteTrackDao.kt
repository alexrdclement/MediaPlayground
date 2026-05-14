package com.alexrdclement.mediaplayground.database.fakes

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteAlbumRef
import com.alexrdclement.mediaplayground.database.model.CompleteAudioClip
import com.alexrdclement.mediaplayground.database.model.CompleteImageAsset
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import com.alexrdclement.mediaplayground.database.model.CompleteTrackClip
import com.alexrdclement.mediaplayground.database.model.SimpleAlbum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class FakeCompleteTrackDao(
    val coroutineScope: CoroutineScope,
    val albumDao: FakeAlbumDao,
    val mediaCollectionDao: FakeMediaCollectionDao = FakeMediaCollectionDao(),
    val artistDao: FakeArtistDao,
    val albumArtistDao: FakeAlbumArtistDao,
    val albumImageDao: FakeAlbumImageDao = FakeAlbumImageDao(),
    val albumTrackDao: FakeAlbumTrackDao = FakeAlbumTrackDao(),
    val imageDao: FakeImageAssetDao,
    val mediaAssetDao: FakeMediaAssetDao = FakeMediaAssetDao(),
    val trackDao: FakeTrackDao,
    val clipDao: FakeClipDao = FakeClipDao(),
    val audioClipDao: FakeAudioClipDao = FakeAudioClipDao(),
    val audioAssetDao: FakeAudioAssetDao = FakeAudioAssetDao(mediaAssetDao),
    val trackClipDao: FakeTrackClipDao = FakeTrackClipDao(),
) : CompleteTrackDao {

    val completeTracks = combine(
        combine(albumDao.albums, mediaCollectionDao.mediaCollections) { albums, mediaCollections -> Pair(albums, mediaCollections) },
        artistDao.artists,
        combine(albumTrackDao.albumTrackRefs, audioAssetDao.audioAssets) { refs, assets -> Pair(refs, assets) },
        combine(trackDao.tracks, clipDao.clips) { tracks, clips -> Pair(tracks, clips) },
        audioClipDao.audioClips,
    ) { (albums, mediaCollections), artists, (albumTrackRefs, audioFiles), (tracks, clips), audioClips ->
        val albumArtists = albumArtistDao.albumArtists
        val mediaAssets = mediaAssetDao.mediaAssets
        tracks.mapNotNull { track ->
            val trackCrossRefs = albumTrackRefs.filter { it.trackId == track.id }
            if (trackCrossRefs.isEmpty()) return@mapNotNull null

            val albumRefs = trackCrossRefs.mapNotNull { crossRef ->
                val album = albums.find { it.id == crossRef.albumId } ?: return@mapNotNull null
                val mediaCollection = mediaCollections.find { it.id == crossRef.albumId }
                    ?: return@mapNotNull null
                val albumImageIds = albumImageDao.albumImages
                    .filter { it.albumId == crossRef.albumId }
                    .map { it.imageId }
                val images = imageDao.images.value
                    .filter { it.id in albumImageIds }
                    .mapNotNull { imageAsset ->
                        val mediaAsset = mediaAssets[imageAsset.id] ?: return@mapNotNull null
                        CompleteImageAsset(imageAsset = imageAsset, mediaAsset = mediaAsset)
                    }
                CompleteAlbumRef(
                    albumTrackCrossRef = crossRef,
                    simpleAlbum = SimpleAlbum(
                        album = album,
                        mediaCollection = mediaCollection,
                        artists = artists.filter { artist ->
                            albumArtists.contains(AlbumArtistCrossRef(crossRef.albumId, artist.id))
                        },
                        images = images,
                    ),
                )
            }
            if (albumRefs.isEmpty()) return@mapNotNull null

            val trackMediaCollection = mediaCollections.find { it.id == track.id }
                ?: return@mapNotNull null

            val trackClipCrossRefs = trackClipDao.trackClips.filter { it.trackId == track.id }
            val completeTrackClips = trackClipCrossRefs.mapNotNull { crossRef ->
                val clip = clips.find { it.id == crossRef.clipId } ?: return@mapNotNull null
                val audioClip = audioClips.find { it.id == clip.id } ?: return@mapNotNull null
                val audioAsset = audioFiles.find { it.id == clip.assetId } ?: return@mapNotNull null
                val mediaAsset = mediaAssets[clip.assetId] ?: return@mapNotNull null
                CompleteTrackClip(
                    trackClipCrossRef = crossRef,
                    completeAudioClip = CompleteAudioClip(
                        clip = clip,
                        audioClip = audioClip,
                        audioAsset = audioAsset,
                        mediaAsset = mediaAsset,
                        artists = emptyList(),
                        images = emptyList(),
                    ),
                )
            }
            CompleteTrack(
                track = track,
                mediaCollection = trackMediaCollection,
                albumRefs = albumRefs,
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
