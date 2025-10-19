package com.alexrdclement.mediaplayground.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.log.LogLevel
import com.alexrdclement.log.Logger
import com.alexrdclement.uiplayground.uievent.toUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val logger: Logger,
) : ViewModel() {

    val errorMessages = logger.getLogFlow(level = LogLevel.Error)
        .map { it.message }
        .toUiEvent(viewModelScope)

    init {
        logToConsole()
    }

    private fun logToConsole() {
        viewModelScope.launch {
            logger.getLogFlow(level = LogLevel.Debug).collect { log ->
                when (log.level) {
                    LogLevel.Debug -> Log.d(log.tag, log.message)
                    LogLevel.Info -> Log.i(log.tag, log.message)
                    LogLevel.Warn -> Log.w(log.tag, log.message)
                    LogLevel.Error -> Log.e(log.tag, log.message)
                }
            }
        }
    }
}
