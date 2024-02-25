package com.alexrdclement.mediaplayground.data.audio.spotify

import androidx.paging.PagingSource
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.result.Result

interface SpotifyAudioRepository {
    sealed class Failure {
        data object Timeout : Failure()
        data class Unexpected(val t: Throwable? = null) : Failure()
    }

    suspend fun getSavedTracks(): List<Track>
    fun getSavedTracksPagingSource(): PagingSource<Int, Track>
    suspend fun getSavedAlbums(): List<Album>
    fun getSavedAlbumsPagingSource(): PagingSource<Int, Album>
    suspend fun getAlbum(id: String): Result<Album?, Failure>
    suspend fun getTrack(id: String): Result<Track?, Failure>
}
