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

import dagger.Binds
import dagger.Module
import dagger.Provides
import io.audioshinigami.superd.data.TweetMedia
import io.audioshinigami.superd.data.source.TwitterSource
import io.audioshinigami.superd.data.source.remote.FakeTwitterSource
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
abstract class TestModule {

    @Singleton
    @Binds
    abstract fun bindTwitterSource(twitterSource: FakeTwitterSource): TwitterSource

    companion object {

        @JvmStatic
        @Provides
        @Singleton
        fun provideIoDispatcher() = Dispatchers.Unconfined

        @JvmStatic
        @Provides
        @Singleton
        fun provideFakeTwitterResponse(): List<TweetMedia> = loadFakeResponse()

        private fun loadFakeResponse() =
                listOf(
                    TweetMedia("https://video.twimg.com/ext_tw_video/1000818082846334982/pu/vid/480x480/8nrrUYJegAWhRiVQ.mp4",
                        "video/mp4", 256000 ),
                    TweetMedia("https://video.twimg.com/ext_tw_video/1000818082846334982/pu/vid/240x240/jStnA8jBZ7idPSFw.mp4",
                        "video/mp4", 832000 ),
                    TweetMedia("https://video.twimg.com/ext_tw_video/1000818082846334982/pu/vid/240x240/twenty_fourw.mp4",
                        "video/mp4", 832000 ),
                    TweetMedia("https://video.twimg.com/ext_tw_video/1000818082846334982/pu/vid/480x480/8nrrUYJegAWhRiVQ.mp4",
                        "video/mp4", 256000 ),
                    TweetMedia("https://video.twimg.com/ext_tw_video/1000818082846334982/pu/vid/240x240/jStnA8jBZ7idPSFw.mp4",
                        "video/mp4", 832000 ),
                    TweetMedia("https://video.twimg.com/ext_tw_video/1000818082846334982/pu/vid/240x240/twenty_fourw.mp4",
                        "video/mp4", 832000 )
                )

    } // END companion obj

}