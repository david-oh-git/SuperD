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

package io.audioshinigami.superd.utility

import io.audioshinigami.superd.factory.DataFactory.randomProgressValue
import io.audioshinigami.superd.factory.DataFactory.randomRequestId
import io.audioshinigami.superd.factory.DataFactory.randomUrl
import io.audioshinigami.superd.data.FileInfo

/*
* creates mock FileData instances for test purposes
* */

object FileInfoAndroidFactory {

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