package io.audioshinigami.superd.addownload

import io.audioshinigami.superd.data.source.db.entity.FileData
import io.audioshinigami.superd.utility.recyclerview.BaseAdaptor

class DownloadAdaptor(private val layoutID: Int ):
 BaseAdaptor() {

    private var data: List<FileData>? = null

    override fun getLayoutIdForPosition(position: Int) = layoutID

    override fun getPositionDataObject(position: Int) = data?.get(position) as FileData

    override fun getItemCount() = data?.size ?: 0
}