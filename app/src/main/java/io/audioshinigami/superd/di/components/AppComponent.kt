package io.audioshinigami.superd.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.audioshinigami.superd.adddownload.di.AddDownloadComponent
import io.audioshinigami.superd.data.source.FileInfoRepository
import io.audioshinigami.superd.di.modules.*
import io.audioshinigami.superd.downloads.di.DownloadComponent
import javax.inject.Singleton

/**
 * Application component
 */

@Singleton
@Component(
    modules = [
        AppModule::class,
        AppModuleBinds::class,
        DataStorageModule::class,
        FetchDownloadModule::class,
        SubComponentModule::class,
        ViewModelBuilderModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appContext: Context): AppComponent
    }

    fun downloadComponent(): DownloadComponent.Factory

    fun addDownload(): AddDownloadComponent.Factory

    val fileInfoRepository: FileInfoRepository
}