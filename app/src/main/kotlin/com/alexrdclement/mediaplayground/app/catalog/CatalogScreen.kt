package com.alexrdclement.mediaplayground.app.catalog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.layout.Scaffold
import com.embarrasdf.palette.components.layout.TopBar
import com.embarrasdf.palette.components.layout.catalog.Catalog
import com.embarrasdf.palette.components.layout.catalog.CatalogItem
import com.embarrasdf.palette.components.navigation.BackNavigationButton
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.trace.ReportDrawn

@Composable
fun <T : CatalogItem> CatalogScreen(
    items: List<T>,
    onItemClick: (T) -> Unit,
    title: String? = null,
    onNavigateBack: (() -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
) {
    ReportDrawn()

    Scaffold(
        // Only show a top bar when there is something in it. An empty TopBar still reserves its
        // minimum height, which offsets the Scaffold's content padding and pushes the vertically
        // centered catalog off center.
        topBar = {
            if (title != null || onNavigateBack != null || actions != null) {
                TopBar(
                    title = title?.let {
                        { Text(title, style = PaletteTheme.component.core.text.titleMedium) }
                    },
                    navButton = onNavigateBack?.let {
                        { BackNavigationButton(onNavigateBack, style = PaletteTheme.component.navigation.backNavigationButton) }
                    },
                    actions = actions,
                    style = PaletteTheme.component.layout.topBar,
                )
            }
        },
        modifier = Modifier
            .safeDrawingPadding(),
        style = PaletteTheme.component.layout.scaffold,
    ) { innerPadding ->
        Catalog(
            items = items,
            onItemClick = onItemClick,
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = PaletteTheme.semantic.dimension.spacing.medium),
            style = PaletteTheme.component.layout.catalog,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        CatalogScreen(
            items = MainCatalogItem.entries.toList(),
            onItemClick = {}
        )
    }
}

@Preview
@Composable
private fun WithNavPreview() {
    PaletteTheme {
        CatalogScreen(
            items = MainCatalogItem.entries.toList(),
            onItemClick = {},
            title = "Components",
            onNavigateBack = {},
        )
    }
}

