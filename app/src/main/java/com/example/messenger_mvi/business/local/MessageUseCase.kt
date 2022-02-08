package com.example.messenger_mvi.business.local

import com.example.messenger_mvi.R
import com.example.messenger_mvi.business.model.chat.ChatAction
import com.example.messenger_mvi.business.model.chat.ChatEvent
import com.example.messenger_mvi.business.model.chat.ChatState
import com.example.messenger_mvi.business.repository.MessageRepository
import com.example.messenger_mvi.framework.managers.ResourceManager
import com.example.messenger_mvi.ui.models.MessageUI
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class MessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
    private val resourceManager: ResourceManager,
) {

    private val _state = MutableStateFlow(ChatState.initial())
    val state: StateFlow<ChatState>
        get() = _state

    private val oldState: ChatState
        get() = _state.value

    private val _action = Channel<ChatAction>(Channel.BUFFERED)
    val action: Flow<ChatAction>
        get() = _action.receiveAsFlow()

    private val messages: List<MessageUI>
        get() = messageRepository.messages.asReversed()

    private val errorLabel = resourceManager.getStringFromResource(android.R.string.ok)

    suspend fun sendEvent(value: ChatEvent) {
        reduce(value)
    }

    private suspend fun setState(value: ChatState) {
        _state.emit(value)
    }

    private suspend fun setAction(value: ChatAction) {
        _action.send(value)
    }

    private suspend fun reduce(event: ChatEvent) {
        when (event) {
            ChatEvent.Launch -> {
                setState(oldState.copy(isLoading = true))
                messageRepository.loadMessages()
                    .onSuccess {
                        setState(oldState.copy(data = messages, isLoading = false))
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
                    return
                }
                setState(oldState.copy(message = event.message, isSending = true))
                messageRepository.sendMessage(event.message)
                    .onSuccess {
                        val data = messages
                        setState(oldState.copy(data = data, message = "", isSending = false))
                    }.onFailure {
                        setState(oldState.copy(isSending = false))
                        setAction(ChatAction.Error(errorMessage = it.localizedMessage ?: "", errorLabel))
                    }
            }
            is ChatEvent.Select -> {
                val data = oldState.data.map {
                    if (it.id == event.messageId) {
                        it.copy(isSelected = !it.isSelected)
                    } else {
                        it
                    }
                }
                setState(oldState.copy(data = data, isEditMode = data.any { it.isSelected }))
            }
            is ChatEvent.Delete -> {
                oldState.data
                    .filter { it.isSelected }
                    .map { it.id }
                    .forEach { messageRepository.deleteMessage(it) }
                setState(oldState.copy(data = messages, isEditMode = false))
            }
            is ChatEvent.ShowAuthorInfo -> {
                setState(oldState.copy(showAuthorInfoData = oldState.data.find { it.id == event.messageId }))
            }
            is ChatEvent.HideAuthorInfo -> {
                setState(oldState.copy(showAuthorInfoData = null))
            }
        }
    }
}