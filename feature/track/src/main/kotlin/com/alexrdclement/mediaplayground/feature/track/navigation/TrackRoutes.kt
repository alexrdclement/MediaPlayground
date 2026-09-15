package com.alexrdclement.mediaplayground.feature.track.navigation

import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.navigation.PathSegment
import com.embarrasdf.palette.navigation.toPathSegment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface TrackNavRoute : NavKey

@Serializable
@SerialName("track-metadata")
data class TrackMetadataRoute(
    val trackIdValue: String,
) : TrackNavRoute {
    override val pathSegment: PathSegment = trackIdValue.toPathSegment()
}

@Serializable
@SerialName("track-delete")
data class TrackDeleteRoute(
    val trackIdValue: String,
    val displayName: String = "",
) : TrackNavRoute {
    override val pathSegment: PathSegment = trackIdValue.toPathSegment()
}
