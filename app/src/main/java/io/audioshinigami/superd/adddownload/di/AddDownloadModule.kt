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

package io.audioshinigami.superd.adddownload.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.twitter.sdk.android.core.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.audioshinigami.superd.BuildConfig
import io.audioshinigami.superd.adddownload.AddDownloadViewModel
import io.audioshinigami.superd.data.TweetMedia
import io.audioshinigami.superd.data.source.TwitterSource
import io.audioshinigami.superd.data.source.remote.TwitterSourceImpl
import io.audioshinigami.superd.di.ViewModelKey
import kotlinx.coroutines.channels.Channel


@Module
abstract class AddDownloadModule {

    @AddDownloadScope
    @Binds
    @IntoMap
    @ViewModelKey(AddDownloadViewModel::class)
    abstract fun bindViewModel( viewModel: AddDownloadViewModel): ViewModel

    @AddDownloadScope
    @Binds
    abstract fun bindTwitterSource( twitterSource: TwitterSourceImpl): TwitterSource

    companion object {

        @JvmStatic
        @AddDownloadScope
        @Provides
        fun provideMediaListChannel(): Channel<List<TweetMedia>> = Channel(Channel.CONFLATED)

        @AddDownloadScope
        @JvmStatic
        @Provides
        fun provideTwitterApiClient(context: Context): TwitterApiClient {

            val twitterAuthConfig = TwitterAuthConfig(BuildConfig.API_KEY, BuildConfig.API_SECRET)
            val builder = TwitterConfig.Builder(context)
            builder.twitterAuthConfig( twitterAuthConfig)
            Twitter.initialize(builder.build())

            return TwitterCore.getInstance().apiClient
        }
    }
}