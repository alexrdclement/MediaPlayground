package com.alexrdclement.mediaplayground.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alexrdclement.mediaplayground.app.navigation.MediaPlaygroundNav
import com.alexrdclement.mediaplayground.app.navigation.rememberMediaPlaygroundNavController
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.core.Surface
import com.embarrasdf.uievent.UiEventState
import dev.zacsweers.metrox.viewmodel.metroViewModel

@Composable
fun App(
    navController: NavController = rememberMediaPlaygroundNavController(),
    viewModel: AppViewModel = metroViewModel(),
) {
    App(
        navController = navController,
        errorMessages = viewModel.errorMessages,
    )
}

@Composable
fun App(
    modifier: Modifier = Modifier,
    navController: NavController = rememberMediaPlaygroundNavController(),
    errorMessages: UiEventState<String> = UiEventState(),
) {
    PaletteTheme {
        Surface(modifier = modifier) {
            MediaPlaygroundNav(
                navController = navController,
                errorMessages = errorMessages,
            )
        }
    }
}
