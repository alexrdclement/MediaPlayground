package com.alexrdclement.mediaplayground.database.di

import android.content.Context
import androidx.room.Room
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun provideMediaPlaygroundDatabase(
        @ApplicationContext context: Context,
    ): MediaPlaygroundDatabase = Room.databaseBuilder(
        context,
        MediaPlaygroundDatabase::class.java,
        "mediaplayground-database",
    ).build()
}
