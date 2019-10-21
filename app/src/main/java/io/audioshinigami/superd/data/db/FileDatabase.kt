package io.audioshinigami.superd.data.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.audioshinigami.superd.data.db.dao.FileDataDao
import io.audioshinigami.superd.data.db.entity.FileData

@Database(entities = [FileData::class], version = 1 )
abstract class FileDatabase : RoomDatabase(){
    abstract fun fileDataDao(): FileDataDao

//    private class FileDatabaseCallback(private val fileData: FileData, val scope: CoroutineScope):
//        RoomDatabase.Callback(){
//
//        override fun onOpen(db: SupportSQLiteDatabase) {
//            super.onOpen(db)
//            INSTANCE?.let {
//                fileDatabase -> scope.launch(Dispatchers.IO){
//                    addtoDatabase(fileDatabase.fileDataDao(), fileData)
//                }
//            }
//        }/*end onOpen*/
//
//        fun addtoDatabase(fileDataDao: FileDataDao, fileData: FileData){
//            fileDataDao.save(fileData)
//        }
//    }/*end FileDBcallback*/

    companion object{

        @Volatile
        private var INSTANCE: FileDatabase? = null
        const val DB_FILENAME = "filedownload_db"


        fun getDbInstance(context: Context): FileDatabase {
            /*returns a db instance*/
            val tempInstance = INSTANCE
            if( tempInstance != null ){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    FileDatabase::class.java,
                    DB_FILENAME
                ).build()
                INSTANCE = instance
                return instance
            }

        } //end getDbInstance

    } // end companion object
} //end Filedatabase