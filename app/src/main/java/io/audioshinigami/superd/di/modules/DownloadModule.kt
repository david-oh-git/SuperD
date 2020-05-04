package io.audioshinigami.superd.di.modules

import android.content.Context
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import dagger.Module
import dagger.Provides
import io.audioshinigami.superd.common.NUMBER_OF_DOWLOADS
import io.audioshinigami.superd.data.source.DownloadDataSource
import io.audioshinigami.superd.data.source.local.FileInfoDao
import io.audioshinigami.superd.data.source.remote.ActiveListener
import io.audioshinigami.superd.data.source.remote.RemoteDownloadDataSource
import javax.inject.Named
import javax.inject.Singleton


@Module
class DownloadModule {

    @Provides
    @Singleton
    @Named(NUMBER_OF_DOWLOADS)
    fun provideNumberOfDownloads(): Int = 3

    @Provides
    @Singleton
    fun provideFetchConfiguration(
        context: Context,
        @Named(NUMBER_OF_DOWLOADS) numberOfDownloads: Int) =
        FetchConfiguration.Builder(context.applicationContext )
            .setDownloadConcurrentLimit(numberOfDownloads)
            .enableLogging(true)
            .build()

    @Provides
    @Singleton
    fun provideFetch( fetchConfig: FetchConfiguration) =
        Fetch.Impl.getInstance(fetchConfig)

    @Provides
    @Singleton
    fun provideDownloadDataSource(
        fetch: Fetch,
        fileInfoDao: FileInfoDao,
        activeListener: ActiveListener
    ): DownloadDataSource = RemoteDownloadDataSource( fetch, fileInfoDao,activeListener)

}