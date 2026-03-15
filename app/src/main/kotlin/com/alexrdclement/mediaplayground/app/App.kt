package com.alexrdclement.mediaplayground.app

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alexrdclement.mediaplayground.app.navigation.MediaPlaygroundNav
import com.alexrdclement.mediaplayground.app.navigation.rememberMediaPlaygroundNavController
import com.alexrdclement.palette.components.core.Surface
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.theme.PaletteTheme
import com.alexrdclement.uievent.UiEventState

@Composable
fun App(
    navController: NavController = rememberMediaPlaygroundNavController(),
) {
    val viewModel = hiltViewModel<AppViewModel>()
    App(
        navController = navController,
        errorMessages = viewModel.errorMessages,
    )
}

@Composable
fun App(
    navController: NavController = rememberMediaPlaygroundNavController(),
    errorMessages: UiEventState<String> = UiEventState(),
) {
    PaletteTheme {
        Surface {
            MediaPlaygroundNav(
                navController = navController,
                errorMessages = errorMessages,
            )
        }
    }
}
