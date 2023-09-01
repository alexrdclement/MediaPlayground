package com.alexrdclement.mediaplayground.data.audio.spotify.mapper

import com.adamratzman.spotify.models.SpotifyImage
import com.alexrdclement.mediaplayground.data.audio.model.Image

fun SpotifyImage.toImage() = Image(
    url = this.url,
    heightPx = this.height,
    widthPx = this.width,
)
