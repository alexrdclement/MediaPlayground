package com.alexrdclement.mediaplayground.app.catalog

import com.alexrdclement.palette.components.layout.catalog.CatalogItem

enum class MainCatalogItem : CatalogItem {
    AudioLibrary,
    Camera,
    Player;

    override val title = this.name
}
