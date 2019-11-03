package io.audioshinigami.superd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.audioshinigami.superd.data.repository.DefaultRepository

class ViewModelFactory constructor(
    private val repository: DefaultRepository ): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass){

            when {
                isAssignableFrom(SharedViewModel::class.java) -> {
                    SharedViewModel(repository)
                }
                else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}