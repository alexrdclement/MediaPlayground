package com.alexrdclement.mediaplayground.feature.error.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import com.alexrdclement.palette.components.layout.dialog.ErrorDialogContent
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

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
        )
    }
}
