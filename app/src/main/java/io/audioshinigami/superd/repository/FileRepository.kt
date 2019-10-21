package io.audioshinigami.superd.repository



import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tonyodev.fetch2.NetworkType
import com.tonyodev.fetch2.Priority
import com.tonyodev.fetch2.Request
import io.audioshinigami.superd.App
import io.audioshinigami.superd.data.db.entity.FileData
import io.audioshinigami.superd.utility.ReUseMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object FileRepository {

    private val fileDataDao = App.instance.dataBaseInstance!!.fileDataDao()

    private val _allFileData = MutableLiveData<List<FileData>>()
    val allFileData: LiveData<List<FileData>> = _allFileData


    @WorkerThread  /* it marks it, so this cant be called from a UI thread*/
    suspend fun insert(fileData: FileData){
        fileDataDao.insert(fileData)
    } /*end save*/

    @WorkerThread
    suspend fun update(fileData: FileData){
        fileDataDao.updateFileData(fileData)
    }

    @WorkerThread
    suspend fun delete(fileData: FileData){
        fileDataDao.delete(fileData)
    }

    @WorkerThread
    fun findByUrl(url: String): FileData? {
        return fileDataDao.findByUrl(url)
    } /* end findbyUrl*/

    suspend fun fetchFileData(url: String): FileData? {
        return  withContext(Dispatchers.IO){
            fileDataDao.findByUrl(url)
        }
    }

    @WorkerThread
    suspend fun insertMultiple(fileData: Array<FileData>){
        fileDataDao.insertAll(*fileData)
    }

    @WorkerThread
    suspend fun startDownload(downloadUrl: String){

        val fetch = App.instance.fetch
        val request = createRequest(downloadUrl)
        fetch?.enqueue(request)

        val fileData = FileData(
            0,
            request.id,
            downloadUrl,
            downloadUrl.substringAfterLast("/"),
            0
        )

        insert(fileData)
        App.instance.activeDownloads!![downloadUrl] = true

    } /*end startDownload*/

    fun pauseDownLoad(id: Int, url: String){
        App.instance.fetch?.pause(id)
        App.instance.activeDownloads!![url] = false
    } /*end pause*/

    @WorkerThread
    fun restartDownload(downloadUrl: String){
        /*to restart download already in DB*/
        val fetch = App.instance.fetch
        val request = createRequest(downloadUrl)
        fetch?.enqueue(request)

        App.instance.activeDownloads!![downloadUrl] = true
    } /*end startDownload*/

    fun resumeDownload(id: Int, url: String){
        App.instance.fetch?.resume(id)
        App.instance.activeDownloads!![url] = true
    }

    private fun createRequest(urlString: String): Request {
        /*creates a request instance */
        val filename = urlString.substringAfterLast("/")
        val directory = ReUseMethods.getPublicFileStorageDir()

//        single queue
        val pathUri = Uri.parse( directory.toString() + File.separator + filename )

        val request =  Request(urlString, pathUri)
        request.priority = Priority.HIGH
        request.networkType = NetworkType.ALL
        request.addHeader("clientKey", "SD78DF93_3947&MVNGHE1WONG")

        return request
    } /*end createRequest*/

} /*end FileRepo*/