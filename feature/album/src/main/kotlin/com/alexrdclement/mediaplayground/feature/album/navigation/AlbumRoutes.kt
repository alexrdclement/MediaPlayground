package com.alexrdclement.mediaplayground.feature.album.navigation

import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.palette.navigation.NavKey
import com.alexrdclement.palette.navigation.PathSegment
import com.alexrdclement.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface AlbumNavRoute : NavKey

@Serializable
@SerialName("album")
data class AlbumRoute(
    val albumIdValue: String,
) : AlbumNavRoute {
    override val pathSegment: PathSegment = albumIdValue.toPathSegment()

    val albumId: AlbumId get() = AlbumId(albumIdValue)
}

@Serializable
@SerialName("album-metadata")
data class AlbumMetadataRoute(
    val albumIdValue: String,
) : AlbumNavRoute {
    override val pathSegment: PathSegment = albumIdValue.toPathSegment()
    val albumId: AlbumId get() = AlbumId(albumIdValue)
}
