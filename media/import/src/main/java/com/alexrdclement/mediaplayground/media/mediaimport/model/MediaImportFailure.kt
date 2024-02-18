package com.alexrdclement.mediaplayground.media.mediaimport.model

sealed class MediaImportFailure {
    data class Unknown(val throwable: Throwable? = null) : MediaImportFailure()
}
