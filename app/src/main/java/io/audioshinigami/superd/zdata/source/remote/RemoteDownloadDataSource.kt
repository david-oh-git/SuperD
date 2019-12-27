package io.audioshinigami.superd.zdata.source.remote

import android.net.Uri
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.NetworkType
import com.tonyodev.fetch2.Priority
import com.tonyodev.fetch2.Request
import io.audioshinigami.superd.zdata.FileData
import io.audioshinigami.superd.zdata.source.DownloadDataSource
import io.audioshinigami.superd.zdata.source.local.FileDataDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteDownloadDataSource internal constructor(
    private val fetch: Fetch,
    private val fileDataDao: FileDataDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    override var _priority: Priority = Priority.NORMAL,
    override var _networkType: NetworkType = NetworkType.ALL

): DownloadDataSource  {

    override suspend fun start(url: String , downloadUri: Uri ) = withContext(ioDispatcher){
        /* create a request*/
        val request = createRequest(url, downloadUri)
        /* start the download*/
        fetch.enqueue(request)

        /* create [FileData] info for file*/
        val fileData = FileData(
            0,
            request.id,
            url,
            url.substringAfterLast("/"),
            0
        )

        /* add to DB*/
        fileDataDao.insert(fileData)
    }

    override suspend fun restart(url: String, downloadUri: Uri ) = withContext(ioDispatcher){
        /* create a request*/
        val request = createRequest(url, downloadUri)

        /* start the download*/
        fetch.enqueue(request)
        
        fileDataDao.updateRequestId( url, request.id )
    }

    override fun pause(id: Int) {
        fetch.pause(id)
    }

    override fun resume(id: Int) {
        fetch.resume(id)
    }

    override suspend fun delete(id: Int) = withContext(ioDispatcher){
        fetch.delete(id)
    }

    override suspend fun remove(id: Int) = withContext(ioDispatcher){
        fetch.remove(id)
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
}