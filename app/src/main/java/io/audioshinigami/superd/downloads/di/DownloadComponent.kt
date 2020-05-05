package io.audioshinigami.superd.downloads.di

import dagger.Subcomponent
import io.audioshinigami.superd.downloads.DownloadsFragment

@Subcomponent( modules = [DownloadModule::class])
interface DownloadComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): DownloadComponent
    }

    fun inject( target: DownloadsFragment)
}