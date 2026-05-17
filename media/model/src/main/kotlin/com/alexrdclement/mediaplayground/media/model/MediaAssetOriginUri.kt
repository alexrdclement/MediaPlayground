package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@Serializable
sealed class MediaAssetOriginUri {
    abstract val value: String

    @Serializable
    data class AndroidContentUri(override val value: String) : MediaAssetOriginUri()
}
