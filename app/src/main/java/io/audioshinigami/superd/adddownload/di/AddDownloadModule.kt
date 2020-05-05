package io.audioshinigami.superd.adddownload.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.audioshinigami.superd.adddownload.AddDownloadViewModel
import io.audioshinigami.superd.di.ViewModelKey


@Module
abstract class AddDownloadModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddDownloadViewModel::class)
    abstract fun bindViewModel( viewModel: AddDownloadViewModel): ViewModel
}