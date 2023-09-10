package com.alexrdclement.mediaplayground.data.audio.spotify.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore
import com.alexrdclement.mediaplayground.data.audio.spotify.mapper.toAlbum
import com.alexrdclement.mediaplayground.data.audio.spotify.mapper.toTrack
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track

class SpotifySavedAlbumsPagingSource(
    private val credentialStore: SpotifyDefaultCredentialStore,
) : PagingSource<Int, Album>() {

    // TODO: write suspending version of getter
    private val spotifyApi: SpotifyClientApi?
        get() = credentialStore.getSpotifyClientPkceApi()

    override fun getRefreshKey(state: PagingState<Int, Album>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        try {
            val spotifyApi = spotifyApi ?: return LoadResult.Invalid()

            val previousPage = params.key?.minus(1) ?: 0
            val nextPageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val offset = previousPage * pageSize
            val response = spotifyApi.library.getSavedAlbums(
                limit = pageSize,
                offset = offset,
            )
            return LoadResult.Page(
                data = response.items.map { it.album.toAlbum() },
                prevKey = null, // Only paging forward.
                nextKey = if (offset + response.items.size < response.total) {
                    nextPageNumber + 1
                } else {
                    null
                }
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
