package io.audioshinigami.superd.util

import io.audioshinigami.superd.factory.DataFactory.randomProgressValue
import io.audioshinigami.superd.factory.DataFactory.randomRequestId
import io.audioshinigami.superd.factory.DataFactory.randomUrl
import io.audioshinigami.superd.data.FileInfo

/*
* creates mock FileData instances for test purposes
* */

object FileInfoFactory {

    fun singleEntry(): FileInfo {

        /* create a FIleDat instance with random values*/
        val url =  randomUrl()
        val fileName = url.substringAfter('/')
        return FileInfo(
            0,
            randomRequestId(),
            url,
            fileName,
            randomProgressValue()
        )
    }

    fun listOfEntries(size : Int): List<FileInfo> {
        val fileDataList = mutableListOf<FileInfo>()

        repeat(size){
            fileDataList.add( singleEntry())
        }

        return fileDataList
    } /*END makeListOfFileData*/
}