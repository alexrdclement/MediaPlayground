package com.alexrdclement.mediaplayground.data.artist.local

import com.alexrdclement.mediaplayground.data.artist.ArtistRepository
import com.alexrdclement.mediaplayground.media.model.SimpleArtist

interface LocalArtistRepository : ArtistRepository {
    suspend fun getArtistByName(name: String): SimpleArtist?
}
