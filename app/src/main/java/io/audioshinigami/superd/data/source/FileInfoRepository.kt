package io.audioshinigami.superd.data.source

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tonyodev.fetch2.FetchListener
import io.audioshinigami.superd.data.FileInfo
import io.audioshinigami.superd.data.Result

interface FileInfoRepository {

    suspend fun observeAll(): LiveData<Result<List<FileInfo>>>

    fun getAllPaged(): LiveData<PagedList<FileInfo>>

    suspend fun getAllFileInfo(): Result<List<FileInfo>>

    suspend fun save(vararg fileInfo: FileInfo)

    suspend fun update(fileInfo: FileInfo)

    suspend fun update( url: String , progressValue: Int )

    suspend fun find( id: Int ) : FileInfo?

    suspend fun find( url: String ) : FileInfo?

    suspend fun clearCompleted(): Int

    suspend fun delete(fileInfo: FileInfo)

    suspend fun delete( url: String )

    suspend fun delete( id: Int )

    suspend fun deleteAll()

    suspend fun start(url: String, downloadUri: Uri)

    suspend fun restart(url: String, downloadUri: Uri)

    fun pause(id: Int)

    fun resume(id: Int)

    fun isDownloading(): Boolean

    fun hasActiveListener(): Boolean

    suspend fun setListener(fetchListener: FetchListener)

    fun disableListener()

    fun addState(id: Int, state: State)
}