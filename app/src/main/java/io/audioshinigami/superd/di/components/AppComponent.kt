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