package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import kotlin.time.Instant

@Entity(
    tableName = "media_assets",
    foreignKeys = [
        ForeignKey(
            entity = MediaItem::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["uri"], unique = true)],
)
data class MediaAsset(
    @PrimaryKey
    val id: String,
    val uri: MediaAssetUri,
    @ColumnInfo(name = "origin_uri")
    val originUri: MediaAssetOriginUri,
    @ColumnInfo(name = "media_type")
    val mediaAssetType: MediaAssetType,
    @ColumnInfo(name = "file_name")
    val fileName: String,
    @ColumnInfo(name = "mime_type")
    val mimeType: String,
    val extension: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "modified_at")
    val modifiedAt: Instant,
)
