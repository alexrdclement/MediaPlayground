package com.alexrdclement.mediaplayground.feature.error.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import com.embarrasdf.palette.components.layout.dialog.ErrorDialogContent
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.PaletteTheme

fun NavGraphBuilder.errorNavGraph() {
    route(ErrorGraph(""))
}

fun EntryProviderScope<NavKey>.errorEntryProvider(
    navController: NavController,
) {
    entry<ErrorGraph>(
        metadata = DialogSceneStrategy.dialog(),
    ) { route ->
        ErrorDialogContent(
            message = route.message,
            onDismissRequest = navController::goBack,
            style = PaletteTheme.component.layout.dialogContent,
        )
    }
}
