package com.example.messenger_mvi.business.model.message

data class MessageData(
    val id: Long?,
    val authorId: Long?,
    val authorName: String?,
    val message: String?,
)
