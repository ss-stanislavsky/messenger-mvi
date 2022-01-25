package com.example.messenger_mvi.business.model.chat

import com.example.messenger_mvi.ui.models.MessageUI

sealed class ChatState(open val data: List<MessageUI> = emptyList(), open val message: String = "") {
    class Loading(override val data: List<MessageUI>, override val message: String) : ChatState(data, message)
    class Sending(override val data: List<MessageUI>, override val message: String) : ChatState(data, message)
    class Error(override val data: List<MessageUI>, override val message: String, val errorMessage: String) : ChatState(data, message)
    class Data(override val data: List<MessageUI>, override val message: String) : ChatState(data, message)
}
