package io.audioshinigami.superd.adddownload.di

import dagger.Subcomponent
import io.audioshinigami.superd.adddownload.AddDownloadFragment


@Subcomponent(modules = [AddDownloadModule::class])
interface AddDownloadComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddDownloadComponent
    }

    fun inject( target: AddDownloadFragment)
}