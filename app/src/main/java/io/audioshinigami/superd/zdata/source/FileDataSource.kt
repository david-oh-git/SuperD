package io.audioshinigami.superd.zdata.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.audioshinigami.superd.zdata.FileData
import io.audioshinigami.superd.zdata.Result

/*entry point for accessing FileData in DB */

interface FileDataSource {

    fun observeAll(): LiveData<Result<List<FileData>>>

    fun getAllPaged(): LiveData<PagedList<FileData>>

    suspend fun getDataResult(): Result<List<FileData>>

    fun observeFileData(id: Int): LiveData<Result<FileData>>

    suspend fun find(id: Int): FileData?

    suspend fun find(getUrl: String): FileData?

    suspend fun insert(vararg allFileData: FileData)

    suspend fun update(fileData: FileData)

    suspend fun update(url: String, progress: Int)

    suspend fun updateRequestId(url: String, id: Int)

    suspend fun delete(fileData: FileData)

    suspend fun delete( url: String ): Int

    suspend fun delete(id: Int): Int

    suspend fun deleteCompletedFileData(): Int

    suspend fun deleteAll()
}