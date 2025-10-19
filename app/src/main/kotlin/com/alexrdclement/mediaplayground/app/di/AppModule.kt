package com.alexrdclement.mediaplayground.app.di

import com.alexrdclement.log.Logger
import com.alexrdclement.log.LoggerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideCoroutineScope() = CoroutineScope(Dispatchers.Main)

    @Provides
    @Singleton
    fun provideLogger(
        coroutineScope: CoroutineScope,
    ): Logger = LoggerImpl(
        coroutineScope = coroutineScope,
    )
}
