package com.alexrdclement.mediaplayground.data.audio.spotify.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyRemoteDataStore
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.result.Result

class SpotifySavedTracksPagingSource(
    private val spotifyRemoteDataStore: SpotifyRemoteDataStore,
) : PagingSource<Int, Track>() {

    override fun getRefreshKey(state: PagingState<Int, Track>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Track> {
        try {
            val previousPage = params.key?.minus(1) ?: 0
            val nextPageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val offset = previousPage * pageSize
            val result = spotifyRemoteDataStore.getSavedTracks(
                limit = pageSize,
                offset = offset,
            )
            return when (result) {
                is Result.Failure -> LoadResult.Error(Exception(result.toString()))
                is Result.Success -> LoadResult.Page(
                    data = result.value.items,
                    prevKey = null, // Only paging forward.
                    nextKey = if (offset + result.value.items.size < result.value.numTotalItems) {
                        nextPageNumber + 1
                    } else {
                        null
                    }
                )
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
