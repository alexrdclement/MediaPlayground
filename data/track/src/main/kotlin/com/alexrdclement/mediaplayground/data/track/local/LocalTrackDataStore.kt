package com.alexrdclement.mediaplayground.data.track.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.mapping.toAudioFileEntity
import com.alexrdclement.mediaplayground.database.mapping.toClipEntity
import com.alexrdclement.mediaplayground.database.mapping.toTrack
import com.alexrdclement.mediaplayground.database.mapping.toTrackEntity
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.deleteTrack
import com.alexrdclement.mediaplayground.database.transaction.insertTrack
import com.alexrdclement.mediaplayground.database.transaction.updateTrack
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class LocalTrackDataStore @Inject constructor(
    private val completeTrackDao: CompleteTrackDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    private val pathProvider: PathProvider,
) {

    fun getTrackCountFlow(): Flow<Int> {
        return completeTrackDao.getTrackCountFlow().distinctUntilChanged()
    }

    fun getTrackPagingData(config: PagingConfig): Flow<PagingData<Track>> {
        return Pager(config = config) {
            completeTrackDao.getTracksPagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.toTrack(
                    mediaItemDir = pathProvider.getAlbumDir(it.album.id),
                    imagesDir = pathProvider.getImagesDir(),
                )
            }
        }
    }

    suspend fun getTrack(trackId: TrackId): Track? {
        val entity = completeTrackDao.getTrack(trackId.value) ?: return null
        return entity.toTrack(
            mediaItemDir = pathProvider.getAlbumDir(entity.album.id),
            imagesDir = pathProvider.getImagesDir(),
        )
    }

    fun getTrackFlow(trackId: TrackId): Flow<Track?> {
        return completeTrackDao.getTrackFlow(trackId.value).map { completeTrack ->
            completeTrack?.toTrack(
                mediaItemDir = pathProvider.getAlbumDir(completeTrack.album.id),
                imagesDir = pathProvider.getImagesDir(),
            )
        }
    }

    suspend fun updateTrackTitle(id: TrackId, title: String) {
        databaseTransactionRunner.run {
            updateTrack(id.value, title)
        }
    }

    suspend fun put(track: Track) {
        val trackEntity = track.toTrackEntity()
        val clipsAndAudioFiles = track.clips.map { trackClip ->
            trackClip.clip.toClipEntity() to trackClip.clip.mediaAsset.toAudioFileEntity()
        }
        val crossRefs = track.clips.map { trackClip ->
            TrackClipCrossRef(
                trackId = track.id.value,
                clipId = trackClip.clip.id.value,
                startFrameInTrack = trackClip.startFrameInTrack,
            )
        }
        databaseTransactionRunner.run {
            insertTrack(
                track = trackEntity,
                clips = clipsAndAudioFiles,
                trackClipCrossRefs = crossRefs,
            )
        }
    }

    suspend fun updateTrackNumber(id: TrackId, trackNumber: Int?) {
        databaseTransactionRunner.run {
            val track = trackDao.getTrack(id.value) ?: return@run
            trackDao.update(track.copy(trackNumber = trackNumber))
        }
    }

    suspend fun updateTrackNotes(id: TrackId, notes: String?) {
        databaseTransactionRunner.run {
            val track = trackDao.getTrack(id.value) ?: return@run
            trackDao.update(track.copy(notes = notes))
        }
    }

    suspend fun delete(id: TrackId) {
        databaseTransactionRunner.run {
            deleteTrack(id.value)
        }
    }
}
