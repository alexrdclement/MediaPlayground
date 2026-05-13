package com.alexrdclement.mediaplayground.database.fakes

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.alexrdclement.mediaplayground.database.dao.CompleteAlbumDao
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum
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

class FakeCompleteAlbumDao(
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
    val audioAssetDao: FakeAudioAssetDao = FakeAudioAssetDao(mediaAssetDao),
    val trackClipDao: FakeTrackClipDao = FakeTrackClipDao(),
) : CompleteAlbumDao {

    val completeAlbums = combine(
        combine(albumDao.albums, mediaCollectionDao.mediaCollections) { albums, mediaCollections -> Pair(albums, mediaCollections) },
        artistDao.artists,
        combine(albumTrackDao.albumTrackRefs, audioAssetDao.audioAssets) { refs, assets -> Pair(refs, assets) },
        trackDao.tracks,
        clipDao.clips,
    ) { (albums, mediaCollections), artists, (albumTrackRefs, audioAssets), tracks, clips ->
        val albumArtists = albumArtistDao.albumArtists
        val mediaAssets = mediaAssetDao.mediaAssets
        albums.mapNotNull { album ->
            val mediaCollection = mediaCollections.find { it.id == album.id }
                ?: return@mapNotNull null
            val albumImageIds = albumImageDao.albumImages
                .filter { it.albumId == album.id }
                .map { it.imageId }
            val albumImages = imageDao.images.value
                .filter { it.id in albumImageIds }
                .mapNotNull { imageAsset ->
                    val mediaAsset = mediaAssets[imageAsset.id] ?: return@mapNotNull null
                    CompleteImageAsset(imageAsset = imageAsset, mediaAsset = mediaAsset)
                }
            val albumCrossRefs = albumTrackRefs.filter { it.albumId == album.id }
            val albumTracks = albumCrossRefs.mapNotNull { crossRef ->
                val track = tracks.find { it.id == crossRef.trackId } ?: return@mapNotNull null
                val trackAlbumRefs = albumTrackRefs.filter { it.trackId == track.id }
                val albumRefsList = trackAlbumRefs.mapNotNull { trackCrossRef ->
                    val refAlbum = albums.find { it.id == trackCrossRef.albumId } ?: return@mapNotNull null
                    val refMediaCollection = mediaCollections.find { it.id == trackCrossRef.albumId }
                        ?: return@mapNotNull null
                    val refAlbumImageIds = albumImageDao.albumImages
                        .filter { it.albumId == trackCrossRef.albumId }
                        .map { it.imageId }
                    val refAlbumImages = imageDao.images.value
                        .filter { it.id in refAlbumImageIds }
                        .mapNotNull { imageAsset ->
                            val mediaAsset = mediaAssets[imageAsset.id] ?: return@mapNotNull null
                            CompleteImageAsset(imageAsset = imageAsset, mediaAsset = mediaAsset)
                        }
                    CompleteAlbumRef(
                        albumTrackCrossRef = trackCrossRef,
                        simpleAlbum = SimpleAlbum(
                            album = refAlbum,
                            mediaCollection = refMediaCollection,
                            artists = artists.filter { artist ->
                                albumArtists.contains(AlbumArtistCrossRef(trackCrossRef.albumId, artist.id))
                            },
                            images = refAlbumImages,
                        ),
                    )
                }
                val trackMediaCollection = mediaCollections.find { it.id == track.id }
                    ?: return@mapNotNull null
                val trackClipCrossRefs = trackClipDao.trackClips.filter { it.trackId == track.id }
                val completeTrackClips = trackClipCrossRefs.mapNotNull { clipCrossRef ->
                    val clip = clips.find { it.id == clipCrossRef.clipId } ?: return@mapNotNull null
                    val audioFile = audioAssets.find { it.id == clip.assetId } ?: return@mapNotNull null
                    val mediaAsset = mediaAssets[clip.assetId] ?: return@mapNotNull null
                    CompleteTrackClip(
                        trackClipCrossRef = clipCrossRef,
                        completeAudioClip = CompleteAudioClip(
                            clip = clip,
                            audioAsset = audioFile,
                            mediaAsset = mediaAsset,
                            artists = emptyList(),
                            images = emptyList(),
                        ),
                    )
                }
                CompleteTrack(
                    track = track,
                    mediaCollection = trackMediaCollection,
                    albumRefs = albumRefsList,
                    clips = completeTrackClips,
                )
            }
            CompleteAlbum(
                simpleAlbum = SimpleAlbum(
                    album = album,
                    mediaCollection = mediaCollection,
                    artists = artists.filter { artist ->
                        albumArtists.contains(AlbumArtistCrossRef(album.id, artist.id))
                    },
                    images = albumImages,
                ),
                tracks = albumTracks,
            )
        }
    }

    override fun getAlbumCountFlow(): Flow<Int> = completeAlbums.map { it.size }

    @SuppressLint("VisibleForTests")
    override fun getAlbumsPagingSource(): PagingSource<Int, CompleteAlbum> {
        return completeAlbums.asPagingSourceFactory(coroutineScope).invoke()
    }

    override suspend fun getAlbum(id: String): CompleteAlbum? {
        return completeAlbums.firstOrNull()?.find { it.simpleAlbum.album.id == id }
    }

    override fun getAlbumFlow(id: String): Flow<CompleteAlbum?> {
        return completeAlbums.map { albums -> albums.find { it.simpleAlbum.album.id == id } }
    }
}
