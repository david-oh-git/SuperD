package io.audioshinigami.superd.data.source.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.audioshinigami.superd.data.source.db.FileDatabase
import io.audioshinigami.superd.factory.FileDataFactory
import io.audioshinigami.superd.util.TestCoroutineRule
import io.audioshinigami.superd.util.observeOnce
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
*Test class for [FileDataDao] db interface
*
*/

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class FileDataDaoTest {

    private lateinit var db: FileDatabase
    // Executes each test synchronously using Architecture Components.
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

        //Act : get fileData via findByUrl .
        val result = db.fileDataDao().findByUrl(fileData.url)

        //Assert : confirm data from db is same as data added
        result?.apply {
            assertThat(this , notNullValue())
            assertThat(url, `is`(fileData.url))
            assertThat(fileName, `is`(fileData.fileName))
            assertThat(progressValue, `is`(fileData.progressValue))
            assertThat(request_id, `is`(fileData.request_id))
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


        // Assert : get data from db with same id & test contents
        val retrievedFileData = db.fileDataDao().find(customUid)

        retrievedFileData?.apply {
            assertThat(uid, `is`(newFileData.uid))
            assertThat(progressValue, `is`(newFileData.progressValue))
            assertThat(request_id, `is`(newFileData.request_id))
            assertThat(fileName, `is`(newFileData.fileName))
            assertThat(url, `is`(newFileData.url))
            assertThat(isCompleted, `is`(newFileData.isCompleted))
        }

    } // END

    @Test
    fun insertFileDataAndGetFileData() = runBlockingTest {
        // Arrange : save a FileData
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
        // Arrange : save FileData
        val fileData = FileDataFactory.makeFileDataEntry().copy(uid = 78)
        db.fileDataDao().insert(fileData)

        // Act : updateProgressvalue FileData
        val updatedFileData =  fileData.copy(progressValue = 100, request_id = 8476473)
        db.fileDataDao().update(updatedFileData)

        // Assert : check if data was updated
        val retrievedFileData = db.fileDataDao().find(fileData.uid)

        retrievedFileData?.apply {
            assertThat(uid, `is`(updatedFileData.uid))
            assertThat(progressValue, `is`(100))
            assertThat(request_id, `is`(8476473) )
            assertThat(isCompleted, `is`(true))
        }
    } // END

    @Test
    fun deleteFileDataByIdAndGetFileData() = runBlockingTest{
        // Arrange save data into db
        val fileData = FileDataFactory.makeFileDataEntry().copy(uid = 60)
        db.fileDataDao().insert(fileData)

        // Act : delete data with uid
        db.fileDataDao().delete(fileData.uid)

        //Assert
        db.fileDataDao().getAll().observeOnce {
            data ->
            assertThat(data.isEmpty(), `is`(true))
        }
    } //END

    @Test
    fun deleteFileDataAndGetAllData() = runBlockingTest {
        // Arrange : save data to db
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
        // Arrange : save completed tasks
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
        //Arrange : save data into db
        val fileData = FileDataFactory.makeFileDataEntry().copy(uid = 69)
        val anotherData = FileDataFactory.makeFileDataEntry()
        db.fileDataDao().insert(fileData)
        db.fileDataDao().insert(anotherData)

        //Act : delete data with uid
        val numberOfDeleted = db.fileDataDao().delete(69)

        //Assert confirm data is deleted & it returns 1
        assertThat(1, `is`(numberOfDeleted))
    } //END

    @Test
    fun deleteCompletedAndGetNumberOfDeletedData() = runBlockingTest {
        // Arrange : create & save complete & also incomplete FileData
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
        db.fileDataDao().insert(completedFileData, secondCompleteFileData, fileData)

        // Act : get all data from db
        db.fileDataDao().getAll().observeOnce {
            assertThat(it.size, `is`(3))
        }
    }

    @Test
    fun insertFileDataAndUpdateProgressValue() = runBlockingTest {
        // Arrange : add data to db with progress value: 0
        val fileData = FileDataFactory.makeFileDataEntry().copy( progressValue = 0)
        db.fileDataDao().insert(fileData)

        // Act: update the progress value
        val newProgressValue = 23
        db.fileDataDao().update(fileData.url, newProgressValue)

        // retrieve fileData & confirm progress value was updated
        db.fileDataDao().getAll().observeOnce {
            assertThat(it.isEmpty(), `is`(false)) // check data not empty
            assertThat(it[0].progressValue, `is`(newProgressValue))
        }
    }

    @Test
    fun insertMultipleDataAndUpdateProgressValues() = runBlockingTest {
        // Arrange: add multiple data
        val data = FileDataFactory.makeFileDataEntry()
        val data2 = FileDataFactory.makeFileDataEntry()
        val data3 = FileDataFactory.makeFileDataEntry()
        val data4 = FileDataFactory.makeFileDataEntry()
        val data5 = FileDataFactory.makeFileDataEntry()

        db.fileDataDao().insert(data, data2, data3, data4, data5)

        // Act: update progress values
        val progress = 77
        val progress2 = 34
        val progress3 = 100
        val progress4 = 25
        val progress5 = 42
        db.fileDataDao().update( data.url, progress)
        db.fileDataDao().update( data2.url, progress2)
        db.fileDataDao().update( data3.url, progress3)
        db.fileDataDao().update( data4.url, progress4)
        db.fileDataDao().update( data5.url, progress5)

        // retrieve data from db & assert values
        db.fileDataDao().getAll().observeOnce {
            assertThat(it.isEmpty(), `is`(false)) // check data not empty

            for( obj in it ){

                when(obj.url){

                    data.url -> assertProgressValue( obj.progressValue , progress )
                    data2.url -> assertProgressValue( obj.progressValue , progress2 )
                    data3.url -> assertProgressValue( obj.progressValue , progress3 )
                    data4.url -> assertProgressValue( obj.progressValue , progress4 )
                    data5.url -> assertProgressValue( obj.progressValue , progress5 )
                }

            }
        }
    } // END insertMultipleDataAndUpdateProgressValues

    @Test
    fun insertMultipleDataWithSameUrl() = runBlockingTest {
        // Arrange: create multiple FileData with same url
        val fileData = FileDataFactory.makeFileDataEntry()
        val fileData2 =  FileDataFactory.makeFileDataEntry().copy( url = fileData.url )

        // Act : save them
        db.fileDataDao().apply {
            insert(fileData)
            insert(fileData2)
        }

        // Assert:

        db.fileDataDao().getAll().observeOnce {

            assertThat(it.size, `is`(1))
            assertThat(it[0].url , `is`(fileData.url) )
        }

    }

    @Test
    fun deleteFileDataByUrlAndGetFileData() = runBlockingTest{
        // Arrange save data into db
        val fileData = FileDataFactory.makeFileDataEntry()
        db.fileDataDao().insert(fileData)

        // Act : delete data with URL
        db.fileDataDao().delete( fileData.url )

        //Assert
        db.fileDataDao().getAll().observeOnce {
                data ->
            assertThat(data.isEmpty(), `is`(true))
        }
    } //END

    @Test
    fun deleteFileDataByUrl_partII() = runBlockingTest{
        // Arrange save data into db
        val fileData = FileDataFactory.makeFileDataEntry()
        val filedata2 =  FileDataFactory.makeFileDataEntry()
        db.fileDataDao().insert(fileData, filedata2 )

        // Act : delete data with URL
        val numberOfDeletedItems: Int = db.fileDataDao().delete( fileData.url )

        //Assert
        db.fileDataDao().getAll().observeOnce {
                data ->
            assertThat(data.size, `is`(1))
            assertThat( numberOfDeletedItems , `is`(1) )
        }
    } //END

    private fun assertProgressValue(progressValue: Int, updatedProgressValue: Int ){
        assertThat( progressValue, `is`(updatedProgressValue))
    }

}