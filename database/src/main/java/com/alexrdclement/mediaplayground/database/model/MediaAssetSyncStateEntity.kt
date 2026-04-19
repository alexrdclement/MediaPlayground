package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState

@Entity(
    tableName = "media_asset_sync_states",
    foreignKeys = [
        ForeignKey(
            entity = MediaAsset::class,
            parentColumns = ["id"],
            childColumns = ["media_asset_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class MediaAssetSyncStateEntity(
    @PrimaryKey
    @ColumnInfo(name = "media_asset_id")
    val mediaAssetId: String,
    @ColumnInfo(name = "sync_state")
    val syncState: MediaAssetSyncState,
)
