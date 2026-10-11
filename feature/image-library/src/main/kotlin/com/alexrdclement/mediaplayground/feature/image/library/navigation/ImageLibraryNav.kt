package com.alexrdclement.mediaplayground.feature.image.library.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.alexrdclement.mediaplayground.feature.image.library.ImageLibraryScreen
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey

fun NavGraphBuilder.imageLibraryNavGraph() {
    route(ImageLibraryGraph)
}

fun EntryProviderScope<NavKey>.imageLibraryEntryProvider(
    onNavigateToImageMetadata: (ImageId) -> Unit = {},
    onNavigateToImageDelete: (imageId: String, displayName: String) -> Unit = { _, _ -> },
) {
    entry<ImageLibraryGraph> {
        ImageLibraryScreen(
            onNavigateToImageMetadata = { imageId -> onNavigateToImageMetadata(ImageId(imageId)) },
            onNavigateToImageDelete = onNavigateToImageDelete,
        )
    }
}
