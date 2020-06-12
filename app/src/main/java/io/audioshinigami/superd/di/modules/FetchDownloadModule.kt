/*
 * MIT License
 *
 * Copyright (c) 2020  David Osemwota
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.audioshinigami.superd.di.modules

import android.content.Context
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import dagger.Module
import dagger.Provides
import io.audioshinigami.superd.BuildConfig
import io.audioshinigami.superd.common.NUMBER_OF_DOWLOADS
import io.audioshinigami.superd.data.source.DownloadDataSource
import io.audioshinigami.superd.data.source.local.FileInfoDao
import io.audioshinigami.superd.data.source.remote.ActiveListener
import io.audioshinigami.superd.data.source.remote.RemoteDownloadDataSource
import javax.inject.Named
import javax.inject.Singleton


@Module
class FetchDownloadModule {

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
            .enableLogging(BuildConfig.DEBUG)
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