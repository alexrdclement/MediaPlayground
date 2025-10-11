package com.alexrdclement.mediaplayground.media.session

import android.content.ComponentName

interface MediaSessionEntry {
    fun getServiceComponent(): ComponentName
}
