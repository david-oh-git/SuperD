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

package io.audioshinigami.superd.adddownload

import android.net.Uri
import androidx.lifecycle.*
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.data.TweetMedia
import io.audioshinigami.superd.data.source.FileInfoRepository
import io.audioshinigami.superd.data.source.TwitterApiRepoImpl
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class AddDownloadViewModel @Inject constructor(
    private val fileInfoRepository: FileInfoRepository,
    private val twitterRepository: TwitterApiRepoImpl
): ViewModel() {

    private val _isTwitterUrl = MutableLiveData<Boolean>()
    val isTwitterUrl: LiveData<Boolean> = _isTwitterUrl

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isListVisible = MutableLiveData<Boolean>()
    val isListVisible: LiveData<Boolean> = _isListVisible

    private val _listOfMedia = MutableLiveData<List<TweetMedia>>()
    val listOfMedia: LiveData<Result<List<TweetMedia>>>
            get() = liveData {
        twitterRepository.allMediaChannel.consumeAsFlow()
            .catch { error -> log("Error msg: \n $error") }
            .collect {
                emit(it)
            }
//                emit( twitterApiRepository.listOfMedia.toList() )
    }

    internal fun startDownload( url: String , downloadUri:Uri ): Boolean {

        if( url.contains("https://twitter.com/", ignoreCase = true)){
            loadTweetUrl(url)
            return false
        }

        viewModelScope.launch {
            fileInfoRepository.start(url, downloadUri)
        }
        return true
    }

    fun showListView() {
        _isLoading.value = false
        _isListVisible.value = true
    }

    fun loadTweetUrl(url: String) = viewModelScope.launch {

        _isListVisible.postValue(false) // hide RecyclerView
        _isTwitterUrl.postValue(true)
        _isLoading.postValue(true)
        // code to update listOfMedia
        twitterRepository.getDownloadUrls( getId(url) )

    }

    private fun log(message: String){
        Timber.d(message)
    }

    private fun getId(twitterUrl: String) = twitterUrl.split("/")[5].split("?")[0].trim().toLong()
}