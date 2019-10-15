package io.audioshinigami.superd.data.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.audioshinigami.superd.util.TestCoroutineRule
import io.audioshinigami.superd.data.db.FileDatabase
import io.audioshinigami.superd.data.db.entity.FileData
import io.audioshinigami.superd.factory.FileDataFactory
import io.audioshinigami.superd.util.observeOnce
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.*
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`

/*
*Test class for [FileDataDao] db interface
*
*/

@ExperimentalCoroutinesApi
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

        //Act : get fileData via findByUrl . returns LivaData hence
        db.fileDataDao().findByUrl(fileData.url).observeOnce {

            //Assert : confirm data from db is same as data added
            assertThat(it as FileData, notNullValue())
            assertThat(it.url, `is`(fileData.url))
            assertThat(it.fileName, `is`(fileData.fileName))
            assertThat(it.progressValue, `is`(fileData.progressValue))
            assertThat(it.request_id, `is`(fileData.request_id))
        }

    }

    @Test
    fun insertFileDataAndGetAllData() = runBlockingTest{
        //Arrange
        val mockAllFileData = FileDataFactory.makeListOfFileData(2)

        mockAllFileData.forEach {
            db.fileDataDao().insert(it)
        }

        //Act : get all data from db
        db.fileDataDao().getAll().observeOnce {
            retrievedData ->
            assertThat(retrievedData.size, `is`(2))
        }

        //Assert
//        Assert.assertEquals(mockAllFileData, retrievedAllFileData?.sortedWith( compareBy({it.uid}, {it.uid})) )
    }

    @Test
    fun insertFileDataReplaceOnConflict() = runBlockingTest {
        // Arrange [FileData] is added to db, providing uid for testing
        val customUid = 45
        val fileData = FileDataFactory.makeFileDataEntry().copy(uid = customUid)
        db.fileDataDao().insert(fileData)

        // Act : when another task with same id is inserted, it should replace it
        val newFileData = FileDataFactory.makeFileDataEntry().copy(uid = customUid )
        db.fileDataDao().insert(newFileData)
        // Assert
        db.fileDataDao().findById(customUid).observeOnce {
            retrievedFileData ->
            assertThat(retrievedFileData?.uid, `is`(newFileData.uid))
            assertThat(retrievedFileData?.progressValue, `is`(newFileData.progressValue))
            assertThat(retrievedFileData?.request_id, `is`(newFileData.request_id))
            assertThat(retrievedFileData?.fileName, `is`(newFileData.fileName))
            assertThat(retrievedFileData?.url, `is`(newFileData.url))
            assertThat(retrievedFileData?.isCompleted, `is`(newFileData.isCompleted))
        }

    } // END

    @Test
    fun insertFileDataAndGetFileData() = runBlockingTest {
        // Arrange : insert a FileData
        val fileData = FileDataFactory.makeFileDataEntry()
        db.fileDataDao().insert(fileData)

        // Act : get all [FileData] from db
        db.fileDataDao().getAll().observeOnce {
            allFileData ->
            // Assert : there should be just one FileData in db
            assertThat(allFileData.size, `is`(1) )
            assertThat(allFileData[0].url, `is`(fileData.url) )
            assertThat(allFileData[0].fileName, `is`(fileData.fileName) )
            assertThat(allFileData[0].request_id, `is`(fileData.request_id) )
            assertThat(allFileData[0].progressValue, `is`(fileData.progressValue))
            assertThat(allFileData[0].isCompleted, `is`(fileData.isCompleted))
        }

    } //END

    @Test
    fun updateCompletedFileDataAndGetById() = runBlockingTest {
        // Arrange : insert FileData
        val fileData = FileDataFactory.makeFileDataEntry().copy(uid = 78)
        db.fileDataDao().insert(fileData)

        // Act : updateFileData FileData
        val updatedFileData =  fileData.copy(progressValue = 100, request_id = 8476473)
        db.fileDataDao().updateFileData(updatedFileData)

        // Assert : check if data was updated
        db.fileDataDao().findById(fileData.uid).observeOnce {
            retrievedFileData ->
            assertThat(retrievedFileData?.uid, `is`(updatedFileData.uid))
            assertThat(retrievedFileData?.progressValue, `is`(100))
            assertThat(retrievedFileData?.request_id, `is`(8476473) )
            assertThat(retrievedFileData?.isCompleted, `is`(true))

        }
    } // END

    @Test
    fun deleteFileDataByIdAndGetFileData() = runBlockingTest{
        // Arrange insert data into db
        val fileData = FileDataFactory.makeFileDataEntry().copy(uid = 60)
        db.fileDataDao().insert(fileData)

        // Act : delete data with uid
        db.fileDataDao().deleteById(fileData.uid)

        //Assert
        db.fileDataDao().getAll().observeOnce {
            data ->
            assertThat(data.isEmpty(), `is`(true))
        }
    } //END

    @Test
    fun deleteFileDataAndGetAllData() = runBlockingTest {
        // Arrange : insert data to db
        val fileData = FileDataFactory.makeFileDataEntry()
        db.fileDataDao().insert(fileData)
        val anotherFileData = FileDataFactory.makeFileDataEntry()
        db.fileDataDao().insert(anotherFileData)

        //Act : deletes all data in db
        db.fileDataDao().deleteAll()

        // Assert : should return null for an empty db
        db.fileDataDao().getAll().observeOnce {
            assertThat(it.isEmpty(), `is`(true))
        }
    } // END

    @Test
    fun deleteCompletedFileDataAndGetFileData() = runBlockingTest {
        // Arrange : insert completed tasks
        val fileDataOne = FileDataFactory.makeFileDataEntry().copy(progressValue = 100)
        val anodaFileData = FileDataFactory.makeFileDataEntry().copy(progressValue = 100)
        db.fileDataDao().insert(fileDataOne)
        db.fileDataDao().insert(anodaFileData)

        //Act
        db.fileDataDao().deleteCompletedFileData()

        // Assert
        db.fileDataDao().getAll().observeOnce {
            assertThat(it.isEmpty(), `is`(true))
        }
    } //END

    @Test
    fun deleteByIdGetNumberOfDeletedData() = runBlockingTest {
        //Arrange : insert data into db
        val fileData = FileDataFactory.makeFileDataEntry().copy(uid = 69)
        val anotherData = FileDataFactory.makeFileDataEntry()
        db.fileDataDao().insert(fileData)
        db.fileDataDao().insert(anotherData)

        //Act : delete data with uid
        val numberOfDeleted = db.fileDataDao().deleteById(69)

        //Assert confirm data is deleted & it returns 1
        assertThat(1, `is`(numberOfDeleted))
    } //END

    @Test
    fun deleteCompletedAndGetNumberOfDeletedData() = runBlockingTest {
        // Arrange : create & insert complete & also incomplete FileData
        val completedFileData = FileDataFactory.makeFileDataEntry().copy(progressValue = 100)
        val secondCompleteFileData = FileDataFactory.makeFileDataEntry().copy( progressValue = 100)
        val fileData = FileDataFactory.makeFileDataEntry().copy(progressValue = 12)
        db.fileDataDao().insert(completedFileData)
        db.fileDataDao().insert(secondCompleteFileData)
        db.fileDataDao().insert(fileData)

        // Act : delete completed FileData & get the return value i.e 2
        val numberOfDeleted = db.fileDataDao().deleteCompletedFileData()

        // Assert
        assertThat(2, `is`(numberOfDeleted))

    } //END

    @ExperimentalCoroutinesApi
    @Test /* use insertAll to add multiple data to db*/
    fun insertFileDataGetNumberOfData() = runBlockingTest {
        // Arrange : create & add multiple data to db using insertAll
        val completedFileData = FileDataFactory.makeFileDataEntry().copy(progressValue = 100)
        val secondCompleteFileData = FileDataFactory.makeFileDataEntry().copy( progressValue = 100)
        val fileData = FileDataFactory.makeFileDataEntry().copy(progressValue = 12)
        db.fileDataDao().insertAll(completedFileData, secondCompleteFileData, fileData)

        // Act : get all data from db
        db.fileDataDao().getAll().observeOnce {
            assertThat(it.size, `is`(3))
        }
    }

}