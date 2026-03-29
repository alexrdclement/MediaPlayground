package com.alexrdclement.mediaplayground.feature.track.navigation

import com.alexrdclement.palette.navigation.NavKey
import com.alexrdclement.palette.navigation.PathSegment
import com.alexrdclement.palette.navigation.toPathSegment
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
