package com.alexrdclement.mediaplayground.feature.artist.navigation

import com.alexrdclement.palette.navigation.NavKey
import com.alexrdclement.palette.navigation.PathSegment
import com.alexrdclement.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface ArtistNavRoute : NavKey

@Serializable
@SerialName("artist-metadata")
data class ArtistMetadataRoute(
    val artistIdValue: String,
) : ArtistNavRoute {
    override val pathSegment: PathSegment = artistIdValue.toPathSegment()
}
