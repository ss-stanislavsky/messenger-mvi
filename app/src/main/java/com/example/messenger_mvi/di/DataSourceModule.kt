package com.example.messenger_mvi.di

import com.example.messenger_mvi.business.local.MessageDataSource
import com.example.messenger_mvi.framework.local.MessageDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    fun provideMessageDataSource(): MessageDataSource = MessageDataSourceImpl()
}