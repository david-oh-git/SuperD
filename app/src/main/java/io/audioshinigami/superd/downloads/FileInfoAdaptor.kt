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

package io.audioshinigami.superd.downloads

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import io.audioshinigami.superd.data.FileInfo
import io.audioshinigami.superd.databinding.ItemBinding

/*
* Adaptor for download items
* recyclerView
* */
class FileInfoAdaptor(
    private val downloadItemActions: DownloadItemActions
):
    PagedListAdapter<FileInfo, FileInfoViewHolder>(FileDataDiffCallback()) {

    override fun onBindViewHolder(holder: FileInfoViewHolder, position: Int) {
        val fileInfo = getItem(position)

        fileInfo?.apply {
            holder.bind( downloadItemActions ,this )
        } ?: holder.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileInfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ItemBinding.inflate(layoutInflater, parent, false)

        return FileInfoViewHolder(binding)
    }

    private class FileDataDiffCallback: DiffUtil.ItemCallback<FileInfo>() {

        override fun areItemsTheSame(oldItem: FileInfo, newItem: FileInfo): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: FileInfo, newItem: FileInfo): Boolean {
            return oldItem == newItem
        }
    }
}