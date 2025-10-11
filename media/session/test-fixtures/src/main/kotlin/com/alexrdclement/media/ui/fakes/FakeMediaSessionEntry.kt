package com.alexrdclement.media.ui.fakes

import com.alexrdclement.mediaplayground.media.session.MediaSessionEntry
import javax.inject.Inject

class FakeMediaSessionEntry @Inject constructor() : MediaSessionEntry {
    override fun getServiceComponent() = throw NotImplementedError("Not implemented")
}
