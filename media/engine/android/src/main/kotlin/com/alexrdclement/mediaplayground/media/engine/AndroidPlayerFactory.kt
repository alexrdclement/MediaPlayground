package com.alexrdclement.mediaplayground.media.engine

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.WAKE_MODE_LOCAL
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class AndroidPlayerFactory @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun createPlayer(): Player {
        return ExoPlayer.Builder(context)
            .setAudioAttributes(AudioAttributes.DEFAULT, /* handleAudioFocus= */ true)
            .setHandleAudioBecomingNoisy(true)
            .setWakeMode(WAKE_MODE_LOCAL)
            .build()
    }
}
