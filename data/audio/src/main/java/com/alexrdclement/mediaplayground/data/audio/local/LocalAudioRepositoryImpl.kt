package com.alexrdclement.mediaplayground.data.audio.local

import android.net.Uri
import androidx.paging.PagingSource
import com.alexrdclement.mediaplayground.data.audio.local.pagination.LocalTrackPagingSource
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportFailure
import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporter
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalAudioRepositoryImpl @Inject constructor(
    private val mediaImporter: MediaImporter,
    private val localAudioDataStore: LocalAudioDataStore,
): LocalAudioRepository {

    override fun importTrackFromDisk(uri: Uri) {
        when (val result = mediaImporter.importTrackFromDisk(uri)) {
            is Result.Failure -> {
                when (val failure = result.failure) {
                    is MediaImportFailure.Unknown -> {
                        // TODO
                        failure.throwable?.let { throw it }
                    }
                }
            }
            is Result.Success -> {
                localAudioDataStore.putTrack(result.value)
            }
        }
    }

    override fun getTracks(): Flow<List<Track>> {
        return localAudioDataStore.getTracksFlow()
    }

    override fun getTrackPagingSource(): PagingSource<Int, Track> {
        return LocalTrackPagingSource(localAudioDataStore)
    }

    override suspend fun getTrack(id: TrackId): Result<Track?, LocalAudioRepository.Failure> {
        val track = localAudioDataStore.getTrack(id)
        return if (track == null) {
            Result.Failure(LocalAudioRepository.Failure.TrackNotFound)
        } else {
            Result.Success(track)
        }
    }
}
