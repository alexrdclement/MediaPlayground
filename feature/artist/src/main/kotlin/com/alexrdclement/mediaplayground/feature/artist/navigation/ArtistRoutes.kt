package com.alexrdclement.mediaplayground.feature.artist.navigation

import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.PathSegment
import com.embarrasdf.palette.navigation.toPathSegment
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

@Serializable
@SerialName("artist-delete")
data class ArtistDeleteRoute(
    val artistIdValue: String,
    val displayName: String = "",
) : ArtistNavRoute {
    override val pathSegment: PathSegment = artistIdValue.toPathSegment()
}

