package io.audioshinigami.superd.data.repository

interface DownloadRepository {

    suspend fun start(url: String )

    suspend fun restart(url: String)

    fun pause(id: Int)

    fun resume(id: Int)
}