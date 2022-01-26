package com.example.messenger_mvi.business.local

import com.example.messenger_mvi.R
import com.example.messenger_mvi.business.model.chat.ChatAction
import com.example.messenger_mvi.business.model.chat.ChatEvent
import com.example.messenger_mvi.business.model.chat.ChatState
import com.example.messenger_mvi.business.repository.MessageRepository
import com.example.messenger_mvi.framework.managers.ResourceManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MessageUseCase @AssistedInject constructor(
    private val messageRepository: MessageRepository,
    private val resourceManager: ResourceManager,
    @Assisted private val scope: CoroutineScope,
) {

    @AssistedFactory
    interface MessageUseCaseFactory {
        fun create(scope: CoroutineScope): MessageUseCase
    }

    private val _state = MutableStateFlow(ChatState.initial())
    val state: StateFlow<ChatState>
        get() = _state

    private val _action = MutableStateFlow<ChatAction>(ChatAction.Empty)
    val action: Flow<ChatAction>
        get() = _action

    private val dbMessagesFlow = messageRepository.messagesFlow

    private val errorLabel = resourceManager.getStringFromResource(android.R.string.ok)

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

    private fun setAction(value: ChatAction) {
        scope.launch(Dispatchers.IO) {
            _action.emit(value)
        }
    }

    private fun reduce(oldState: ChatState, event: ChatEvent) {
        scope.launch(Dispatchers.IO) {
            when (event) {
                ChatEvent.Launch -> {
                    setState(oldState.copy(isLoading = true))
                    messageRepository.loadMessages()
                        .onSuccess {
                            setState(oldState.copy(data = messageRepository.messages.asReversed(), isLoading = false))
                        }.onFailure {
                            setState(oldState.copy(isLoading = false))
                            setAction(ChatAction.Error(errorMessage = it.localizedMessage ?: "", errorLabel))
                        }
                }
                is ChatEvent.Send -> {
                    if (event.message.isBlank()) {
                        setAction(
                            ChatAction.Error(
                                errorMessage = resourceManager.getStringFromResource(R.string.enter_some_message),
                                errorLabel
                            )
                        )
                        return@launch
                    }
                    setState(oldState.copy(isSending = true))
                    messageRepository.sendMessage(event.message)
                        .onSuccess {
                            val data = messageRepository.messages.asReversed()
                            setState(oldState.copy(data = data, message = "", isSending = false))
                            setAction(ChatAction.Scroll(position = data.size))
                        }.onFailure {
                            setState(oldState.copy(isSending = false))
                            setAction(ChatAction.Error(errorMessage = it.localizedMessage ?: "", errorLabel))
                        }
                }
            }
        }
    }
}