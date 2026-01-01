package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.logging.Loggable

sealed class PlaylistError(
    override val message: String,
    override val cause: Throwable? = null,
) : Throwable(), Loggable {

    data class IndexOutOfBounds(
        val index: Int,
        val size: Int,
    ) : PlaylistError(
        message = "Index $index is out of bounds for playlist of size $size",
    )

    override val throwable: Throwable?
        get() = this
}
