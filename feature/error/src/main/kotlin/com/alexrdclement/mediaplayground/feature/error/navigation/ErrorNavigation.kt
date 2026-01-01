package com.alexrdclement.mediaplayground.feature.error.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.alexrdclement.palette.components.layout.ErrorDialogContent

private const val ErrorRoute = "error"
internal const val ErrorMessageArgKey = "errorMessage"
const val ErrorRouteTemplate = "$ErrorRoute/{$ErrorMessageArgKey}"

fun createErrorRoute(errorMessage: String) = "$ErrorRoute/${errorMessage}"

fun NavController.navigateToError(
    errorMessage: String,
    navOptions: NavOptions? = null
) {
    navigate(createErrorRoute(errorMessage), navOptions)
}

fun NavGraphBuilder.errorDialog(
    navController: NavController,
) {
    dialog(
        route = ErrorRouteTemplate,
        arguments = listOf(
            navArgument(ErrorMessageArgKey) { type = NavType.StringType },
        ),
    ) {
        ErrorDialogContent(
            message = requireNotNull(it.arguments).getString(ErrorMessageArgKey) ?: "Unknown error",
            onDismissRequest = { navController.popBackStack() },
        )
    }
}
