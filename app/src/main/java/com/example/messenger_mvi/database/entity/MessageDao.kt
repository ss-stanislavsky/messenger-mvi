package com.example.messenger_mvi.database.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = REPLACE)
    fun insert(value: MessageEntity)

    @Insert(onConflict = REPLACE)
    fun insert(values: List<MessageEntity>)

    @Update
    fun update(value: MessageEntity)

    @Query("SELECT * FROM ${MessageEntity.TABLE_NAME}")
    fun getAllMessages(): List<MessageEntity>

    @Query("SELECT * FROM ${MessageEntity.TABLE_NAME}")
    fun getAllMessagesFlow(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM ${MessageEntity.TABLE_NAME} WHERE ${MessageEntity.MESSAGE_ID} = :id")
    fun getMessageById(id: Long): MessageEntity

    @Query("SELECT * FROM ${MessageEntity.TABLE_NAME} WHERE ${MessageEntity.MESSAGE_ID} = :id")
    fun getMessageByIdFlow(id: Long): Flow<MessageEntity>

    @Query("DELETE FROM ${MessageEntity.TABLE_NAME} WHERE ${MessageEntity.MESSAGE_ID} = :id")
    fun deleteMessage(id: Long)
}