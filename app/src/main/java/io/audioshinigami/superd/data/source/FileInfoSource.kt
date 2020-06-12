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