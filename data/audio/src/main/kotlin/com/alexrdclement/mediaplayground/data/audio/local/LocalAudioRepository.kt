package com.alexrdclement.mediaplayground.data.audio.local

import android.net.Uri
import androidx.paging.PagingSource
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.flow.Flow

interface LocalAudioRepository {
    sealed class Failure {
        data object TrackNotFound : Failure()
    }

    fun importTrackFromDisk(uri: Uri)
    fun getTracks(): Flow<List<Track>>
    fun getTrackPagingSource(): PagingSource<Int, Track>
    suspend fun getTrack(id: TrackId): Result<Track?, Failure>
}
