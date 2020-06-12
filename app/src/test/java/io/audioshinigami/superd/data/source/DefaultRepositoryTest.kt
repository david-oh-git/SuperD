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

package io.audioshinigami.superd.data.source

import com.google.common.truth.Truth.assertThat
import io.audioshinigami.superd.data.Result.Success
import io.audioshinigami.superd.util.FileInfoFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class DefaultRepositoryTest {

    private val allFileInfoData = FileInfoFactory.listOfEntries(7).sortedBy { it.uid }
    private lateinit var localFileInfoSource: FakeInfoDataSource
    private lateinit var downloadDataSource: DownloadDataSource
    private val newFileInfo = FileInfoFactory.singleEntry()

    // Class under test
    private lateinit var repository: DefaultRepository


    @Before
    fun setUp(){
        localFileInfoSource = FakeInfoDataSource( allFileInfoData.toMutableList() )
        downloadDataSource = mock(DownloadDataSource::class.java)

        repository =
            DefaultRepository(
                localFileInfoSource,
                downloadDataSource,
                Dispatchers.Unconfined
            )
    }

    @Test
    fun getAllFileInfo_requestFromLocalSource() = runBlockingTest {
        // Arrange : data already added to repository

        // Act : request all fileInfo from FileInfo repository
        val allFileInfo = repository.getAllFileInfo() as Success

        //Assert :
        assertThat( allFileInfo.data , `is`(allFileInfoData) )
    }

    @Test
    fun saveFileInfo_saveToLocalSource() = runBlockingTest {
        // Arrange : create a new fileInfo that is not in localSource
        assertThat( localFileInfoSource.dbData ).doesNotContain( newFileInfo )
        assertThat( (repository.getAllFileInfo() as? Success)?.data ).doesNotContain( newFileInfo )

        // Act : save a fileInfo to the repository
        repository.save( newFileInfo )

        //Assert : new fileInfo is saved
        val result =  repository.getAllFileInfo() as? Success
        assertThat( result?.data ).contains( newFileInfo )
    }

    @Test
    fun findFileInfo_returnFileInfoOrNull() = runBlockingTest {
        // Arrange : create and save fileInfo
        val fileInfo = FileInfoFactory.singleEntry().copy( uid = 89)
        assertThat( localFileInfoSource.dbData ).doesNotContain( fileInfo )
        assertThat( (repository.getAllFileInfo() as? Success)?.data ).doesNotContain( fileInfo )
        repository.save( fileInfo )

        //Act : search for fileInfo
        val result = repository.find(89)

        // Assert :
        val allFileInfo = repository.getAllFileInfo() as? Success
        assertThat( allFileInfo?.data ).contains( result )
        assertThat( fileInfo, `is`(result) )
    }

    @Test
    fun updateFileInfo_() = runBlockingTest {
        // Arrange : create a new fileInfo that is not in localSource, save it
        assertThat( localFileInfoSource.dbData ).doesNotContain( newFileInfo )
        assertThat( (repository.getAllFileInfo() as? Success)?.data ).doesNotContain( newFileInfo )
        repository.save( newFileInfo )

        // Act : update newFileInfo
        val updatedFileInfo = newFileInfo.copy( progressValue = 0 )
        repository.update( updatedFileInfo )


        // Assert: confirm data was updated
        val result = repository.find( newFileInfo.uid )
//        assertThat( result, isNotNull())
        assertThat( result , `is`(updatedFileInfo) )
    }

    @Test
    fun pauseDownload_verifyPauseIsCalled() = runBlockingTest {

        // Act : pause
        repository.pause( 22 )

        // Assert : pause is called
        verify(downloadDataSource).pause(22)
    }

    @Test
    fun resumeDownload_verifyResume() = runBlockingTest {

        // Act : pause
        repository.resume( 22 )

        // Assert : pause is called
        verify(downloadDataSource).resume(22)
    }

}