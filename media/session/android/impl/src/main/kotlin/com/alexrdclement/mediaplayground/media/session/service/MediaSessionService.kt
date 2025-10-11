package com.alexrdclement.mediaplayground.media.session.service

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getActivity
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.alexrdclement.mediaplayground.media.engine.AndroidPlayerFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MediaSessionService @Inject constructor() : MediaSessionService() {

    @Inject
    lateinit var androidPlayerFactory: AndroidPlayerFactory

    private val sessionActivityPendingIntent: PendingIntent
        get() = getActivity(
            this,
            /* requestCode */ 0,
            packageManager.getLaunchIntentForPackage(packageName),
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT,
        )

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        mediaSession = createMediaSession()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    private fun createMediaSession(): MediaSession {
        return MediaSession.Builder(this, androidPlayerFactory.createPlayer())
            .setSessionActivity(sessionActivityPendingIntent)
            .build()
    }
}
