package com.example.messenger_mvi.di

import android.app.Application
import com.example.messenger_mvi.database.MessengerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDatabase(app: Application): MessengerDatabase = MessengerDatabase.create(app)
}