package com.alexrdclement.mediaplayground.data.album.local

import com.alexrdclement.mediaplayground.data.album.AlbumRepository
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum

interface LocalAlbumRepository : AlbumRepository {
    suspend fun getAlbumByTitleAndArtistId(albumTitle: String, artistId: String): SimpleAlbum?
}
