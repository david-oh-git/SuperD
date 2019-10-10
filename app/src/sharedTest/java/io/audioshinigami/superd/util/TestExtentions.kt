package io.audioshinigami.superd.util

import androidx.lifecycle.LiveData

fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit ) {
    val observer = OneTimeObserver(handler = onChangeHandler)
    observe(observer, observer)
}