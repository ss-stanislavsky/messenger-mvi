package com.example.messenger_mvi.ui.screens.chat

import androidx.lifecycle.viewModelScope
import com.example.messenger_mvi.business.local.MessageUseCase
import com.example.messenger_mvi.business.model.chat.ChatEvent
import com.example.messenger_mvi.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    messageUseCaseFactory: MessageUseCase.MessageUseCaseFactory
) : BaseViewModel() {

    private val messageUseCase: MessageUseCase = messageUseCaseFactory.create(viewModelScope)

    val state = messageUseCase.state

    private fun sendEvent(event: ChatEvent) {
        messageUseCase.sendEvent(event)
    }

    fun sendMessage(value: String) {
        sendEvent(ChatEvent.Send(value))
    }
}