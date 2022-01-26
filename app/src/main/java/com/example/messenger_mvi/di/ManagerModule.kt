package com.example.messenger_mvi.di

import android.app.Application
import com.example.messenger_mvi.framework.managers.ResourceManager
import com.example.messenger_mvi.framework.managers.ResourceManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ManagerModule {

    @Provides
    fun provideResourceManager(application: Application): ResourceManager = ResourceManagerImpl(application)
}