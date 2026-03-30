package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "file_name")
    val fileName: String,
)
