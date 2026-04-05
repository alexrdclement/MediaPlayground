package com.alexrdclement.mediaplayground.data.media

import com.alexrdclement.mediaplayground.data.album.AlbumRepository
import com.alexrdclement.mediaplayground.data.track.TrackRepository
import com.alexrdclement.mediaplayground.media.engine.MediaItemRepository
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.MediaItem
import com.alexrdclement.mediaplayground.media.model.TrackId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class MediaItemRepositoryImpl @Inject constructor(
    private val trackRepository: TrackRepository,
    private val albumRepository: AlbumRepository,
) : MediaItemRepository {

    override fun getMediaItemFlow(id: String): Flow<MediaItem?> {
        return combine(
            trackRepository.getTrackFlow(TrackId(id)),
            albumRepository.getAlbumFlow(AlbumId(id)),
        ) { track, album ->
            track ?: album
        }
    }
}
