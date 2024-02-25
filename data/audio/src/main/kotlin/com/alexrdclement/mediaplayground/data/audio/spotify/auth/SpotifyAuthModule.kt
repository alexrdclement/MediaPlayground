package com.alexrdclement.mediaplayground.data.audio.spotify.auth

import android.content.Context
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SpotifyAuthModule {
    @Provides
    fun provideSpotifyDefaultCredentialStore(
        @ApplicationContext context: Context,
    ): SpotifyDefaultCredentialStore {
        return SpotifyDefaultCredentialStore(
            clientId = ClientId,
            redirectUri = RedirectUri,
            applicationContext = context,
        )
    }
}
