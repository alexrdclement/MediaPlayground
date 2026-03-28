package com.alexrdclement.media.ui.fakes

import com.alexrdclement.mediaplayground.media.session.MediaSessionEntry

class FakeMediaSessionEntry : MediaSessionEntry {
    override fun getServiceComponent() = throw NotImplementedError("Not implemented")
}
