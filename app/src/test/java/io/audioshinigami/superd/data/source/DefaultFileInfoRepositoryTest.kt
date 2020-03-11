package io.audioshinigami.superd.data.source

import io.audioshinigami.superd.util.FileInfoFactory
import io.audioshinigami.superd.data.Result.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class DefaultFileInfoRepositoryTest {

    private val allFileInfoData = FileInfoFactory.listOfEntries(7).sortedBy { it.uid }
    private lateinit var localFileInfoSource: FakeInfoDataSource
    private lateinit var downloadDataSource: DownloadDataSource
    private val newFileInfo = FileInfoFactory.singleEntry()

    // Class under test
    private lateinit var fileInfoRepository: DefaultFileInfoRepository


    @Before
    fun setUp(){
        localFileInfoSource = FakeInfoDataSource( allFileInfoData.toMutableList() )
        downloadDataSource = mock(DownloadDataSource::class.java)

        fileInfoRepository =
            DefaultFileInfoRepository(
                localFileInfoSource,
                downloadDataSource,
                Dispatchers.Unconfined
            )
    }

    @Test
    fun getAllFileInfo_requestFromLocalSource() = runBlockingTest {
        // Arrange : data already added to repository

        // Act : request all fileInfo from FileInfo repository
        val allFileInfo = fileInfoRepository.getAllFileInfo() as Success

        //Assert :
        assertThat( allFileInfo.data , `is`(allFileInfoData) )
    }

    @Test
    fun saveFileInfo_saveToLocalSource() = runBlockingTest {
        // Arrange : create a new fileInfo that is not in localSource
        assertThat( localFileInfoSource.dbData ).doesNotContain( newFileInfo )
        assertThat( (fileInfoRepository.getAllFileInfo() as? Success)?.data ).doesNotContain( newFileInfo )

        // Act : save a fileInfo to the repository
        fileInfoRepository.save( newFileInfo )

        //Assert : new fileInfo is saved
        val result =  fileInfoRepository.getAllFileInfo() as? Success
        assertThat( result?.data ).contains( newFileInfo )
    }

    @Test
    fun findFileInfo_returnFileInfoOrNull() = runBlockingTest {
        // Arrange : create and save fileInfo
        val fileInfo = FileInfoFactory.singleEntry().copy( uid = 89)
        assertThat( localFileInfoSource.dbData ).doesNotContain( fileInfo )
        assertThat( (fileInfoRepository.getAllFileInfo() as? Success)?.data ).doesNotContain( fileInfo )
        fileInfoRepository.save( fileInfo )

        //Act : search for fileInfo
        val result = fileInfoRepository.find(89)

        // Assert :
        val allFileInfo = fileInfoRepository.getAllFileInfo() as? Success
        assertThat( allFileInfo?.data ).contains( result )
        assertThat( fileInfo, `is`(result) )
    }

    @Test
    fun updateFileInfo_() = runBlockingTest {
        // Arrange : create a new fileInfo that is not in localSource, save it
        assertThat( localFileInfoSource.dbData ).doesNotContain( newFileInfo )
        assertThat( (fileInfoRepository.getAllFileInfo() as? Success)?.data ).doesNotContain( newFileInfo )
        fileInfoRepository.save( newFileInfo )

        // Act : update newFileInfo
        val updatedFileInfo = newFileInfo.copy( progressValue = 0 )
        fileInfoRepository.update( updatedFileInfo )


        // Assert: confirm data was updated
        val result = fileInfoRepository.find( newFileInfo.uid )
//        assertThat( result, isNotNull())
        assertThat( result , `is`(updatedFileInfo) )
    }

    @Test
    fun pauseDownload_verifyPauseIsCalled() = runBlockingTest {

        // Act : pause
        fileInfoRepository.pause( 22 )

        // Assert : pause is called
        verify(downloadDataSource).pause(22)
    }

    @Test
    fun resumeDownload_verifyResume() = runBlockingTest {

        // Act : pause
        fileInfoRepository.resume( 22 )

        // Assert : pause is called
        verify(downloadDataSource).resume(22)
    }

}