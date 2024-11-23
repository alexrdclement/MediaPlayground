package com.alexrdclement.mediaplayground.data.audio.local

import android.net.Uri
import androidx.paging.PagingSource
import com.alexrdclement.mediaplayground.data.audio.local.pagination.LocalTrackPagingSource
import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporter
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalAudioRepositoryImpl @Inject constructor(
    private val pathProvider: PathProvider,
    private val mediaImporter: MediaImporter,
    private val localAudioDataStore: LocalAudioDataStore,
): LocalAudioRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var importJob: Job? = null

    override fun importTrackFromDisk(uri: Uri) {
        importJob?.cancel()
        importJob = coroutineScope.launch {
            val result = mediaImporter.importTrackFromDisk(
                uri = uri,
                fileWriteDir = pathProvider.trackImportFileWriteDir,
            )
            when (result) {
                is Result.Failure -> onMediaImportFailure(result.failure)
                is Result.Success -> {
                    localAudioDataStore.putTrack(result.value)
                }
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

    private fun onMediaImportFailure(error: MediaImportError) = when (error) {
        MediaImportError.MkdirError -> TODO("MkdirError")
        MediaImportError.InputFileError -> {
            TODO("InputFileError")
        }
        is MediaImportError.FileWriteError.InputFileNotFound -> TODO("InputFileNotFound")
        MediaImportError.FileWriteError.InputStreamError -> TODO("InputStreamError")
        is MediaImportError.FileWriteError.Unknown -> {
            // TODO
            error.throwable?.let { throw it }
        }
        is MediaImportError.Unknown -> {
            // TODO
            error.throwable?.let { throw it }
        }
    }
}
