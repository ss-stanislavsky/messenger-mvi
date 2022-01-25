package com.example.messenger_mvi.framework.local

import com.example.messenger_mvi.business.local.Storage

class StorageImpl : Storage {
    override val userId: Long
        get() = CURRENT_USER_ID

    companion object {
        private const val CURRENT_USER_ID = 0L
    }
}