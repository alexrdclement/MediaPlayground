package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio_files")
data class AudioFile(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "mime_type")
    val mimeType: String,
    val extension: String,
    @ColumnInfo(name = "file_name")
    val fileName: String,
    val source: Source,
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
