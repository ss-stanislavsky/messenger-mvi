package com.example.messenger_mvi.business.model.chat

sealed class ChatEvent {
    class Send(val message: String) : ChatEvent()
    object Launch : ChatEvent()
}