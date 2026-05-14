package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@Serializable
sealed class MediaAssetUri(val scheme: Scheme) {

    enum class Scheme(val value: String) {
        Shared("shared"),
        Album("album"),
        ;

        companion object {
            val values = entries.map { it.value }

            fun fromString(value: String): Scheme = entries.first { it.value == value }
        }
    }

    abstract fun toUriString(): String

    data class Shared(val fileName: String) : MediaAssetUri(scheme = Scheme.Shared) {
        override fun toUriString(): String = "${scheme.value}://$fileName"
    }

    data class Album(val albumId: AlbumId, val fileName: String) : MediaAssetUri(scheme = Scheme.Album) {
        override fun toUriString(): String = "${scheme.value}://${albumId.value}/$fileName"
    }

    companion object {
        private const val SchemeDelimeter = "://"
        fun fromUriString(value: String): MediaAssetUri = when (value.substringBefore(SchemeDelimeter)) {
            Scheme.Shared.value -> Shared(
                fileName = value.substringAfter(SchemeDelimeter),
            )
            Scheme.Album.value -> {
                val rest = value.substringAfter(SchemeDelimeter)
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
