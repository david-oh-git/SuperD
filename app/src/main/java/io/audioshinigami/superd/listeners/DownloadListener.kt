package io.audioshinigami.superd.listeners

import io.audioshinigami.superd.data.db.entity.FileData

interface DownloadListener {

    fun onPauseDownload(id: Int)
    fun onResumeDownload( id: Int)
    fun onStartDownload(urlString: String)
    fun onCompleteDownload(fileData: FileData)
}