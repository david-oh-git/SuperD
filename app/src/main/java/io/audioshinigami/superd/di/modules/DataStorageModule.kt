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
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.audioshinigami.superd.common.FILE_DB_NAME
import io.audioshinigami.superd.data.source.FileInfoSource
import io.audioshinigami.superd.data.source.local.FileDatabase
import io.audioshinigami.superd.data.source.local.FileInfoDao
import io.audioshinigami.superd.data.source.local.LocalFileInfoSource
import javax.inject.Singleton

@Module
class DataStorageModule {

    @Provides
    @Singleton
    fun provideFileDatabase(context: Context): FileDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            FileDatabase::class.java, FILE_DB_NAME
        ).build()

    @Provides
    @Singleton
    fun provideFileInfoDao( fileDatabase: FileDatabase ): FileInfoDao =
        fileDatabase.fileDataDao()

    @Provides
    @Singleton
    fun provideFileInfoDataSource(fileInfoDao: FileInfoDao): FileInfoSource =
        LocalFileInfoSource( fileInfoDao )
}