package com.alexrdclement.mediaplayground.database.di

import android.app.Application
import androidx.room.Room
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.migrations.MIGRATION_1_2
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunnerImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
interface DatabaseModule {
    companion object {
        @Provides
        @SingleIn(AppScope::class)
        fun provideMediaPlaygroundDatabase(
            application: Application,
        ): MediaPlaygroundDatabase = Room.databaseBuilder(
            application,
            MediaPlaygroundDatabase::class.java,
            "mediaplayground-database",
        ).addMigrations(MIGRATION_1_2)
            .build()

        @Provides
        fun provideDatabaseTransactionRunner(
            database: MediaPlaygroundDatabase,
        ): DatabaseTransactionRunner = DatabaseTransactionRunnerImpl(database)
    }
}
