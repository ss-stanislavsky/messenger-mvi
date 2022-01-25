package com.example.messenger_mvi.framework.local

import com.example.messenger_mvi.business.local.MessageDataSource
import com.example.messenger_mvi.business.model.message.MessageData
import com.example.messenger_mvi.random
import kotlinx.coroutines.delay

class MessageDataSourceImpl : MessageDataSource {

    override suspend fun sendMessage(value: String): Result<MessageData> {
        delay(3000)
        return if (random(10) % 10 != 0) {
            val author = Users.getById(random(5).toLong())
            val data = MessageData(
                id = null,
                message = value,
                authorId = author.userId,
                authorName = author.userName
            )
            Result.success(data)
        } else {
            Result.failure(Throwable("Error send message"))
        }
    }

    override suspend fun loadMessages(): Result<List<MessageData>> {
        delay(2000)
        return Result.success(emptyList())
    }
}

private enum class Users(val userId: Long, val userName: String) {
    First(0, "Stanislav Burdovitsyn"),
    Second(1, "Sergey Yaskevich"),
    Third(2, "Vladimir Erokhin"),
    Fourth(3, "Vyacheslav Merkulov"),
    Fifth(4, "Karina Lysenko");

    companion object {
        fun getById(value: Long): Users = values().first { it.userId == value }
    }
}