package com.example.messenger_mvi.business.local

import com.example.messenger_mvi.business.model.chat.ChatEvent
import com.example.messenger_mvi.business.model.chat.ChatState
import com.example.messenger_mvi.business.repository.MessageRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MessageUseCase @AssistedInject constructor(
    private val messageRepository: MessageRepository,
    @Assisted private val scope: CoroutineScope,
) {

    @AssistedFactory
    interface MessageUseCaseFactory {
        fun create(scope: CoroutineScope): MessageUseCase
    }

    private val _state = MutableStateFlow<ChatState>(ChatState.Loading(emptyList(), ""))
    val state: StateFlow<ChatState>
        get() = _state

    private val dbMessagesFlow = messageRepository.messagesFlow

    init {
        scope.launch(Dispatchers.IO) {
            dbMessagesFlow.collect {

            }
        }
    }

    fun sendEvent(value: ChatEvent) {
        reduce(_state.value, value)
    }

    private fun setState(value: ChatState) {
        scope.launch(Dispatchers.IO) {
            _state.emit(value)
        }
    }

    private fun reduce(oldState: ChatState, event: ChatEvent) {
        scope.launch(Dispatchers.IO) {
            when (event) {
                ChatEvent.Launch -> {
                    setState(ChatState.Loading(oldState.data, oldState.message))
                    messageRepository.loadMessages()
                        .onSuccess {
                            setState(ChatState.Data(messageRepository.messages, oldState.message))
                        }.onFailure {
                            setState(ChatState.Error(oldState.data, oldState.message, it.localizedMessage))
                        }
                }
                is ChatEvent.Send -> {
                    setState(ChatState.Sending(oldState.data, event.message))
                    messageRepository.sendMessage(event.message)
                        .onSuccess {
                            setState(ChatState.Data(messageRepository.messages, ""))
                        }.onFailure {
                            setState(ChatState.Error(oldState.data, oldState.message, it.localizedMessage))
                        }
                }
            }
        }
    }
}