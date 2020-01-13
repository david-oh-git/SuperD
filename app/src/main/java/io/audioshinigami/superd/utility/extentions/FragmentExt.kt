package io.audioshinigami.superd.utility.extentions

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import io.audioshinigami.superd.App
import io.audioshinigami.superd.ViewModelFactory
import io.audioshinigami.superd.data.repository.DefaultRepository

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>): T {
    val repository = DefaultRepository(App.instance.dataBaseInstance?.fileDataDao()!! ,
        App.instance.fetch )

    return ViewModelProviders.of(this.requireActivity(), ViewModelFactory(repository)).get(viewModelClass)
}

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

