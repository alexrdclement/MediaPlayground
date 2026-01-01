package com.alexrdclement.mediaplayground.app

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alexrdclement.mediaplayground.app.navigation.NavHost
import com.alexrdclement.uievent.UiEventState
import com.alexrdclement.palette.components.core.Surface
import com.alexrdclement.palette.theme.PaletteTheme

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
    PaletteTheme {
        Surface {
            NavHost(
                errorMessages = errorMessages,
            )
        }
    }
}
