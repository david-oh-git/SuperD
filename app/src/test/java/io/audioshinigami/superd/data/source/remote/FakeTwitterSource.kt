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

package io.audioshinigami.superd.data.source.remote

import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.data.TweetMedia
import io.audioshinigami.superd.data.source.TwitterSource
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class FakeTwitterSource @Inject constructor(
    private val fakeTwitterService: List<TweetMedia>
) : TwitterSource {

    override val allMediaChannel: Channel<Result<List<TweetMedia>>> = Channel(Channel.CONFLATED)

    override suspend fun getTweetUrls(id: Long) {
        if( id % 2L == 0L){
            updateTweetMediaList( Result.Success(fakeTwitterService))
        }
        else
            updateTweetMediaList( Result.Error( Exception("Error getting url")))
    }

    override suspend fun updateTweetMediaList(result: Result<List<TweetMedia>>) {
        allMediaChannel.send(result)
    }
}