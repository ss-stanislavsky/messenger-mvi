package com.example.messenger_mvi.ui.models

data class MessageUI(
    val id: Long,
    val authorId: Long,
    val authorName: String,
    val authorNameAlias: String,
    val message: String,
    val isMyOwn: Boolean,
)
