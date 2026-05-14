package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CompleteAudioClip(
    @Embedded
    val clip: Clip,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val audioClip: AudioClip,
    @Relation(
        parentColumn = "asset_id",
        entityColumn = "id",
    )
    val audioAsset: AudioAsset,
    @Relation(
        parentColumn = "asset_id",
        entityColumn = "id",
    )
    val mediaAsset: MediaAsset,
    @Relation(
        entity = Artist::class,
        parentColumn = "asset_id",
        entityColumn = "id",
        associateBy = Junction(
            value = AudioAssetArtistCrossRef::class,
            parentColumn = "audio_asset_id",
            entityColumn = "artist_id",
        ),
    )
    val artists: List<Artist>,
    @Relation(
        entity = ImageAsset::class,
        parentColumn = "asset_id",
        entityColumn = "id",
        associateBy = Junction(
            value = AudioAssetImageCrossRef::class,
            parentColumn = "audio_asset_id",
            entityColumn = "image_id",
        ),
    )
    val images: List<CompleteImageAsset>,
)
