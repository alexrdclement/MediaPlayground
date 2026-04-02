package com.alexrdclement.mediaplayground.feature.image.navigation

import com.alexrdclement.mediaplayground.media.model.image.ImageId
import com.alexrdclement.palette.navigation.NavKey
import com.alexrdclement.palette.navigation.PathSegment
import com.alexrdclement.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface ImageNavRoute : NavKey

@Serializable
@SerialName("image-metadata")
data class ImageMetadataRoute(
    val imageIdValue: String,
) : ImageNavRoute {
    override val pathSegment: PathSegment = imageIdValue.toPathSegment()
    val imageId: ImageId get() = ImageId(imageIdValue)
}

@Serializable
@SerialName("image-delete")
data class ImageDeleteRoute(
    val imageIdValue: String,
    val displayName: String = "",
) : ImageNavRoute {
    override val pathSegment: PathSegment = imageIdValue.toPathSegment()
    val imageId: ImageId get() = ImageId(imageIdValue)
}

