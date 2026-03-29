package com.alexrdclement.mediaplayground.feature.image.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.feature.image.ImageMetadataScreen
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavKey

fun EntryProviderScope<NavKey>.imageMetadataEntryProvider(
    navController: NavController,
) {
    entry<ImageMetadataRoute> { route ->
        ImageMetadataScreen(
            albumId = route.albumId,
            imageIndex = route.imageIndex,
            onNavigateBack = { navController.goBack() },
        )
    }
}
