package io.audioshinigami.superd.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.audioshinigami.superd.data.source.db.FileDatabase
import io.audioshinigami.superd.factory.FileDataFactory
import io.audioshinigami.superd.util.TestCoroutineRule
import io.audioshinigami.superd.util.observeOnce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DefaultRepositoryTest {

    private lateinit var defaultRepository: DefaultRepository
    private lateinit var database : FileDatabase

    // run each test synchronously
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // set coroutines despatcher for test
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    @Before
    fun init(){
        // using in memory database for tests
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext() ,
            FileDatabase::class.java )
            .allowMainThreadQueries()
            .build()

        defaultRepository = DefaultRepository(database.fileDataDao(), Dispatchers.Main)
    } //end init

    @After
    fun cleanUp() = database.close()

    @Test
    fun saveFileData_getFileData() = runBlockingTest {
        // Arrange : create data & save to database
        val testUid = 62
        val fileData =  FileDataFactory.makeFileDataEntry().copy(uid = testUid)
        defaultRepository.save(fileData)

        // Act : retrieve data by id
        val result = defaultRepository.getById(testUid)

        result?.apply {
            assertThat(uid, `is`(testUid) )
            assertThat(fileName, `is`(fileData.fileName) )
            assertThat(progressValue, `is`(fileData.progressValue) )
            assertThat(url, `is`(fileData.url) )
            assertThat(isCompleted, `is`(fileData.isCompleted))
        }
    } //END

    @Test
    fun completeFileData_getCompleteFileData() = runBlockingTest {
        // Arrange : create completed( progressValue of 100%) FileData & add to database
        val testUid = 16
        val fileData = FileDataFactory.makeFileDataEntry().copy(uid = testUid, progressValue = 100)
        defaultRepository.save(fileData)

        // Act : get the fileData by id
        val result = defaultRepository.getById(testUid)

        // Assert : test if retrieved data is complete
        result?.apply {
            assertThat(uid, `is`(testUid))
            assertThat(isCompleted, `is`(fileData.isCompleted))
        }


    }//END

    @Test
    fun addIncompletedFileData_retrieveIncompleteFileData() = runBlockingTest {
        // Arrange : create incomplete fileData & add to database
        val testUid = 72
        val fileData = FileDataFactory.makeFileDataEntry().copy(uid = testUid, progressValue = 74)
        defaultRepository.save(fileData)

        //Act : retrieve the data from db
        val result = defaultRepository.getById(testUid)

        // Assert ; check data is incomplete( progressValue != 100)
        result?.apply {
            assertThat(uid, `is`(testUid))
            assertThat(isCompleted, `is`(false) )
        }

    } //END

    @Test
    fun clearCompletedFileData_dataIsNull() = runBlockingTest {
        // Arrange : create mixture of complete & incomplete, then save the data to db
        val completeUid = 23
        val completeUid2 = 56
        val testUid = 57
        val completeFileData = FileDataFactory.makeFileDataEntry().copy(progressValue = 100, uid = completeUid)
        val completeFileData2 = FileDataFactory.makeFileDataEntry().copy(progressValue = 100, uid = completeUid2)
        val fileData = FileDataFactory.makeFileDataEntry().copy(progressValue = 34, uid = testUid)
        defaultRepository.saveAll( completeFileData, completeFileData2, fileData)

        // Act :  clear all completed data
        defaultRepository.clearCompleted()

        // Assert : attempt to retrieve data
        val result = defaultRepository.getById(completeUid)
        val result2 = defaultRepository.getById(completeUid2)
        // complete data should be null
        assertThat( result == null , `is`(true) )
        assertThat( result2 == null , `is`(true))

        // for incomplete data
        val result3 = defaultRepository.getById(testUid)
        assertThat(result3, notNullValue())
        assertThat(result3, `is`(fileData))

    } //END

    @Test
    fun deleteAllFileData_getEmptyList() = runBlockingTest {
        // Arrange : add multiple data to db
        val fileData = FileDataFactory.makeFileDataEntry()
        val fileData2 = FileDataFactory.makeFileDataEntry()

        defaultRepository.saveAll(fileData, fileData2)

        // Act : delete all data
        defaultRepository.deleteAll()

        // Assert : get all data from db , it should be empty
        defaultRepository.getAll().observeOnce {
            result ->
            assertThat(result.isEmpty(), `is`(true))
        }
    }//END

    @Test
    fun addFileData_retrieveSavedData() = runBlockingTest {
        // Assert : add multiple data & save to db
        val fileData = FileDataFactory.makeFileDataEntry()
        val fileData2 = FileDataFactory.makeFileDataEntry()
        defaultRepository.saveAll(fileData, fileData2)

        // Act : get all data from db
        defaultRepository.getAll().observeOnce {
            // Assert : db contains correct number of data
            result ->
            assertThat(result.size, `is`(2))
        }
    } //END

}// END class