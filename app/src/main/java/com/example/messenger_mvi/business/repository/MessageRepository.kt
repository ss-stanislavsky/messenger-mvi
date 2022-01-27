package com.example.messenger_mvi.business.repository

import com.example.messenger_mvi.business.model.message.MessageData
import com.example.messenger_mvi.ui.models.MessageUI
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    val messages: List<MessageUI>
    val messagesFlow: Flow<List<MessageUI>>
    suspend fun saveMessageToDB(value: MessageData)
    suspend fun sendMessage(value: String): Result<Unit>
    suspend fun loadMessages(): Result<Unit>
}