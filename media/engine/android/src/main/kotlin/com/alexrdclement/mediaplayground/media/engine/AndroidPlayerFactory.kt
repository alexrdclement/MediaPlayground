package com.alexrdclement.mediaplayground.media.engine

import android.app.Application
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.WAKE_MODE_LOCAL
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dev.zacsweers.metro.Inject

class AndroidPlayerFactory @Inject constructor(
    private val application: Application,
) {
    fun createPlayer(): Player {
        return ExoPlayer.Builder(application)
            .setAudioAttributes(AudioAttributes.DEFAULT, /* handleAudioFocus= */ true)
            .setHandleAudioBecomingNoisy(true)
            .setWakeMode(WAKE_MODE_LOCAL)
            .build()
    }
}
