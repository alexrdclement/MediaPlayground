package com.alexrdclement.mediaplayground.app.di

import com.alexrdclement.mediaplayground.data.audio.di.AudioBindingModule
import com.alexrdclement.mediaplayground.data.audio.local.di.LocalAudioBindingModule
import com.alexrdclement.mediaplayground.data.audio.local.di.PathProviderModule
import com.alexrdclement.mediaplayground.database.di.DaoModule
import com.alexrdclement.mediaplayground.database.di.DatabaseModule
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface DataModule :
    AudioBindingModule,
    LocalAudioBindingModule,
    PathProviderModule,
    DatabaseModule,
    DaoModule
