package com.alexrdclement.mediaplayground.data.track.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.mapping.toAudioAssetEntity
import com.alexrdclement.mediaplayground.database.mapping.toAudioClip
import com.alexrdclement.mediaplayground.database.mapping.toAudioClipEntity
import com.alexrdclement.mediaplayground.database.mapping.toAudioTrack
import com.alexrdclement.mediaplayground.database.mapping.toClipEntity
import com.alexrdclement.mediaplayground.database.mapping.toMediaAssetRecord
import com.alexrdclement.mediaplayground.database.mapping.toMediaCollectionEntity
import com.alexrdclement.mediaplayground.database.mapping.toMediaItemEntity
import com.alexrdclement.mediaplayground.database.mapping.toTrackEntity
import com.alexrdclement.mediaplayground.database.model.AlbumTrackCrossRef
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef
import com.alexrdclement.mediaplayground.database.transaction.ClipData
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.deleteTrack
import com.alexrdclement.mediaplayground.database.transaction.insertTrack
import com.alexrdclement.mediaplayground.database.transaction.updateTrack
import com.alexrdclement.mediaplayground.media.model.AlbumTrack
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.alexrdclement.mediaplayground.media.model.deletion.DeleteTrackPolicy
import dev.zacsweers.metro.Inject
import kotlin.time.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class LocalTrackDataStore @Inject constructor(
    private val completeTrackDao: CompleteTrackDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
) {

    fun getTrackCountFlow(): Flow<Int> {
        return completeTrackDao.getTrackCountFlow().distinctUntilChanged()
    }

    fun getTrackPagingData(config: PagingConfig): Flow<PagingData<Track>> {
        return Pager(config = config) {
            completeTrackDao.getTracksPagingSource()
        }.flow.map { pagingData ->
            pagingData.map { it.toAudioTrack() }
        }
    }

    suspend fun getTrack(trackId: TrackId): Track? {
        val entity = completeTrackDao.getTrack(trackId.value) ?: return null
        return entity.toAudioTrack()
    }

    fun getTrackFlow(trackId: TrackId): Flow<Track?> {
        return completeTrackDao.getTrackFlow(trackId.value).map { completeTrack ->
            completeTrack?.toAudioTrack()
        }
    }

    suspend fun updateTrackTitle(id: TrackId, title: String) {
        databaseTransactionRunner.run {
            updateTrack(id.value, title)
        }
    }

    suspend fun put(albumTrack: AlbumTrack) {
        val track = albumTrack.track
        val mediaCollectionEntity = track.toMediaCollectionEntity()
        val trackEntity = track.toTrackEntity()
        val albumTrackCrossRef = AlbumTrackCrossRef(
            albumId = albumTrack.albumId.value,
            trackId = track.id.value,
            trackNumber = albumTrack.trackNumber,
        )
        val clipsAndAudioAssets = track.items.map { trackClip ->
            ClipData(
                clipMediaItem = trackClip.clip.toMediaItemEntity(),
                clip = trackClip.clip.toClipEntity(),
                audioClip = trackClip.clip.toAudioClipEntity(),
                assetMediaItem = trackClip.clip.mediaAsset.toMediaItemEntity(),
                mediaAsset = trackClip.clip.mediaAsset.toMediaAssetRecord(),
                audioAsset = trackClip.clip.mediaAsset.toAudioAssetEntity(),
                artistIds = trackClip.clip.mediaAsset.artists.map { it.id.value }.toSet(),
                imageIds = trackClip.clip.mediaAsset.images.map { it.id.value }.toSet(),
            )
        }
        val crossRefs = track.items.map { trackClip ->
            TrackClipCrossRef(
                id = trackClip.id.value,
                trackId = track.id.value,
                clipId = trackClip.clip.id.value,
                startSampleInTrack = trackClip.trackOffset.samples,
                createdAt = trackClip.createdAt,
                modifiedAt = trackClip.modifiedAt,
            )
        }
        databaseTransactionRunner.run {
            insertTrack(
                mediaItem = track.toMediaItemEntity(),
                mediaCollection = mediaCollectionEntity,
                track = trackEntity,
                albumTrackCrossRefs = listOf(albumTrackCrossRef),
                clips = clipsAndAudioAssets,
                trackClipCrossRefs = crossRefs,
            )
        }
    }

    suspend fun updateTrackNotes(id: TrackId, notes: String?) {
        databaseTransactionRunner.run {
            val track = trackDao.getTrack(id.value) ?: return@run
            trackDao.update(track.copy(notes = notes))
            val mi = mediaItemDao.getMediaItem(id.value) ?: return@run
            mediaItemDao.update(mi.copy(modifiedAt = Clock.System.now()))
        }
    }

    suspend fun delete(id: TrackId, policy: DeleteTrackPolicy = DeleteTrackPolicy()) {
        databaseTransactionRunner.run {
            deleteTrack(id.value, policy)
        }
    }
}
