package com.example.messenger_mvi.framework.mappers

import com.example.messenger_mvi.business.local.Storage
import com.example.messenger_mvi.business.mappers.MessageMapper
import com.example.messenger_mvi.business.model.message.MessageData
import com.example.messenger_mvi.database.entity.MessageEntity
import com.example.messenger_mvi.ui.models.MessageUI

private const val DEFAULT_ID = -1L

class MessageMapperImpl(
    private val storage: Storage,
) : MessageMapper {

    override fun map(value: MessageEntity): MessageUI =
        MessageUI(
            id = value.id ?: DEFAULT_ID,
            authorId = value.authorId,
            authorName = value.authorName,
            authorNameAlias = value.authorName.toAlias(),
            message = value.message,
            isMyOwn = value.isMyOwn,
        )

    override fun map(values: List<MessageEntity>): List<MessageUI> =
        values.map { map(it) }

    override fun mapToEntity(value: MessageData): MessageEntity =
        MessageEntity(
            id = value.id,
            authorId = value.authorId ?: DEFAULT_ID,
            authorName = value.authorName ?: "",
            message = value.message ?: "",
            isMyOwn = value.authorId == storage.userId,
        )

    override fun mapToEntity(values: List<MessageData>): List<MessageEntity> =
        values.map { mapToEntity(it) }

    private fun String.toAlias(): String =
        this.split(" ").let { list ->
            val first = if (list.isNotEmpty()) list.component1().take(1) else ""
            val second = if (list.size > 1) list.component2().take(1) else ""
            "${first.uppercase()}${second.uppercase()}"
        }
}