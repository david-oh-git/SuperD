package io.audioshinigami.superd.di.modules

import dagger.Module
import io.audioshinigami.superd.adddownload.di.AddDownloadComponent
import io.audioshinigami.superd.downloads.di.DownloadComponent

@Module(
    subcomponents = [
        DownloadComponent::class,
        AddDownloadComponent::class
    ]
)
object SubComponentModule