package com.alexrdclement.mediaplayground.mediasession.service

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getActivity
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.WAKE_MODE_LOCAL
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MediaSessionService @Inject constructor() : MediaSessionService() {

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
        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, /* handleAudioFocus= */ true)
            .setHandleAudioBecomingNoisy(true)
            .setWakeMode(WAKE_MODE_LOCAL)
            .build()
        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(sessionActivityPendingIntent)
            .build()
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
}
