package com.example.messenger_mvi.business.network

sealed class Result<out R> {

    data class Success<out R>(val data: R?) : Result<R>()
    data class Error(val error: BaseError) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[exception=$error]"
        }
    }
}

inline fun <reified R> Result<R>.onSuccess(perform: (R?) -> Unit): Result<R> {
    if (this is Result.Success) perform(data)
    return this
}

inline fun <reified R> Result<R>.onError(perform: (BaseError) -> Unit): Result<R> {
    if (this is Result.Error) perform(error)
    return this
}

class BaseError(message: String = "") : Throwable(message)