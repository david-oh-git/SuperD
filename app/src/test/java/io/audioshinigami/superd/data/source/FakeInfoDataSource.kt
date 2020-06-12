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

package io.audioshinigami.superd.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.audioshinigami.superd.data.FileInfo
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.data.Result.Success

class FakeInfoDataSource (
    var dbData: MutableList<FileInfo>? = mutableListOf()
) : FileInfoSource {

    override fun observeAll(): LiveData<Result<List<FileInfo>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllPaged(): LiveData<PagedList<FileInfo>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getDataResult(): Result<List<FileInfo>> {
        dbData?.let { return Success(it) }

        return Result.Error( Exception("No data found !!"))
    }

    override fun observeFileData(id: Int): LiveData<Result<FileInfo>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun find(id: Int): FileInfo? {
        return dbData?.find { it.uid == id }
    }

    override suspend fun find(getUrl: String): FileInfo? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun insert(vararg allFileData: FileInfo) {
        for( data in allFileData){
            dbData?.add(data)
        }
    }

    override suspend fun update(fileInfo: FileInfo) {
        val replace = dbData?.find { it.uid == fileInfo.uid }

        dbData?.indexOf(replace)?.run {

            dbData?.remove( replace )

            dbData?.add( this, fileInfo)
        }

    }

    override suspend fun update(url: String, progress: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateRequestId(url: String, id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun delete(fileInfo: FileInfo) {
        dbData?.removeAll{
            it == fileInfo
        }

    }

    override suspend fun delete(url: String): Int {
        val numberDeleted = dbData?.filter { it.url == url }?.size ?: 0
        dbData?.removeAll{
            it.url == url
        }

        return numberDeleted
    }

    override suspend fun delete(id: Int): Int {
        val numberDeleted = dbData?.filter { it.request_id == id }?.size ?: 0
        dbData?.removeAll{
            it.request_id == id
        }

        return numberDeleted
    }

    override suspend fun deleteCompletedFileData(): Int {
        val size = dbData?.filter { it.progressValue == 100 }?.size ?: 0
        dbData?.removeAll { it.progressValue == 100 }

        return size
    }

    override suspend fun deleteAll() {
        dbData?.clear()
    }
}