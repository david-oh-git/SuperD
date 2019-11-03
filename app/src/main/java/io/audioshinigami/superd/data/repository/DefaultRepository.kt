package io.audioshinigami.superd.data.repository

import android.net.Uri
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.NetworkType
import com.tonyodev.fetch2.Priority
import com.tonyodev.fetch2.Request
import io.audioshinigami.superd.data.source.db.dao.FileDataDao
import io.audioshinigami.superd.data.source.db.entity.FileData
import io.audioshinigami.superd.utility.ReUseMethods
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/*implementation of the projects default repository */


class DefaultRepository(
    private val dao: FileDataDao,
    private val fetch: Fetch,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataBaseRepository, DownloadRepository {

    private fun createRequest(urlString: String): Request {

        val fileName = urlString.substringAfterLast("/")
        val directory = ReUseMethods.getPublicFileStorageDir()

        val pathUri = Uri.parse( directory.toString() + File.separator + fileName )

        val request =  Request(urlString, pathUri)

        request.apply {
            priority = Priority.HIGH
            networkType = NetworkType.ALL
            addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG")
        }

        return request
    }

    override suspend fun save(fileData: FileData) = withContext(ioDispatcher){
        dao.insert(fileData)
    }

    override suspend fun saveAll(vararg fileData: FileData) = withContext(ioDispatcher){
        dao.insertAll(*fileData)
    }

    override suspend fun getById(id: Int): FileData? {
        return dao.findById(id)
    }

    override suspend fun update(fileData: FileData) = withContext(ioDispatcher) {
        dao.updateFileData(fileData)
    }

    override suspend fun clearCompleted() = withContext(ioDispatcher){
        return@withContext dao.deleteCompletedFileData()
    }

    override suspend fun delete(fileData: FileData) = withContext(ioDispatcher){
        dao.delete(fileData)
    }

    override suspend fun deleteAll() = withContext(ioDispatcher){
        dao.deleteAll()
    }

    override suspend fun deleteById(id: Int) = withContext(ioDispatcher) {
        dao.deleteById(id)
    }

    override suspend fun getAll() = dao.getAll()

    override suspend fun start(url: String) {

        /* create a request*/
        val request = createRequest(url)

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
        save(fileData)
    }

    override suspend fun pause(id: Int) {

        /*pause download*/
        fetch.pause(id)
    }

    override suspend fun resume(id: Int) {
        fetch.resume(id)
    }

    override suspend fun restart(url: String) {
        /* create a request*/
        val request = createRequest(url)

        /* start the download*/
        fetch.enqueue(request)
    }
}