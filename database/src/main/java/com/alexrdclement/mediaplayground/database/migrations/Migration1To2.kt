package com.alexrdclement.mediaplayground.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create album_images junction table
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS album_images (
                album_id TEXT NOT NULL,
                image_id TEXT NOT NULL,
                PRIMARY KEY (album_id, image_id),
                FOREIGN KEY (album_id) REFERENCES albums(id) ON UPDATE NO ACTION ON DELETE NO ACTION,
                FOREIGN KEY (image_id) REFERENCES images(id) ON UPDATE NO ACTION ON DELETE NO ACTION
            )
        """.trimIndent())
        db.execSQL("CREATE INDEX IF NOT EXISTS index_album_images_album_id ON album_images (album_id)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_album_images_image_id ON album_images (image_id)")

        // Populate album_images from images.album_id
        db.execSQL("INSERT INTO album_images (album_id, image_id) SELECT album_id, id FROM images")

        // Recreate images table without album_id
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS images_new (
                id TEXT NOT NULL PRIMARY KEY,
                file_name TEXT NOT NULL
            )
        """.trimIndent())
        db.execSQL("INSERT INTO images_new (id, file_name) SELECT id, file_name FROM images")
        db.execSQL("DROP TABLE images")
        db.execSQL("ALTER TABLE images_new RENAME TO images")
    }
}
