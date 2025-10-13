package com.alexrdclement.mediaplayground

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alexrdclement.mediaplayground.navigation.NavHost
import com.alexrdclement.uiplayground.components.core.Surface
import com.alexrdclement.uiplayground.theme.PlaygroundTheme
import com.alexrdclement.uiplayground.uievent.UiEventState

@Composable
fun App() {
    val viewModel = hiltViewModel<AppViewModel>()
    App(
        errorMessages = viewModel.errorMessages,
    )
}

@Composable
fun App(
    errorMessages: UiEventState<String> = UiEventState(),
) {
    PlaygroundTheme {
        Surface {
            NavHost(
                errorMessages = errorMessages,
            )
        }
    }
}
