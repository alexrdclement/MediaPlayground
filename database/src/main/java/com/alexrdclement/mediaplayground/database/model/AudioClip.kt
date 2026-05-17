package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "audio_clips",
    foreignKeys = [
        ForeignKey(
            entity = Clip::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class AudioClip(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "start_sample_in_asset")
    val startSampleInAsset: Long,
    @ColumnInfo(name = "duration_samples")
    val durationSamples: Long,
)
