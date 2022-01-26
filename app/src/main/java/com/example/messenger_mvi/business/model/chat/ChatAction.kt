package com.example.messenger_mvi.business.model.chat

sealed class ChatAction() {
    object Empty : ChatAction()
    class Error(val errorMessage: String, val errorLabel: String) : ChatAction()
    class Scroll(val position: Int) : ChatAction()
}