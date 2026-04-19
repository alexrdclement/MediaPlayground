package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@Serializable
sealed class MediaAssetUri {

    abstract fun toUriString(): String

    data class Shared(val fileName: String) : MediaAssetUri() {
        override fun toUriString(): String = "shared://$fileName"
    }

    data class Album(val albumId: AlbumId, val fileName: String) : MediaAssetUri() {
        override fun toUriString(): String = "album://${albumId.value}/$fileName"
    }

    companion object {
        fun fromUriString(value: String): MediaAssetUri = when {
            value.startsWith("shared://") -> Shared(
                fileName = value.removePrefix("shared://"),
            )
            value.startsWith("album://") -> {
                val rest = value.removePrefix("album://")
                val slashIndex = rest.indexOf('/')
                Album(
                    albumId = AudioAlbumId(rest.substring(0, slashIndex)),
                    fileName = rest.substring(slashIndex + 1),
                )
            }
            else -> error("Unknown MediaAssetUri scheme: $value")
        }
    }
}
