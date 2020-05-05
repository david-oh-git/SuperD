package io.audioshinigami.superd.di.modules

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import io.audioshinigami.superd.di.ViewModelFactory


@Module
abstract class ViewModelBuilderModule {

    @Binds
    abstract fun bindViewModelFactory( factory: ViewModelFactory): ViewModelProvider.Factory
}