package io.audioshinigami.superd.data.repository

interface DownloadDataSource {

    suspend fun start(url: String )

    suspend fun restart(url: String)

    fun pause(id: Int)

    fun resume(id: Int)
}