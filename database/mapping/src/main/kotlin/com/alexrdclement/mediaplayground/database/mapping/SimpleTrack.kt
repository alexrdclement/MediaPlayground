package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.SimpleTrack
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.io.files.Path
import com.alexrdclement.mediaplayground.database.model.CompleteTrack as CompleteTrackEntity

fun CompleteTrackEntity.toSimpleTrack(
    mediaItemDir: Path,
    Artists: PersistentList<Artist>,
    images: PersistentList<Image>,
): SimpleTrack {
    return SimpleTrack(
        id = TrackId(track.id),
        name = track.title,
        artists = Artists,
        trackNumber = track.trackNumber,
        clips = clips.map { it.toTrackClip(mediaItemDir, Artists, images) }.toPersistentSet(),
    )
}
