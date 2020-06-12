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