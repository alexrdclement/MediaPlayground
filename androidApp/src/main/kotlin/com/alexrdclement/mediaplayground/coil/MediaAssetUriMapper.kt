package com.alexrdclement.mediaplayground.coil

import coil3.map.Mapper
import coil3.request.Options
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri

class MediaAssetUriMapper : Mapper<String, MediaAssetUri> {
    override fun map(data: String, options: Options): MediaAssetUri? {
        val scheme = data.substringBefore("://", missingDelimiterValue = "")
        if (scheme !in MediaAssetUri.Scheme.values) return null
        return try {
            MediaAssetUri.fromUriString(data)
        } catch (e: Exception) {
            null
        }
    }
}
