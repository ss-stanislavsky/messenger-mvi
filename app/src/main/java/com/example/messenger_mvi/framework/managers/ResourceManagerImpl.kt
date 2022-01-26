package com.example.messenger_mvi.framework.managers

import android.content.Context
import androidx.annotation.StringRes

class ResourceManagerImpl(private val context: Context) : ResourceManager {
    override fun getStringFromResource(@StringRes resId: Int): String =
        context.getString(resId)
}