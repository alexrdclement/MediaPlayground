package com.alexrdclement.mediaplayground.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE tracks ADD COLUMN notes TEXT")
        db.execSQL("ALTER TABLE albums ADD COLUMN notes TEXT")
        db.execSQL("ALTER TABLE artists ADD COLUMN notes TEXT")
        db.execSQL("ALTER TABLE images ADD COLUMN notes TEXT")
    }
}
