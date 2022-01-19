package com.example.messenger_mvi.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = MessageEntity.TABLE_NAME)
data class MessageEntity(
    @PrimaryKey val id: Long,
    val authorId: Long,
    val authorName: String,
    val message: String,
    val isMyOwn: Boolean,
) {
    companion object {
        const val TABLE_NAME = "messages"
        const val MESSAGE_ID = "id"
    }
}
