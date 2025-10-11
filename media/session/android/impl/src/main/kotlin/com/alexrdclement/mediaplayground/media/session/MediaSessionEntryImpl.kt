package com.alexrdclement.mediaplayground.media.session

import android.content.ComponentName
import android.content.Context
import com.alexrdclement.mediaplayground.media.session.service.MediaSessionService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MediaSessionEntryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
): MediaSessionEntry {
    override fun getServiceComponent(): ComponentName {
        return ComponentName(context, MediaSessionService::class.java)
    }
}
