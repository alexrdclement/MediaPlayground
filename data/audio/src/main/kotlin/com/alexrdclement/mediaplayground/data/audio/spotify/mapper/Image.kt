package com.alexrdclement.mediaplayground.data.audio.spotify.mapper

import com.adamratzman.spotify.models.SpotifyImage
import com.alexrdclement.mediaplayground.model.audio.Image

fun SpotifyImage.toImage() = Image(
    uri = this.url,
    heightPx = this.height,
    widthPx = this.width,
)
