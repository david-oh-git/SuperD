package io.audioshinigami.superd.factory

import io.audioshinigami.superd.db.entity.FileData
import io.audioshinigami.superd.factory.DataFactory.randomRequestId
import io.audioshinigami.superd.factory.DataFactory.randomProgressValue
import io.audioshinigami.superd.factory.DataFactory.randomUrl

/*
* creates mock FileData instances for test purposes
* */

object FileDataFactory {

    fun makeFileDataEntry(): FileData {

        /* create a FIleDat instance with random values*/
        val url =  randomUrl()
        val fileName = url.substringAfter('/')
        return FileData(0, randomRequestId(), url, fileName, randomProgressValue())
    }
    
    fun makeListOfFileData( size : Int): List<FileData> {
        val fileDataList = mutableListOf<FileData>()

        repeat(size){
            fileDataList.add( makeFileDataEntry())
        }

        return fileDataList
    } /*END makeListOfFileData*/
}