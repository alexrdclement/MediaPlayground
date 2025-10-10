package com.alexrdclement.mediaplayground.media.engine

import android.content.ComponentName
import android.content.Context
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.alexrdclement.mediaplayground.media.session.service.MediaSessionService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.guava.await
import javax.inject.Inject

class MediaControllerHolder @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private var mediaController: MediaController? = null

    suspend fun getMediaController(): MediaController {
        return mediaController ?: createMediaSession().also {
            this.mediaController = it
        }
    }

    private suspend fun createMediaSession(): MediaController {
        val sessionToken = SessionToken(context, ComponentName(context, MediaSessionService::class.java))
        return MediaController.Builder(context, sessionToken)
            .buildAsync()
            .await()
    }
}
