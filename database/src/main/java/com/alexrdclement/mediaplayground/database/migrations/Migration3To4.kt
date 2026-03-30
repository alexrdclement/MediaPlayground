package com.alexrdclement.mediaplayground.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE images ADD COLUMN width_px INTEGER")
        db.execSQL("ALTER TABLE images ADD COLUMN height_px INTEGER")
        db.execSQL("ALTER TABLE images ADD COLUMN date_time_original TEXT")
        db.execSQL("ALTER TABLE images ADD COLUMN gps_latitude REAL")
        db.execSQL("ALTER TABLE images ADD COLUMN gps_longitude REAL")
        db.execSQL("ALTER TABLE images ADD COLUMN camera_make TEXT")
        db.execSQL("ALTER TABLE images ADD COLUMN camera_model TEXT")
    }
}
