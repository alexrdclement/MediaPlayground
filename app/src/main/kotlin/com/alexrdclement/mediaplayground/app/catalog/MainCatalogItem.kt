package com.alexrdclement.mediaplayground.app.catalog

import com.alexrdclement.uiplayground.components.layout.catalog.CatalogItem

enum class MainCatalogItem : CatalogItem {
    AudioLibrary,
    SpotifyLibrary,
    Player;

    override val title = this.name
}
