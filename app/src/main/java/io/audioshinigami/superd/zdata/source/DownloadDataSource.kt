package io.audioshinigami.superd.zdata.source

import android.net.Uri
import androidx.lifecycle.LiveData
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.NetworkType
import com.tonyodev.fetch2.Priority

interface DownloadDataSource {

    var _priority: Priority
    var _networkType: NetworkType
    val isActive: Map<Int, Boolean>

    suspend fun start(url: String, downloadUri: Uri )

    suspend fun restart(url: String, downloadUri: Uri )

    fun pause(id: Int)

    fun resume(id: Int)

    suspend fun delete(id: Int): Fetch

    suspend fun remove(id: Int): Fetch

    suspend fun update( url: String , progress: Int )

    suspend fun isActive(id: Int ): Boolean?

    suspend fun isDownloading(): LiveData<Boolean>
}