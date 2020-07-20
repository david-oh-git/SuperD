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

package io.audioshinigami.superd.data.source

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import io.audioshinigami.superd.data.TweetMedia
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class TwitterApiRepository(
    private val ioDispatcher: CoroutineDispatcher
): TwitterUrlRepository {

    override suspend fun getDownloadUrls(twitterUrl: String) = flow {
        log("starting fetchTweet...")
        val id = getId(twitterUrl)
        if( id.isNotBlank() && id.isNotEmpty())
            emit(getTweetUrls(id.toLong()))
        else{
            log("Invalid url")
        }
    }

    private fun extractDownloadUrl(url: String) = Regex("""https://.*\.[mM][pP]4""").find(url)?.value?.trim()

    private fun getId(twitterUrl: String) = twitterUrl.split("/")[5].split("?")[0].trim()

    private fun log(message: String){
        Timber.d(message)
    }

    private fun noMedia(){
        log("No media detected")
    }

    private fun getTweetUrls(id: Long): MutableSet<TweetMedia> {
        log("getTweet Activated...")
        val twitterApiClient = TwitterCore.getInstance().apiClient
        val statusesService = twitterApiClient.statusesService
        val tweetCall = statusesService.show(id, null,null,null)

        // set of all [TweetMedia] , all the urls and bitrate
        val allMedia = mutableSetOf<TweetMedia>()

        tweetCall.enqueue( object: Callback<Tweet>(){

            override fun success(result: Result<Tweet>?) {
                log("success...")
                // if media present
                if(result?.data?.extendedEntities == null && result?.data?.entities?.media == null ){
                    noMedia()
                }
                else if ( (result.data.extendedEntities.media[0].type) != "video" &&
                    (result.data.extendedEntities.media[0].type) != "animated_gif" ){
                    noMedia()
                }else{

                    result.data.extendedEntities.media[0].videoInfo.variants?.map { variant ->
                        // save all TweetMedia
                        variant.apply {
                            if( url.contains(".mp4", ignoreCase = true)){
                                val url = extractDownloadUrl(url)

                                url?.let {
                                    allMedia.add( TweetMedia( it, contentType, bitrate))
                                }

                            }

                        }

                    }

                }
            }

            override fun failure(exception: TwitterException?) {
                log("Tweet call failed :(")
            }
        } )

        return allMedia
    }
}