package com.alexrdclement.mediaplayground.app.di

import com.alexrdclement.mediaplayground.feature.album.di.AlbumModule
import com.alexrdclement.mediaplayground.feature.audio.library.di.AudioLibraryModule
import com.alexrdclement.mediaplayground.feature.camera.di.CameraModule
import com.alexrdclement.mediaplayground.feature.media.control.di.MediaControlModule
import com.alexrdclement.mediaplayground.feature.player.di.PlayerModule
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface FeatureModule :
    AlbumModule,
    AudioLibraryModule,
    CameraModule,
    MediaControlModule,
    PlayerModule
