package com.alexrdclement.mediaplayground.data.audio.local.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioDataStore
import com.alexrdclement.mediaplayground.model.audio.Album
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

class LocalAlbumPagingSource(
    private val localAudioDataStore: LocalAudioDataStore,
) : PagingSource<Int, Album>() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        coroutineScope.launch {
            localAudioDataStore.getAlbumsFlow()
                .drop(1)
                .collect {
                    invalidate()
                }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Album>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        return LoadResult.Page(
            data = localAudioDataStore.getAlbums(),
            prevKey = null, // Only paging forward.
            nextKey = null,
        )
    }
}
