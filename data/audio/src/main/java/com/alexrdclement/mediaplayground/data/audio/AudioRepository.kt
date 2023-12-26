package com.alexrdclement.mediaplayground.data.audio

import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result

interface AudioRepository {
    sealed class Failure {
        data object Timeout : Failure()
        data class Unexpected(val t: Throwable? = null) : Failure()
    }

    suspend fun getSavedTracks(): List<Track>
    suspend fun getSavedAlbums(): List<Album>
    suspend fun getAlbum(id: AlbumId): Result<Album?, Failure>
    suspend fun getTrack(id: TrackId): Result<Track?, Failure>
}
