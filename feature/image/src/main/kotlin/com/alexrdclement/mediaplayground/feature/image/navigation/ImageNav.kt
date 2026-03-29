package com.alexrdclement.mediaplayground.feature.image.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.feature.image.ImageMetadataScreen
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

fun NavGraphBuilder.imageMetadataNavGraph() {
    wildcardRoute<ImageMetadataRoute> { pathSegment ->
        ImageMetadataRoute(imageIdValue = pathSegment.value)
    }
}

fun EntryProviderScope<NavKey>.imageMetadataEntryProvider(
    navController: NavController,
) {
    entry<ImageMetadataRoute> { route ->
        ImageMetadataScreen(
            imageId = route.imageId,
            onNavigateBack = { navController.goBack() },
        )
    }
}
