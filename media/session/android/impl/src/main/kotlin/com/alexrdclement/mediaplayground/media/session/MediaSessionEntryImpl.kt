package com.alexrdclement.mediaplayground.media.session

import android.app.Application
import android.content.ComponentName
import com.alexrdclement.mediaplayground.media.session.service.MediaSessionService
import dev.zacsweers.metro.Inject

class MediaSessionEntryImpl @Inject constructor(
    private val application: Application,
) : MediaSessionEntry {
    override fun getServiceComponent(): ComponentName {
        return ComponentName(application, MediaSessionService::class.java)
    }
}
