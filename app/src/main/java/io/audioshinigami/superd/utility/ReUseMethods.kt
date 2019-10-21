package io.audioshinigami.superd.utility

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.audioshinigami.superd.common.TAG
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

        fun getPublicFileStorageDir(albumName: String = "SuperD"): File? {
            /* get download directory to store File*/
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                albumName )
            if (!file.exists()){
                file.mkdirs()
                Log.d(TAG, "Directory not created")
            } //end if

            return file

        }  //end getExternalStorage



    }  //end companion
} //end Reuse