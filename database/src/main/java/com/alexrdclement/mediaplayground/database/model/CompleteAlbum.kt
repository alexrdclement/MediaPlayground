package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CompleteAlbum(
    @Embedded
    val simpleAlbum: SimpleAlbum,
    @Relation(
        entity = Track::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AlbumTrackCrossRef::class,
            parentColumn = "album_id",
            entityColumn = "track_id",
        ),
    )
    val tracks: List<CompleteTrack>,
) {
    val orderedTracks: List<CompleteTrack>
        get() {
            val albumId = this.id
            return tracks.sortedBy { completeTrack ->
                completeTrack.albumRefs
                    .find { it.albumTrackCrossRef.albumId == albumId }
                    ?.albumTrackCrossRef?.trackNumber
            }
        }
}

val CompleteAlbum.id: String
    get() = simpleAlbum.album.id
