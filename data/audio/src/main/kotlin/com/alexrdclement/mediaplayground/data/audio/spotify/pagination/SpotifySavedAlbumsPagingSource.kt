package com.alexrdclement.mediaplayground.data.audio.spotify.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyRemoteDataStore
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.result.Result

class SpotifySavedAlbumsPagingSource(
    private val spotifyRemoteDataStore: SpotifyRemoteDataStore,
) : PagingSource<Int, Album>() {

    override fun getRefreshKey(state: PagingState<Int, Album>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        return try {
            val previousPage = params.key?.minus(1) ?: 0
            val nextPageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val offset = previousPage * pageSize
            val result = spotifyRemoteDataStore.getSavedAlbums(
                limit = pageSize,
                offset = offset,
            )
            when (result) {
                is Result.Failure -> LoadResult.Error(Throwable(result.toString()))
                is Result.Success -> {
                    val albums = result.value.items
                    LoadResult.Page(
                        data = albums,
                        prevKey = null, // Only paging forward.
                        nextKey = if (offset + albums.size < result.value.numTotalItems) {
                            nextPageNumber + 1
                        } else {
                            null
                        }
                    )
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
