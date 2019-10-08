package io.audioshinigami.superd.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.audioshinigami.superd.TestCoroutineRule
import io.audioshinigami.superd.db.FileDatabase
import io.audioshinigami.superd.db.entity.FileData
import io.audioshinigami.superd.factory.FileDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.*
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat

/*
*Test class for [FileDataDao] db interface
*
*/

@RunWith(AndroidJUnit4::class)
class FileDataDaoTest {

    private lateinit var db: FileDatabase
    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    @Before
    fun initDb(){
        db = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            FileDatabase::class.java
            ).allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() = db.close()

    @Test
    fun insertFileDataAndGetById() = runBlockingTest {
        //Arrange add a fileData
        val fileData = FileDataFactory.makeFileDataEntry()
        db.fileDataDao().insert(fileData)

        //Act get filedata by url
        val loaded = db.fileDataDao().searchWithUrl(fileData.url)

        //Assert
        assertThat<FileData>(loaded as FileData, notNullValue())
    }

    @Test
    fun getAll_retrievesData(){
        //Arrange
        val mockAllFileData = FileDataFactory.makeListOfFileData(5)

        mockAllFileData.forEach {
            runBlocking { db.fileDataDao().insert(it) }
        }

        //Act
        val retrievedAllFileData = db.fileDataDao().getAll().value

        //Assert
        Assert.assertEquals(mockAllFileData, retrievedAllFileData?.sortedWith( compareBy({it.uid}, {it.uid})))
    }

    @Test
    fun insertFileData_savesData(){
        //Arrange
        val mockFileData = FileDataFactory.makeFileDataEntry()

        //Act
        runBlocking { db.fileDataDao().insert(mockFileData) }

        // Assert
        val allFileData = db.fileDataDao().getAll().value
        assert( allFileData!!.isNotEmpty())

    }
}