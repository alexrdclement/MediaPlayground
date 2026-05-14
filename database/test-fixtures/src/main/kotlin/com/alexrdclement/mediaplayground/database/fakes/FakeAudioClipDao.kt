package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.AudioClipDao
import com.alexrdclement.mediaplayground.database.model.AudioClip
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAudioClipDao : AudioClipDao {

    val audioClips = MutableStateFlow(emptySet<AudioClip>())

    override suspend fun insert(vararg audioClip: AudioClip) {
        for (newAudioClip in audioClip) {
            if (audioClips.value.any { it.id == newAudioClip.id }) continue
            audioClips.value += newAudioClip
        }
    }
}
