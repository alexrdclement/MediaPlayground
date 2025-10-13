package com.alexrdclement.mediaplayground.data.audio.local.mapper

import com.alexrdclement.mediaplayground.model.audio.Source
import com.alexrdclement.mediaplayground.database.model.Source as DataSource

fun Source.toEntitySource(): DataSource {
    return when (this) {
        Source.Local -> DataSource.Local
        Source.Spotify -> DataSource.Spotify
    }
}

fun DataSource.toDomainSource(): Source {
    return when (this) {
        DataSource.Local -> Source.Local
        DataSource.Spotify -> Source.Spotify
    }
}
