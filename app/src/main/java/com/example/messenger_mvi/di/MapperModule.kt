package com.example.messenger_mvi.di

import com.example.messenger_mvi.business.local.Storage
import com.example.messenger_mvi.business.mappers.MessageMapper
import com.example.messenger_mvi.framework.mappers.MessageMapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MapperModule {

    @Provides
    fun provideMessageMapper(storage: Storage): MessageMapper = MessageMapperImpl(storage)
}