package com.alexrdclement.mediaplayground.data.audio.local.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioDataStore
import com.alexrdclement.mediaplayground.model.audio.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

// Temp until DB impl
class LocalTrackPagingSource(
    private val localAudioDataStore: LocalAudioDataStore,
) : PagingSource<Int, Track>() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        coroutineScope.launch {
            localAudioDataStore.getTracksFlow()
                .drop(1)
                .collect {
                    invalidate()
                }
        }
    }

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
