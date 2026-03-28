package com.alexrdclement.mediaplayground.media.engine

import android.app.Application
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.alexrdclement.mediaplayground.media.session.MediaSessionEntry
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MediaControllerHolder @Inject constructor(
    private val application: Application,
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
        val sessionToken = SessionToken(application, mediaSessionEntry.getServiceComponent())
        return MediaController.Builder(application, sessionToken)
            .buildAsync()
            .await()
    }
}
