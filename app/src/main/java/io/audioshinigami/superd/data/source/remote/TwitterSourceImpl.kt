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

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import io.audioshinigami.superd.data.TweetMedia
import io.audioshinigami.superd.data.source.TwitterSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class TwitterSourceImpl @Inject constructor(
    private val twitterApiClient: TwitterApiClient,
    private val ioDispatcher: CoroutineDispatcher
): TwitterSource {

    override val allMediaChannel: Channel<io.audioshinigami.superd.data.Result<List<TweetMedia>>>
        = Channel(Channel.CONFLATED)

    override suspend fun getTweetUrls(id: Long) = withContext(ioDispatcher) {

        log("getTweet Activated...")
        val tweetCall = twitterApiClient.statusesService.show(id, null,null,null)

        tweetCall.enqueue( object: Callback<Tweet>(){

            override fun success(result: Result<Tweet>?) {
                log("success...")
                // set of all [TweetMedia] , all the urls and bitrate
                val allMedia = mutableSetOf<TweetMedia>()
                // if media present
                if(result?.data?.extendedEntities == null && result?.data?.entities?.media == null ){
                    log("No media")
                }
                else if ( (result.data.extendedEntities.media[0].type) != "video" &&
                    (result.data.extendedEntities.media[0].type) != "animated_gif" ){
                    log("No media")
                }else{

                    result.data.extendedEntities.media[0].videoInfo.variants?.map { variant ->
                        // save all TweetMedia
                        variant.apply {
                            if( url.contains(".mp4", ignoreCase = true)){
                                val url = extractDownloadUrl(url)

                                url?.let {
//                                        emit( TweetMedia( it, contentType, bitrate) )
                                    allMedia.add( TweetMedia( it, contentType, bitrate))
                                    log("url is $it")
                                }

                            }

                        }

                    }.also {
                        runBlocking {
                            updateTweetMediaList( io.audioshinigami.superd.data.Result.Success(allMedia.toList()) )
                        }

                    }

                }
            }

            override fun failure(exception: TwitterException?) {
                log("Tweet call failed :( \nError message: \n$exception")
                runBlocking {
                    updateTweetMediaList(io.audioshinigami.superd.data.Result.Error( Exception("Tweet call failed")) )
                }
            }
        } )

    }

    override suspend fun updateTweetMediaList(result: io.audioshinigami.superd.data.Result<List<TweetMedia>>){
        allMediaChannel.send(result)
    }

    private fun log(message: String){
        Timber.d(message)
    }

    private fun extractDownloadUrl(url: String) = Regex("""https://.*\.[mM][pP]4""").find(url)?.value?.trim()
}