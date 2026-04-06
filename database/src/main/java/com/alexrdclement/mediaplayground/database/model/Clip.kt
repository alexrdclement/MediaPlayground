package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "clips",
    foreignKeys = [
        ForeignKey(
            entity = AudioFile::class,
            parentColumns = ["id"],
            childColumns = ["audio_file_id"],
            onDelete = ForeignKey.NO_ACTION,
        ),
    ],
    indices = [
        Index(value = ["audio_file_id"]),
    ],
)
data class Clip(
    @PrimaryKey
    val id: String,
    val title: String,
    @ColumnInfo(name = "audio_file_id")
    val assetId: String,
    @ColumnInfo(name = "start_frame_in_file")
    val startFrameInFile: Long,
    @ColumnInfo(name = "end_frame_in_file")
    val endFrameInFile: Long,
)
