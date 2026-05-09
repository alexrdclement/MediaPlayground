package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "track_clips",
    primaryKeys = ["track_id", "clip_id"],
    foreignKeys = [
        ForeignKey(
            entity = Track::class,
            parentColumns = ["id"],
            childColumns = ["track_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Clip::class,
            parentColumns = ["id"],
            childColumns = ["clip_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["track_id"]),
        Index(value = ["clip_id"]),
    ],
)
data class TrackClipCrossRef(
    @ColumnInfo(name = "track_id")
    val trackId: String,
    @ColumnInfo(name = "clip_id")
    val clipId: String,
    @ColumnInfo(name = "start_sample_in_track")
    val startSampleInTrack: Long,
)
