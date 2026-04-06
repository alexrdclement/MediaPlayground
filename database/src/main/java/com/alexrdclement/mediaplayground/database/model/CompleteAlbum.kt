package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteAlbum(
    @Embedded
    val simpleAlbum: SimpleAlbum,
    @Relation(
        entity = Track::class,
        parentColumn = "id",
        entityColumn = "album_id",
    )
    val tracks: List<CompleteTrack>,
) {
    val orderedTracks: List<CompleteTrack>
        get() = tracks.sortedBy { it.track.trackNumber }
}

val CompleteAlbum.id: String
    get() = simpleAlbum.album.id
