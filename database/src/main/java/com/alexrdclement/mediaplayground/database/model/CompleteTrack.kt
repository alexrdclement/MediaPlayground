package com.alexrdclement.mediaplayground.database.model

import androidx.room.DatabaseView

@DatabaseView("""
    SELECT 
        tracks.id,
        tracks.title,
        tracks.durationMs,
        tracks.trackNumber,
        tracks.uri,
        tracks.modifiedDate,
        albums.id AS albumId,
        albums.title AS albumTitle,
        artists.id AS artistId,
        artists.name AS artistName
    FROM tracks
    INNER JOIN albums ON tracks.albumId = albums.id
    INNER JOIN artists ON albums.artistId = artists.id
    ORDER BY tracks.modifiedDate DESC
""")
data class CompleteTrack(
    val id: String,
    val title: String,
    val durationMs: Int,
    val trackNumber: Int?,
    val uri: String?,
    val modifiedDate: Long,
    val albumId: String,
    val albumTitle: String,
    val artistId: String,
    val artistName: String,
)
