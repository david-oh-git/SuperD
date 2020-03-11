package io.audioshinigami.superd.utility.extentions

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.audioshinigami.superd.App

/* create a snackbar*/
fun Fragment.sendSnack( message: String ){
    Snackbar.make( this.requireView(), message, Snackbar.LENGTH_LONG).show()
}

/* sends a Toast */

fun Fragment.sendToastMsg( msg: String, length: Int = Toast.LENGTH_LONG ){
    Toast.makeText(context, msg, length ).show()
}

/* check for write external permission */
fun Fragment.isPermissionGranted(): Boolean {

    return ContextCompat.checkSelfPermission( App.instance , android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED
}