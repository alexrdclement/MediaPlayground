package com.alexrdclement.mediaplayground.data.audio

import com.alexrdclement.mediaplayground.data.audio.AudioRepository.Failure
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepository
import com.alexrdclement.mediaplayground.data.audio.util.awaitFirst
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Source
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.mapFailure
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor(
    private val localAudioRepository: LocalAudioRepository,
) : AudioRepository {

    override suspend fun getAlbum(id: AlbumId, source: Source?): Result<Album?, Failure> {
        if (source != null) {
            return getAlbum(id, source)
        }
        return coroutineScope {
            Source.entries.map {
                async {
                    getAlbum(id, it)
                }
            }.awaitFirst { it is Result.Success && it.value != null }
                ?: Result.Failure(Failure.Unexpected())
        }
    }

    override suspend fun getTrack(id: TrackId, source: Source?): Result<Track?, Failure> {
        if (source != null) {
            return getTrack(id, source)
        }
        return coroutineScope {
            Source.entries.map {
                async {
                    getTrack(id, it)
                }
            }.awaitFirst { it is Result.Success && it.value != null }
                ?: Result.Failure(Failure.Unexpected())
        }
    }

    @JvmName("getAlbumWithSource")
    private suspend fun getAlbum(id: AlbumId, source: Source): Result<Album?, Failure> {
        return coroutineScope {
            when (source) {
                Source.Local -> localAudioRepository.getAlbum(id)
                    .mapFailure(::mapLocalAudioRepositoryFailure)
            }
        }
    }

    @JvmName("getTrackWithSource")
    private suspend fun getTrack(id: TrackId, source: Source): Result<Track?, Failure> {
        return coroutineScope {
            when (source) {
                Source.Local -> localAudioRepository.getTrack(id)
                    .mapFailure(::mapLocalAudioRepositoryFailure)
            }
        }
    }

    private fun mapLocalAudioRepositoryFailure(
        failure: LocalAudioRepository.Failure
    ): Failure {
        return when (failure) {
            LocalAudioRepository.Failure.TrackNotFound -> Failure.Unexpected()
            LocalAudioRepository.Failure.AlbumNotFound -> Failure.Unexpected()
        }
    }
}
