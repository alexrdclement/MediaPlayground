package com.alexrdclement.mediaplayground.data.album.local.mapper

import com.alexrdclement.mediaplayground.media.model.Source
import com.alexrdclement.mediaplayground.database.model.Source as DataSource

fun Source.toEntitySource(): DataSource {
    return when (this) {
        Source.Local -> DataSource.Local
    }
}

fun DataSource.toDomainSource(): Source {
    return when (this) {
        DataSource.Local -> Source.Local
    }
}
