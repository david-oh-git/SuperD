package io.audioshinigami.superd.utility

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import io.audioshinigami.superd.App
import io.audioshinigami.superd.ViewModelFactory
import io.audioshinigami.superd.data.repository.DefaultRepository

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>): T {
    val repository = DefaultRepository(App.instance.dataBaseInstance?.fileDataDao()!! ,
        App.instance.fetch!! )

    return ViewModelProviders.of(this.requireActivity(), ViewModelFactory(repository)).get(viewModelClass)
}
