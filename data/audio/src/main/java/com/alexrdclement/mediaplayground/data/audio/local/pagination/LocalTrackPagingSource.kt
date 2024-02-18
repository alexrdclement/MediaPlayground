package com.alexrdclement.mediaplayground.data.audio.local.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioDataStore
import com.alexrdclement.mediaplayground.data.audio.spotify.mapper.toAlbum
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track

// Temp until Room impl
class LocalTrackPagingSource(
    private val localAudioDataStore: LocalAudioDataStore,
) : PagingSource<Int, Track>() {

    override fun getRefreshKey(state: PagingState<Int, Track>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Track> {
        return LoadResult.Page(
            data = localAudioDataStore.getTracks(),
            prevKey = null, // Only paging forward.
            nextKey = null,
        )
    }
}
