package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "audio_assets",
    foreignKeys = [
        ForeignKey(
            entity = MediaAsset::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class AudioAsset(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "duration_us")
    val durationUs: Long,
    @ColumnInfo(name = "sample_rate")
    val sampleRate: Int,
    @ColumnInfo(name = "channel_count")
    val channelCount: Int,
    @ColumnInfo(name = "bit_rate")
    val bitRate: Int,
    @ColumnInfo(name = "bit_depth")
    val bitDepth: Int,
)
