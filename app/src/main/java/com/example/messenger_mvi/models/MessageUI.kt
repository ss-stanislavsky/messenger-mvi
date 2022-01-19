package com.example.messenger_mvi.models

data class MessageUI(
    val id: Long,
    val authorId: Long,
    val authorName: String,
    val message: String,
    val isMyOwn: Boolean,
)
