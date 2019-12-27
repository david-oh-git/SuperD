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

/*
* implementation of the projects default repository
*/


class DefaultRepository(
    private val dao: FileDataDao,
    val fetch: Fetch,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataBaseRepository, DownloadDataSource {

    private fun createRequest(urlString: String): Request {

        val fileName = urlString.substringAfterLast("/")
        val directory = ReUseMethods.getPublicFileStorageDir()
        val downloadUri = Uri.parse( directory.toString() + File.separator + fileName )

        return  Request(urlString, downloadUri!! ) // TODO fix this null BS !!!!
            .apply {
            priority = Priority.NORMAL
            networkType = NetworkType.ALL
            addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG")
        }

    }

    override suspend fun save(fileData: FileData) = withContext(ioDispatcher){

        dao.insert(fileData)
    }

    override suspend fun save(vararg fileData: FileData) = withContext(ioDispatcher){
        dao.insert(*fileData)
    }

    override suspend fun update(url: String, progressValue: Int ) = withContext(ioDispatcher){
        dao.update( url , progressValue )
    }

    override suspend fun getById(id: Int): FileData? {
        return dao.find(id)
    }

    override suspend fun update(fileData: FileData) = withContext(ioDispatcher) {
        dao.update(fileData)
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

    override suspend fun delete(id: Int) = withContext(ioDispatcher) {
        dao.delete(id)
    }

    override suspend fun delete(url: String): Int {
        return dao.delete( url )
    }

    override suspend fun getAll() = dao.getAll()

    fun getAllPaged() = dao.getAllPaged()

    override suspend fun getData(): List<FileData> {
        return dao.getData()
    }

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

    override fun pause(id: Int) {

        /*pause download*/
        fetch.pause(id)
    }

    override fun resume(id: Int) {
        fetch.resume(id)
    }

    override suspend fun restart(url: String) {
        /* create a request*/
        val request = createRequest(url)

        /*update request id in DB */
        dao.updateRequestId( url, request.id)

        /* start the download*/
        fetch.enqueue(request)
    }

}