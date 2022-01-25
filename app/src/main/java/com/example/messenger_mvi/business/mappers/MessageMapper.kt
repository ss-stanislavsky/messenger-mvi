package com.example.messenger_mvi.business.mappers

import com.example.messenger_mvi.business.model.message.MessageData
import com.example.messenger_mvi.database.entity.MessageEntity
import com.example.messenger_mvi.ui.models.MessageUI

interface MessageMapper {
    fun map(value: MessageEntity): MessageUI
    fun map(values: List<MessageEntity>): List<MessageUI>

    fun mapToEntity(value: MessageData): MessageEntity
    fun mapToEntity(values: List<MessageData>): List<MessageEntity>
}