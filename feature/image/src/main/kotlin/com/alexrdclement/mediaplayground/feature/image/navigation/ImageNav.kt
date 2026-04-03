package com.alexrdclement.mediaplayground.feature.image.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import com.alexrdclement.mediaplayground.feature.image.ImageMetadataScreen
import com.alexrdclement.mediaplayground.feature.image.delete.ImageDeleteScreen
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

fun NavGraphBuilder.imageMetadataNavGraph() {
    wildcardRoute<ImageMetadataRoute> { pathSegment ->
        ImageMetadataRoute(imageIdValue = pathSegment.value)
    }
    wildcardRoute<ImageDeleteRoute> { pathSegment ->
        ImageDeleteRoute(imageIdValue = pathSegment.value)
    }
}

fun EntryProviderScope<NavKey>.imageMetadataEntryProvider(
    navController: NavController,
    onNavigateToImageDelete: (imageId: String, displayName: String) -> Unit = { _, _ -> },
) {
    entry<ImageMetadataRoute> { route ->
        ImageMetadataScreen(
            imageId = route.imageId,
            onNavigateBack = { navController.goBack() },
            onNavigateToDelete = { displayName -> onNavigateToImageDelete(route.imageId.value, displayName) },
        )
    }
    entry<ImageDeleteRoute>(metadata = DialogSceneStrategy.dialog()) { route ->
        ImageDeleteScreen(
            imageId = route.imageId,
            displayName = route.displayName,
            onNavigateBack = { navController.goBack() },
        )
    }
}
