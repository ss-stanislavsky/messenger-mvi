package com.example.messenger_mvi.di

import com.example.messenger_mvi.business.local.Storage
import com.example.messenger_mvi.framework.local.StorageImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    fun provideStorage(): Storage = StorageImpl()
}