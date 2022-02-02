package com.example.messenger_mvi.ui.screens.chat

import androidx.lifecycle.viewModelScope
import com.example.messenger_mvi.business.local.MessageUseCase
import com.example.messenger_mvi.business.model.chat.ChatEvent
import com.example.messenger_mvi.di.IODispatcher
import com.example.messenger_mvi.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class ChatViewModel @Inject constructor(
    messageUseCaseFactory: MessageUseCase.MessageUseCaseFactory,
    @IODispatcher private val coroutineContext: CoroutineContext,
) : BaseViewModel() {

    private val messageUseCase: MessageUseCase = messageUseCaseFactory.create(viewModelScope)

    val state = messageUseCase.state
    val action = messageUseCase.action

    init {
        sendEvent(ChatEvent.Launch)
    }

    private fun sendEvent(event: ChatEvent) {
        viewModelScope.launch(coroutineContext) {
            messageUseCase.sendEvent(event)
        }
    }

    fun sendMessage(value: String) {
        sendEvent(ChatEvent.Send(value))
    }

    fun deleteMessages() {
        sendEvent(ChatEvent.Delete)
    }

    fun showAuthorInfo(messageId: Long) {
        sendEvent(ChatEvent.ShowAuthorInfo(messageId))
    }

    fun hideAuthorInfo() {
        sendEvent(ChatEvent.HideAuthorInfo)
    }

    fun selectItem(messageId: Long) {
        sendEvent(ChatEvent.Select(messageId))
    }
}