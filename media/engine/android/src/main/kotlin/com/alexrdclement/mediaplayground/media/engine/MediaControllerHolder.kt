package com.alexrdclement.mediaplayground.media.engine

import android.content.Context
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.alexrdclement.mediaplayground.media.session.MediaSessionEntry
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class MediaControllerHolder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mediaSessionEntry: MediaSessionEntry,
) {
    private val mutex = Mutex()
    private var mediaController: MediaController? = null

    suspend fun getMediaController(): MediaController {
        return mutex.withLock {
            mediaController ?: createMediaController().also {
                this.mediaController = it
            }
        }
    }

    private suspend fun createMediaController(): MediaController {
        val sessionToken = SessionToken(context, mediaSessionEntry.getServiceComponent())
        return MediaController.Builder(context, sessionToken)
            .buildAsync()
            .await()
    }
}
