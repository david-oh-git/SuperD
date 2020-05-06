package io.audioshinigami.superd.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.audioshinigami.superd.common.FILE_DB_NAME
import io.audioshinigami.superd.data.source.FileInfoSource
import io.audioshinigami.superd.data.source.local.FileDatabase
import io.audioshinigami.superd.data.source.local.FileInfoDao
import io.audioshinigami.superd.data.source.local.LocalFileInfoSource
import javax.inject.Singleton

@Module
class DataStorageModule {

    @Provides
    @Singleton
    fun provideFileDatabase(context: Context): FileDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            FileDatabase::class.java, FILE_DB_NAME
        ).build()

    @Provides
    @Singleton
    fun provideFileInfoDao( fileDatabase: FileDatabase ): FileInfoDao =
        fileDatabase.fileDataDao()

    @Provides
    @Singleton
    fun provideFileInfoDataSource(fileInfoDao: FileInfoDao): FileInfoSource =
        LocalFileInfoSource( fileInfoDao )
}