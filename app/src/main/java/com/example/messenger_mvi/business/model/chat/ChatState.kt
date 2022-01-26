package com.example.messenger_mvi.business.model.chat

import com.example.messenger_mvi.ui.models.MessageUI

data class ChatState(
    val data: List<MessageUI>,
    val message: String,
    val isSending: Boolean,
    val isLoading: Boolean,
) {
    companion object {
        fun initial() = ChatState(
            data = emptyList(),
            message = "",
            isSending = false,
            isLoading = false,
        )
    }
}