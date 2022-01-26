package com.example.messenger_mvi.framework.managers

import androidx.annotation.StringRes

interface ResourceManager {
    fun getStringFromResource(@StringRes resId: Int): String
}