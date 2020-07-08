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

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.io.File

class ReUseMethods {

    companion object{

        fun attachFragment(frag: Fragment, fragManager: FragmentManager, layout: Int){
//            launches a fragment
            val transaction = fragManager.beginTransaction()
            transaction.replace(layout, frag)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        fun showDialogFrag(frag: DialogFragment, fragManager: FragmentManager){
            frag.show(fragManager, "get_url")
        } //end showDialog

        fun isNetworkAvailable(context: Context): Boolean {
//            checks if network is available
            val connManager =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connManager.activeNetworkInfo

            return activeNetwork?.isConnected == true
        } // end checkNetwork

        /* check if external storage is available 4 read & write */
        fun isExternalStorageWriteable(context: Context): Boolean {
//            return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
            return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }  //end isExternal

        fun getPublicFileStorageDir(albumName: String = "Superd"): File? {

            // TODO : start here , check if this works on android 10

            /* get download directory to store File*/
            val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//            val directory = App.instance.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File( directory ,
                albumName )
            if (!file.exists()){
                file.mkdirs()
            } //end if

            return file

        }  //end getExternalStorage



    }  //end companion

} //end Reuse