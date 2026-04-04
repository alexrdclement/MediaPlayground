package com.alexrdclement.mediaplayground.feature.playback.control

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.playbackState
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class PlaybackControlViewModel @Inject constructor(
    private val mediaSessionControl: MediaSessionControl,
    mediaSessionState: MediaSessionState,
) : ViewModel() {

    private val playbackState = mediaSessionState.playbackState

    val speedControl = playbackState
        .map {
            PlaybackControl.Speed(
                value = it.speed,
                decrementEnabled = it.speed >= MIN_VALUE,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlaybackControl.Speed(),
        )

    val pitchControl = playbackState
        .map {
            PlaybackControl.Pitch(
                value = it.pitch,
                decrementEnabled = it.pitch >= MIN_VALUE,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlaybackControl.Pitch(),
        )

    fun onSpeedDecrease() {
        viewModelScope.launch {
            val newValue = speedControl.value.decrementValue()
            mediaSessionControl.getMediaEngineControl().playbackControl.setSpeed(newValue)
        }
    }

    fun onSpeedIncrease() {
        viewModelScope.launch {
            val newValue = speedControl.value.incrementValue()
            mediaSessionControl.getMediaEngineControl().playbackControl.setSpeed(newValue)
        }
    }

    fun onPitchDecrease() {
        viewModelScope.launch {
            val newValue = pitchControl.value.decrementValue()
            mediaSessionControl.getMediaEngineControl().playbackControl.setPitch(newValue)
        }
    }

    fun onPitchIncrease() {
        viewModelScope.launch {
            val newValue = pitchControl.value.incrementValue()
            mediaSessionControl.getMediaEngineControl().playbackControl.setPitch(newValue)
        }
    }

    private fun NudgeControl.decrementValue() = (value - STEP).roundTo1Decimal().coerceAtLeast(MIN_VALUE)
    private fun NudgeControl.incrementValue() = (value + STEP).roundTo1Decimal()

    private fun Float.roundTo1Decimal(): Float = (this * 10).roundToInt() / 10f

    private companion object {
        private const val MIN_VALUE = 0.1f
        private const val STEP = 0.1f
    }
}
