package com.example.messenger_mvi.di

import com.example.messenger_mvi.business.repository.MessageRepository
import com.example.messenger_mvi.framework.repository.MessageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideMessageRepository(): MessageRepository = MessageRepositoryImpl()
}