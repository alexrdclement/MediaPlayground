package com.alexrdclement.mediaplayground.app.di

import com.alexrdclement.mediaplayground.feature.album.di.AlbumModule
import com.alexrdclement.mediaplayground.feature.artist.di.ArtistModule
import com.alexrdclement.mediaplayground.feature.audio.library.di.AudioLibraryModule
import com.alexrdclement.mediaplayground.feature.camera.di.CameraModule
import com.alexrdclement.mediaplayground.feature.image.di.ImageModule
import com.alexrdclement.mediaplayground.feature.image.library.di.ImageLibraryModule
import com.alexrdclement.mediaplayground.feature.media.control.di.MediaControlModule
import com.alexrdclement.mediaplayground.feature.playback.control.di.PlaybackControlModule
import com.alexrdclement.mediaplayground.feature.player.di.PlayerModule
import com.alexrdclement.mediaplayground.feature.track.di.TrackModule
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface FeatureModule :
    AlbumModule,
    ArtistModule,
    AudioLibraryModule,
    CameraModule,
    ImageModule,
    ImageLibraryModule,
    MediaControlModule,
    PlaybackControlModule,
    PlayerModule,
    TrackModule
