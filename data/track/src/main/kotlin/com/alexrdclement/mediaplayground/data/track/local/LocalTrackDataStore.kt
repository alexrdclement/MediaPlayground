package com.alexrdclement.mediaplayground.data.track.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.mapping.toAudioAssetEntity
import com.alexrdclement.mediaplayground.database.mapping.toAudioTrack
import com.alexrdclement.mediaplayground.database.mapping.toClipEntity
import com.alexrdclement.mediaplayground.database.mapping.toMediaAssetRecord
import com.alexrdclement.mediaplayground.database.mapping.toMediaCollectionEntity
import com.alexrdclement.mediaplayground.database.mapping.toTrackEntity
import com.alexrdclement.mediaplayground.database.model.AlbumTrackCrossRef
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.deleteTrack
import com.alexrdclement.mediaplayground.database.transaction.insertTrack
import com.alexrdclement.mediaplayground.database.transaction.updateTrack
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackId
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

    suspend fun put(track: Track) {
        when (track) {
            is AudioTrack -> putAudioTrack(track)
        }
    }

    private suspend fun putAudioTrack(track: AudioTrack) {
        val mediaCollectionEntity = track.toMediaCollectionEntity()
        val trackEntity = track.toTrackEntity()
        val albumTrackCrossRef = AlbumTrackCrossRef(
            albumId = track.simpleAlbum.id.value,
            trackId = track.id.value,
            trackNumber = track.trackNumber,
        )
        val clipsAndAudioAssets = track.clips.mapNotNull { trackClip ->
            val audioAsset = trackClip.clip.mediaAsset as? AudioAsset ?: return@mapNotNull null
            Triple(
                trackClip.clip.toClipEntity(),
                audioAsset.toMediaAssetRecord(),
                audioAsset.toAudioAssetEntity(),
            )
        }
        val crossRefs = track.clips.map { trackClip ->
            TrackClipCrossRef(
                trackId = track.id.value,
                clipId = trackClip.clip.id.value,
                startFrameInTrack = trackClip.trackOffset.samples,
            )
        }
        databaseTransactionRunner.run {
            insertTrack(
                mediaCollection = mediaCollectionEntity,
                track = trackEntity,
                albumTrackCrossRef = albumTrackCrossRef,
                clips = clipsAndAudioAssets,
                trackClipCrossRefs = crossRefs,
            )
        }
    }

    suspend fun updateTrackNumber(id: TrackId, trackNumber: Int?) {
        databaseTransactionRunner.run {
            albumTrackDao.updateTrackNumber(id.value, trackNumber)
        }
    }

    suspend fun updateTrackNotes(id: TrackId, notes: String?) {
        databaseTransactionRunner.run {
            val track = trackDao.getTrack(id.value) ?: return@run
            trackDao.update(track.copy(notes = notes))
            val mc = mediaCollectionDao.getMediaCollection(id.value) ?: return@run
            mediaCollectionDao.update(mc.copy(modifiedAt = Clock.System.now()))
        }
    }

    suspend fun delete(id: TrackId, deleteOrphanedClips: Boolean = true) {
        databaseTransactionRunner.run {
            deleteTrack(id.value, deleteOrphanedClips)
        }
    }
}
