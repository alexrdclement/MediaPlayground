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

        db.execSQL("ALTER TABLE tracks RENAME TO tracks_old")
        db.execSQL("""
            CREATE TABLE tracks (
                id TEXT NOT NULL,
                file_name TEXT,
                title TEXT NOT NULL,
                album_id TEXT NOT NULL,
                duration_ms INTEGER NOT NULL,
                track_number INTEGER,
                modified_date TEXT NOT NULL,
                source TEXT NOT NULL,
                notes TEXT,
                PRIMARY KEY(id),
                FOREIGN KEY(album_id) REFERENCES albums(id) ON DELETE CASCADE
            )
        """.trimIndent())
        db.execSQL("INSERT INTO tracks SELECT * FROM tracks_old")
        db.execSQL("DROP TABLE tracks_old")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_tracks_album_id ON tracks (album_id)")

        db.execSQL("ALTER TABLE album_artists RENAME TO album_artists_old")
        db.execSQL("""
            CREATE TABLE album_artists (
                album_id TEXT NOT NULL,
                artist_id TEXT NOT NULL,
                PRIMARY KEY(album_id, artist_id),
                FOREIGN KEY(album_id) REFERENCES albums(id) ON DELETE CASCADE,
                FOREIGN KEY(artist_id) REFERENCES artists(id) ON DELETE CASCADE
            )
        """.trimIndent())
        db.execSQL("INSERT INTO album_artists SELECT * FROM album_artists_old")
        db.execSQL("DROP TABLE album_artists_old")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_album_artists_album_id ON album_artists (album_id)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_album_artists_artist_id ON album_artists (artist_id)")

        db.execSQL("ALTER TABLE album_images RENAME TO album_images_old")
        db.execSQL("""
            CREATE TABLE album_images (
                album_id TEXT NOT NULL,
                image_id TEXT NOT NULL,
                PRIMARY KEY(album_id, image_id),
                FOREIGN KEY(album_id) REFERENCES albums(id) ON DELETE CASCADE,
                FOREIGN KEY(image_id) REFERENCES images(id) ON DELETE CASCADE
            )
        """.trimIndent())
        db.execSQL("INSERT INTO album_images SELECT * FROM album_images_old")
        db.execSQL("DROP TABLE album_images_old")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_album_images_album_id ON album_images (album_id)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_album_images_image_id ON album_images (image_id)")
    }
}
