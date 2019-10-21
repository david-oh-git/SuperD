package io.audioshinigami.superd.data.repository

interface DownloadRepository {

    suspend fun start(url: String )

    suspend fun restart(url: String)

    suspend fun pause(id: Int)

    suspend fun resume(id: Int)
}