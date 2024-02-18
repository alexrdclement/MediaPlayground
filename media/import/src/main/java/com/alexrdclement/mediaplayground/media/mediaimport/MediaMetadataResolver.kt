package com.alexrdclement.mediaplayground.media.mediaimport

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MediaMetadataResolver @Inject constructor(
    @ApplicationContext context: Context
) {

    private companion object {
        const val ContentUriScheme = "content"
    }

    private val contentResolver = context.contentResolver

    fun getMediaMetadata(contentUri: Uri): MediaMetadata? {
        if (contentUri.scheme != ContentUriScheme) {
            return null
        }

        return contentResolver.query(
            contentUri,
            arrayOf(
                MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.TRACK,
                MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ALBUM,
            ),
            null,
            null,
            null,
        )?.use { cursor ->
            val displayName = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
            val idColumn = cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID)
            val titleColumn = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)
            val durationColumn = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)
            val trackNumberColumn = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TRACK)
            val artistIdColumn = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST_ID)
            val artistColumn = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)
            val albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID)
            val albumColumn = cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM)

            cursor.moveToNext()

            MediaMetadata(
                id = cursor.getLong(idColumn),
                title = cursor.getString(titleColumn) ?: cursor.getString(displayName),
                durationMs = cursor.getLong(durationColumn),
                trackNumber = cursor.getInt(trackNumberColumn),
                artistId = cursor.getLong(artistIdColumn),
                artistName = cursor.getString(artistColumn),
                albumId = cursor.getLong(albumIdColumn),
                albumTitle = cursor.getString(albumColumn)
            )
        }
    }
}
