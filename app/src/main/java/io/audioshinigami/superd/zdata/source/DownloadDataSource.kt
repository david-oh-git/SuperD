package io.audioshinigami.superd.zdata.source

import android.net.Uri
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2.NetworkType
import com.tonyodev.fetch2.Priority

interface DownloadDataSource {

    var _priority: Priority
    var _networkType: NetworkType

    suspend fun start(url: String, downloadUri: Uri )

    suspend fun restart(url: String, downloadUri: Uri )

    fun pause(id: Int)

    fun resume(id: Int)

//    suspend fun isActive(id: Int ): Boolean?

    suspend fun setListener(fetchListener: FetchListener)

    fun isDownloading(): Boolean

    fun hasActiveListener(): Boolean

    fun disableListener()

    fun addState(id: Int, isActive: State)
}