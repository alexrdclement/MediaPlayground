package com.alexrdclement.mediaplayground.feature.image.navigation

import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.palette.navigation.NavKey
import com.alexrdclement.palette.navigation.PathSegment
import com.alexrdclement.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface ImageNavRoute : NavKey

@Serializable
@SerialName("image-metadata")
data class ImageMetadataRoute(
    val albumIdValue: String,
    val imageIndex: Int,
) : ImageNavRoute {
    override val pathSegment: PathSegment = "$albumIdValue-img$imageIndex".toPathSegment()
    val albumId: AlbumId get() = AlbumId(albumIdValue)
}
