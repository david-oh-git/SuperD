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

import android.net.Uri
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2.exception.FetchException
import com.tonyodev.fetch2core.Func
import io.audioshinigami.superd.data.FileInfo
import io.audioshinigami.superd.data.source.DownloadDataSource
import io.audioshinigami.superd.data.source.State
import io.audioshinigami.superd.data.source.State.DOWNLOADING
import io.audioshinigami.superd.data.source.State.PAUSED
import io.audioshinigami.superd.data.source.local.FileInfoDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class RemoteDownloadDataSource internal constructor(
    private val fetch: Fetch,
    private val fileInfoDao: FileInfoDao,
    private val activeListener: ActiveListener,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    override var _priority: Priority = Priority.NORMAL,
    override var _networkType: NetworkType = NetworkType.ALL

): DownloadDataSource  {

    /* fetch listener*/
    private var _fetchListener: FetchListener? = null

    override suspend fun start(url: String, downloadUri: Uri ) = withContext(ioDispatcher){
        /* create a request*/
        val request = createRequest(url, downloadUri)


        try {
            /* start the download*/
            fetch.enqueue(request,null,
                Func<Error> {
                    // TODO send snack message with error
                    Timber.e("Error is ${it.value}")
                })

            // add to active urls
            activeListener.add(request.id, DOWNLOADING)

            /* create [FileData] info for file*/
            val fileData = FileInfo(
                0,
                request.id,
                url,
                url.substringAfterLast("/"),
                0
            )

            /* add to DB*/
            fileInfoDao.insert(fileData)

        }catch ( e: FetchException){
            Timber.d("Error message is ${e.message}")
        }


    }

    override suspend fun restart(url: String, downloadUri: Uri ) = withContext(ioDispatcher){
        /* create a request*/
        val request = createRequest(url, downloadUri)



        try {
            /* start the download*/
            fetch.enqueue(request,null,
                Func<Error> {
                    // TODO send snack message with error
                    Timber.e("Error is ${it.value}")
                })

            // add to active urls
            activeListener.add(request.id, DOWNLOADING)

            fileInfoDao.updateRequestId( url, request.id )
        }catch (e: FetchException){
            Timber.d("Error message is ${e.message}")
        }
    }

    override fun pause(id: Int) {
        fetch.pause(id)
        // add to active urls
        activeListener.add(id, PAUSED)
    }

    override fun resume(id: Int) {
        fetch.resume(id)
        // add to active urls
        activeListener.add(id, DOWNLOADING)
    }

    override fun isDownloading() = activeListener.isDownloading()

    override fun addState(id: Int, isActive: State) {
        activeListener.add(id, isActive)
    }

    override suspend fun setListener(fetchListener: FetchListener) {
        if( _fetchListener == null)
            fetch.addListener( provideFetchListener(fetchListener) )
    }

    override fun disableListener() {

        _fetchListener?.run {
            fetch.removeListener(this)
        }

        _fetchListener = null
    }

    override fun hasActiveListener() = _fetchListener != null

    private suspend fun createRequest(url: String, downloadUri: Uri ): Request
            = withContext(ioDispatcher){

        return@withContext  Request(url, downloadUri )
            .apply {
                priority = _priority
                networkType = _networkType
                addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG")
            }
    }

    private fun provideFetchListener(fetchListener: FetchListener): FetchListener {
        synchronized(this){
            val defaultListener = _fetchListener

            if( defaultListener == null ){
                _fetchListener = fetchListener

                return fetchListener
            }

            return _fetchListener ?: fetchListener
        }
    }

}