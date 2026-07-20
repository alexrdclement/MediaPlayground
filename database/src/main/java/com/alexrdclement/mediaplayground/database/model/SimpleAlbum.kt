package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class SimpleAlbum(
    @Embedded
    val album: Album,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val mediaItem: MediaItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val mediaCollection: MediaCollection,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AlbumArtistCrossRef::class,
            parentColumn = "album_id",
            entityColumn = "artist_id",
        ),
    )
    val artists: List<Artist>,
    @Relation(
        entity = ImageAsset::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MediaItemImageCrossRef::class,
            parentColumn = "item_id",
            entityColumn = "image_asset_id",
        ),
    )
    val images: List<CompleteImageAsset>,
)
