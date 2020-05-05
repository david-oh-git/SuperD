package io.audioshinigami.superd.downloads.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.audioshinigami.superd.di.ViewModelKey
import io.audioshinigami.superd.downloads.DownloadsViewModel

@Module
abstract class DownloadModule {

    @Binds
    @IntoMap
    @ViewModelKey(DownloadsViewModel::class)
    abstract fun bindViewModel( viewModel: DownloadsViewModel): ViewModel
}