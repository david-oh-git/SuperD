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

package io.audioshinigami.superd

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import io.audioshinigami.superd.data.source.DefaultPreferenceRepository
import io.audioshinigami.superd.data.source.FileInfoRepository
import io.audioshinigami.superd.data.source.SharedPreferenceRepo
import io.audioshinigami.superd.data.source.State
import io.audioshinigami.superd.data.source.local.FileDatabase
import io.audioshinigami.superd.data.source.local.LocalPreferenceSource

object ServiceLocator {

    @Volatile // could be used from multiple threads
    var sharedPreferences: SharedPreferences? = null
    private var database: FileDatabase? = null
    @Volatile
    var fileInfoRepository: FileInfoRepository? = null
        @VisibleForTesting set

    @Volatile
    var preferenceRepository: SharedPreferenceRepo? = null

    @Volatile
    private var activeDownloads: MutableMap<Int, State>? = null

    internal fun provideSharedPreferenceRepository( name: String, context: Context): SharedPreferenceRepo {
        synchronized(this){
            return preferenceRepository ?: createPreferenceRepository(name, context)
        }
    }

    internal fun provideActiveDownloadsMap(): MutableMap<Int, State> {
        synchronized(this){
            return activeDownloads ?: mutableMapOf()
        }
    }

    private fun createPreferenceRepository( name: String ,context: Context): SharedPreferenceRepo {
        return DefaultPreferenceRepository(
            createLocalPreferenceSource(name, context )
        )
    }

    private fun createSharedPreference( name: String, context: Context ): SharedPreferences {
        return context.getSharedPreferences( name , 0 )
    }

    private fun createLocalPreferenceSource(name: String, context: Context): LocalPreferenceSource {
        return LocalPreferenceSource(
            createSharedPreference(name, context)
        )
    }

    @VisibleForTesting
    fun resetRepository(){

        database = null
        fileInfoRepository = null
    }

}