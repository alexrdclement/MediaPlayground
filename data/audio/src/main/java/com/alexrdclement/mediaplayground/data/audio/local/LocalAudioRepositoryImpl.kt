package com.alexrdclement.mediaplayground.data.audio.local

import android.content.Context
import android.net.Uri
import androidx.paging.PagingSource
import com.alexrdclement.mediaplayground.data.audio.local.pagination.LocalTrackPagingSource
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporter
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalAudioRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
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
                fileWriteDir = context.cacheDir,
            )
            when (result) {
                is Result.Failure -> {
                    when (val failure = result.failure) {
                        is MediaImportError.Unknown -> {
                            // TODO
                            failure.throwable?.let { throw it }
                        }
                        is MediaImportError.FileWriteError.Unknown -> {
                            // TODO
                            failure.throwable?.let { throw it }
                        }
                        MediaImportError.FileWriteError.InputStreamError,
                        MediaImportError.InputFileError -> {
                            TODO("InputFileError")
                        }
                    }
                }
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
}
