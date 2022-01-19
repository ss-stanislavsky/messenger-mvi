package com.example.messenger_mvi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.messenger_mvi.database.entity.MessageDao
import com.example.messenger_mvi.database.entity.MessageEntity

private const val DB_NAME = "KEGEL_DATABASE"
private const val DB_VERSION = 1

@Database(
    version = DB_VERSION,
    entities = [
        MessageEntity::class,
    ]
)
@TypeConverters(
//    ListStringConverter::class,
//    ByteArrayConverter::class,
)

abstract class MessengerDatabase : RoomDatabase() {
    abstract val messageDao: MessageDao

    companion object {
        fun create(context: Context) =
            Room.databaseBuilder(context, MessengerDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}