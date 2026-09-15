package com.alexrdclement.mediaplayground.app

import androidx.compose.runtime.Composable
import com.alexrdclement.mediaplayground.app.navigation.MediaPlaygroundNav
import com.alexrdclement.mediaplayground.app.navigation.rememberMediaPlaygroundNavController
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.uievent.UiEventState
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun App(
    navController: NavController = rememberMediaPlaygroundNavController(),
) {
    val viewModel = metroViewModel<AppViewModel>()
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
        Surface(style = PaletteTheme.component.core.surface.default) {
            MediaPlaygroundNav(
                navController = navController,
                errorMessages = errorMessages,
            )
        }
    }
}
