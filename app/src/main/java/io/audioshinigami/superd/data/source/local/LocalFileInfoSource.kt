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

package io.audioshinigami.superd.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.audioshinigami.superd.data.FileInfo
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.data.Result.Error
import io.audioshinigami.superd.data.Result.Success
import io.audioshinigami.superd.data.source.FileInfoSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalFileInfoSource internal constructor(
    private val fileInfoDao: FileInfoDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FileInfoSource {

    private val CACHED_PAGE_SIZE = 15

    override fun observeAll(): LiveData<Result<List<FileInfo>>> {
        return Transformations.map( fileInfoDao.observeAll() ){
            Success(it)
        }
    }

    override fun getAllPaged(): LiveData<PagedList<FileInfo>> {

        return LivePagedListBuilder<Int, FileInfo>( fileInfoDao.getAllPaged() ,
            CACHED_PAGE_SIZE ).build()
    }

    override suspend fun getDataResult(): Result<List<FileInfo>> = withContext(ioDispatcher){
        return@withContext try {
            Success( fileInfoDao.getData() )
        }catch (e: Exception ){
            Error(e)
        }
    }

    override fun observeFileData(id: Int): LiveData<Result<FileInfo>> {
        return Transformations.map( fileInfoDao.observeFileInfo(id) ){
            Success(it)
        }
    }

    override suspend fun find(id: Int): FileInfo? = withContext(ioDispatcher){
        return@withContext fileInfoDao.find(id)
    }

    override suspend fun find(getUrl: String): FileInfo? = withContext(ioDispatcher){
        return@withContext fileInfoDao.find(getUrl)
    }

    override suspend fun insert(vararg allFileData: FileInfo) = withContext(ioDispatcher) {
        fileInfoDao.insert( *allFileData )
    }

    override suspend fun update(fileInfo: FileInfo) = withContext(ioDispatcher){
        fileInfoDao.update(fileInfo)
    }

    override suspend fun update(url: String, progress: Int) = withContext(ioDispatcher){
        fileInfoDao.update(url, progress)
    }

    override suspend fun updateRequestId(url: String, id: Int) = withContext(ioDispatcher){
        fileInfoDao.updateRequestId(url, id )
    }

    override suspend fun delete(fileInfo: FileInfo) = withContext(ioDispatcher){
        fileInfoDao.delete(fileInfo)
    }

    override suspend fun delete(url: String): Int = withContext(ioDispatcher){
        return@withContext fileInfoDao.delete( url )
    }

    override suspend fun delete(id: Int): Int = withContext(ioDispatcher){
        return@withContext fileInfoDao.delete(id)
    }

    override suspend fun deleteCompletedFileData(): Int = withContext(ioDispatcher){
        return@withContext fileInfoDao.deleteCompletedFileData()
    }

    override suspend fun deleteAll() = withContext(ioDispatcher){
        fileInfoDao.deleteAll()
    }
}