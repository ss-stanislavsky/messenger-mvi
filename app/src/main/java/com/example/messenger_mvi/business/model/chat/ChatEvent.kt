package com.example.messenger_mvi.business.model.chat

sealed class ChatEvent {
    class Send(val message: String) : ChatEvent()
    object Launch : ChatEvent()
    class Select(val messageId: Long) : ChatEvent()
    object Delete : ChatEvent()
    class ShowAuthorInfo(val messageId: Long) : ChatEvent()
    object HideAuthorInfo : ChatEvent()
}