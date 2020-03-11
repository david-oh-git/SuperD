package io.audioshinigami.superd.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.audioshinigami.superd.data.FileInfo
import io.audioshinigami.superd.data.Result

/*entry point for accessing FileData in DB */

interface FileInfoSource {

    fun observeAll(): LiveData<Result<List<FileInfo>>>

    fun getAllPaged(): LiveData<PagedList<FileInfo>>

    suspend fun getDataResult(): Result<List<FileInfo>>

    fun observeFileData(id: Int): LiveData<Result<FileInfo>>

    suspend fun find(id: Int): FileInfo?

    suspend fun find(getUrl: String): FileInfo?

    suspend fun insert(vararg allFileData: FileInfo)

    suspend fun update(fileInfo: FileInfo)

    suspend fun update(url: String, progress: Int)

    suspend fun updateRequestId(url: String, id: Int)

    suspend fun delete(fileInfo: FileInfo)

    suspend fun delete( url: String ): Int

    suspend fun delete(id: Int): Int

    suspend fun deleteCompletedFileData(): Int

    suspend fun deleteAll()
}