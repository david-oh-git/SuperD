package io.audioshinigami.superd.zdata

import java.lang.Exception

sealed class Result<out R> {

    data class Success<out T>(val data: T): Result<T>()
    data class Error(val exception: Exception): Result<Nothing>()
    object Loading: Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/* checks if Result type is Sucess */
val Result<*>.sucessful
get() = this is Result.Success && data != null