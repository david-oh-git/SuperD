package io.audioshinigami.superd.zdata.source.remote

import android.net.Uri
import com.tonyodev.fetch2.*
import io.audioshinigami.superd.App
import io.audioshinigami.superd.zdata.FileInfo
import io.audioshinigami.superd.zdata.source.DownloadDataSource
import io.audioshinigami.superd.zdata.source.local.FileInfoDao
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
        /* start the download*/
        fetch.enqueue(request)

        // add to active urls
        activeListener.add(request.id, true)

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

    }

    override suspend fun restart(url: String, downloadUri: Uri ) = withContext(ioDispatcher){
        /* create a request*/
        val request = createRequest(url, downloadUri)

        Timber.d("uri is $downloadUri")

        /* start the download*/
        fetch.enqueue(request)
        // add to active urls
        activeListener.add(request.id, true)
        
        fileInfoDao.updateRequestId( url, request.id )
    }

    override fun pause(id: Int) {
        fetch.pause(id)
        // add to active urls
        activeListener.add(id, false)
    }

    override fun resume(id: Int) {
        fetch.resume(id)
        // add to active urls
        activeListener.add(id, true)
    }

    override fun isDownloading() =  activeListener.isDownloading()

    override fun onError(id: Int) {
        activeListener.onError(id)
    }

    override suspend fun setListener(fetchListener: FetchListener) {
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