package com.example.messenger_mvi.framework.repository

import com.example.messenger_mvi.business.local.MessageDataSource
import com.example.messenger_mvi.business.mappers.MessageMapper
import com.example.messenger_mvi.business.model.message.MessageData
import com.example.messenger_mvi.business.repository.MessageRepository
import com.example.messenger_mvi.database.MessengerDatabase
import com.example.messenger_mvi.ui.models.MessageUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepositoryImpl(
    private val messageDataSource: MessageDataSource,
    private val database: MessengerDatabase,
    private val messageMapper: MessageMapper,
) : MessageRepository {

    override val messages: List<MessageUI>
        get() = messageMapper.map(database.messageDao.getAllMessages())

    override val messagesFlow: Flow<List<MessageUI>>
        get() = database.messageDao.getAllMessagesFlow().map {
            messageMapper.map(it)
        }

    override suspend fun saveMessageToDB(value: MessageData) {
        database.runInTransaction {
            database.messageDao.insert(messageMapper.mapToEntity(value))
        }
    }

    override suspend fun sendMessage(value: String): Result<Unit> =
        messageDataSource.sendMessage(value).onSuccess { it.save() }.mapResult()

    override suspend fun loadMessages(): Result<Unit> =
        messageDataSource.loadMessages().onSuccess { it.save() }.mapResult()

    override suspend fun deleteMessage(id: Long) {
        runDbTransaction {
            messageDao.deleteMessage(id)
        }
    }

    private fun <R> Result<R>.mapResult(): Result<Unit> =
        if (isSuccess) Result.success(Unit)
        else Result.failure(exceptionOrNull()!!)

    private fun MessageData.save() {
        runDbTransaction {
            messageDao.insert(messageMapper.mapToEntity(this@save))
        }
    }

    private fun List<MessageData>.save() {
        runDbTransaction {
            messageDao.insert(messageMapper.mapToEntity(this@save))
        }
    }

    private inline fun runDbTransaction(crossinline action: MessengerDatabase.() -> Unit) {
        with(database) {
            runInTransaction {
                action(this)
            }
        }
    }
}