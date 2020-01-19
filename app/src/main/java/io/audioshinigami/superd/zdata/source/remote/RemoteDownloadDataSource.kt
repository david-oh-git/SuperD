package io.audioshinigami.superd.zdata.source.remote

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.tonyodev.fetch2.*
import io.audioshinigami.superd.zdata.FileInfo
import io.audioshinigami.superd.zdata.source.DownloadDataSource
import io.audioshinigami.superd.zdata.source.local.FileInfoDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDownloadDataSource internal constructor(
    private val fetch: Fetch,
    private val fileInfoDao: FileInfoDao,
    override val isActive: MutableMap<Int, Boolean>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    override var _priority: Priority = Priority.NORMAL,
    override var _networkType: NetworkType = NetworkType.ALL

): DownloadDataSource  {

    /* fetch listener*/
    var _fetchListener: FetchListener? = null

    override suspend fun start(url: String, downloadUri: Uri ) = withContext(ioDispatcher){
        /* create a request*/
        val request = createRequest(url, downloadUri)
        /* start the download*/
        fetch.enqueue(request)

        // add to active urls
        isActive[request.id] = true


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

        /* start the download*/
        fetch.enqueue(request)
        // add to active urls
        isActive[request.id] = true
        
        fileInfoDao.updateRequestId( url, request.id )
    }

    override fun pause(id: Int) {
        fetch.pause(id)
        // add to active urls
        isActive[id] = false
    }

    override fun resume(id: Int) {
        fetch.resume(id)
        // add to active urls
        isActive[id] = true
    }

    override suspend fun delete(id: Int) = withContext(ioDispatcher){
        fetch.delete(id)
    }

    override suspend fun remove(id: Int) = withContext(ioDispatcher){
        fetch.remove(id)
    }

    override suspend fun update(url: String, progress: Int) = withContext(ioDispatcher){
        fileInfoDao.update(url, progress)
    }

    override suspend fun isActive(id: Int) = withContext(ioDispatcher) {
        return@withContext isActive[id]
    }

    override suspend fun isDownloading() = withContext(ioDispatcher) {
        // TODO shameless coding here, utterly shameless.

        if(isActive.isEmpty() )
            return@withContext MutableLiveData(false)

        for ( value in isActive.values ){
            if(value)
                return@withContext MutableLiveData(value)
        }

        return@withContext MutableLiveData(false)
    }

    override fun setListener(fetchListener: FetchListener) {
        fetch.addListener( provideFetchListener(fetchListener) )
    }

    override fun disableListener() {

        _fetchListener?.run {
            fetch.removeListener(this)
        }

        _fetchListener = null
    }

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