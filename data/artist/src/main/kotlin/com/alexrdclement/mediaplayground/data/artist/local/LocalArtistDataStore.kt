package com.alexrdclement.mediaplayground.data.artist.local

import com.alexrdclement.mediaplayground.database.dao.AlbumArtistDao
import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.mapping.toArtist
import com.alexrdclement.mediaplayground.database.mapping.toArtistEntity
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.deleteArtist
import com.alexrdclement.mediaplayground.database.transaction.insertArtist
import com.alexrdclement.mediaplayground.database.transaction.updateArtist
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId
import dev.zacsweers.metro.Inject
import kotlin.time.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalArtistDataStore @Inject constructor(
    private val artistDao: ArtistDao,
    private val albumArtistDao: AlbumArtistDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
) {
    suspend fun getArtist(artistId: ArtistId): Artist? {
        return artistDao.getArtist(artistId.value)?.toArtist()
    }

    fun getArtistFlow(artistId: ArtistId): Flow<Artist?> {
        return artistDao.getArtistFlow(artistId.value).map { it?.toArtist() }
    }

    suspend fun getArtistAlbumCount(artistId: ArtistId): Int {
        return albumArtistDao.getArtistAlbums(artistId.value).count()
    }

    suspend fun getArtistByName(name: String): Artist? {
        return artistDao.getArtistByName(name)?.toArtist()
    }

    suspend fun put(artist: Artist) {
        databaseTransactionRunner.run {
            insertArtist(artist.toArtistEntity())
        }
    }

    suspend fun updateArtistName(
        artistId: ArtistId,
        name: String?,
    ) = databaseTransactionRunner.run {
        val artist = artistDao.getArtist(artistId.value) ?: return@run
        updateArtist(artist = artist.copy(name = name, modifiedAt = Clock.System.now()))
    }

    suspend fun updateArtistNotes(
        artistId: ArtistId,
        notes: String?
    ) = databaseTransactionRunner.run {
        val artist = artistDao.getArtist(artistId.value) ?: return@run
        updateArtist(artist = artist.copy(notes = notes, modifiedAt = Clock.System.now()))
    }

    suspend fun delete(artistId: ArtistId) = databaseTransactionRunner.run {
        deleteArtist(id = artistId.value)
    }
}
