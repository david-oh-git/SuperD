package io.audioshinigami.superd.zdata.source

import io.audioshinigami.superd.data.source.FakeInfoDataSource
import io.audioshinigami.superd.util.FileInfoFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before


@ExperimentalCoroutinesApi
class DefaultFileInfoRepositoryTest {

    private val localDbData = FileInfoFactory.listOfEntries(7)
    private lateinit var localFileInfoSource: FakeInfoDataSource
    private lateinit var downloadDataSource: DownloadDataSource


    @Before
    fun setUp(){
        localFileInfoSource = FakeInfoDataSource( localDbData.toMutableList() )
        downloadDataSource =
    }
}