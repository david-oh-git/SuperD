package io.audioshinigami.superd.downloads

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/*
*
* A LiveData that only emits change when it isnt null.
* */

class OneTimeLiveData<T> : MutableLiveData<T>() {

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        // to allow a single observer
        if( hasActiveObservers() )
            throw Throwable("Only one observer allowed!!!")

        super.observe(owner, Observer {
            data ->
            if( data == null ) return@Observer

            observer.onChanged(data)

            value = null
        })
    }

    @MainThread
    fun sendData(data: T ){
        value = data
    }
}