package com.alexrdclement.mediaplayground.feature.image.library.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.feature.image.library.ImageLibraryScreen
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.palette.navigation.NavController
import com.alexrdclement.palette.navigation.NavGraphBuilder
import com.alexrdclement.palette.navigation.NavKey

fun NavGraphBuilder.imageLibraryNavGraph() {
    route(ImageLibraryGraph)
}

fun EntryProviderScope<NavKey>.imageLibraryEntryProvider(
    navController: NavController,
    onNavigateToImageMetadata: (ImageId) -> Unit = {},
    onNavigateToImageDelete: (imageId: String, displayName: String) -> Unit = { _, _ -> },
) {
    entry<ImageLibraryGraph> {
        ImageLibraryScreen(
            onNavigateToImageMetadata = { onNavigateToImageMetadata(ImageId(it)) },
            onNavigateToImageDelete = onNavigateToImageDelete,
        )
    }
}
