package com.example.messenger_mvi.business.local

import com.example.messenger_mvi.business.model.message.MessageData

interface MessageDataSource {
    suspend fun sendMessage(value: String): Result<MessageData>
    suspend fun loadMessages(): Result<List<MessageData>>
}