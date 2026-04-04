package com.alexrdclement.mediaplayground.feature.playback.control

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.playbackPitchState
import com.alexrdclement.mediaplayground.media.session.playbackRateState
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

    private val playbackRateState = mediaSessionState.playbackRateState
    private val playbackPitchState = mediaSessionState.playbackPitchState

    val rateControl = playbackRateState
        .map {
            PlaybackRateControl(
                value = it.speed,
                decrementEnabled = it.speed >= MIN_VALUE,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlaybackRateControl(),
        )

    val pitchControl = playbackPitchState
        .map {
            PlaybackPitchControl(
                value = it.pitch,
                decrementEnabled = it.pitch >= MIN_VALUE,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlaybackPitchControl(),
        )

    fun onRateDecrease() {
        viewModelScope.launch {
            val newValue = rateControl.value.decrementValue()
            mediaSessionControl.getMediaEngineControl().playbackRateControl.setSpeed(newValue)
        }
    }

    fun onRateIncrease() {
        viewModelScope.launch {
            val newValue = rateControl.value.incrementValue()
            mediaSessionControl.getMediaEngineControl().playbackRateControl.setSpeed(newValue)
        }
    }

    fun onPitchDecrease() {
        viewModelScope.launch {
            val newValue = pitchControl.value.decrementValue()
            mediaSessionControl.getMediaEngineControl().playbackPitchControl.setPitch(newValue)
        }
    }

    fun onPitchIncrease() {
        viewModelScope.launch {
            val newValue = pitchControl.value.incrementValue()
            mediaSessionControl.getMediaEngineControl().playbackPitchControl.setPitch(newValue)
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
