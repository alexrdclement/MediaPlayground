package com.alexrdclement.mediaplayground.data.image.local

import android.net.Uri
import com.alexrdclement.mediaplayground.data.image.ImageRepository

interface LocalImageRepository : ImageRepository {
    suspend fun importImages(uris: List<Uri>)
}
