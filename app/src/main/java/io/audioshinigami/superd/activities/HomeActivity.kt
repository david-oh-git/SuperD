package io.audioshinigami.superd.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.audioshinigami.superd.App
import io.audioshinigami.superd.R
import io.audioshinigami.superd.adaptors.ViewAdaptor
import io.audioshinigami.superd.common.SD_REQCODE
import io.audioshinigami.superd.common.subscribe
import io.audioshinigami.superd.data.source.db.entity.FileData
import io.audioshinigami.superd.listeners.SwipeToDeleteCallback
import io.audioshinigami.superd.utility.ReUseMethods
import io.audioshinigami.superd.viewholders.ItemViewHolder
import io.audioshinigami.superd.viewmodels.FileDataViewModel
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber


class HomeActivity : AppCompatActivity(), SwipeToDeleteCallback.SwipeToDeleteListener
{
    private val fileDataViewModel by lazy { ViewModelProviders.of(this).get(FileDataViewModel::class.java) }
    private val adaptor = ViewAdaptor()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /*check for Sd WRITE permit */
        requestSDWrite()

        fileDataViewModel.allFileData.subscribe(this, adaptor::addItems)

        setUpRecyclerView()

        val itemTouchHelper: ItemTouchHelper.SimpleCallback =
            SwipeToDeleteCallback(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(id_list_download)
    }

    private fun setUpRecyclerView(){
        id_list_download.adapter = adaptor
        id_list_download.layoutManager = LinearLayoutManager(this)
    }/*end addRecycler*/

    override fun onResume() {
        super.onResume()
        if( App.instance.fetch != null )
            fileDataViewModel.enableListener()
    }

    override fun onPause() {
        super.onPause()
        if( App.instance.fetch != null )
            fileDataViewModel.removeFetchListner()
    }

    private fun requestSDWrite(){
        /* request SD write permission */
        if(!ReUseMethods.isExternalStorageWriteable(this))
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), SD_REQCODE)
    }  //end requestSDWrite


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults.isNotEmpty()){

        } //en dif

        if(requestCode == SD_REQCODE && grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED ){
            Timber.d( "Write Permission granted")
        } //end if
        else{
//            toast("Write Permission not granted")
            finish()
        }

    }// end onRequestPermissions

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if ( viewHolder is ItemViewHolder ){
            val deletedItem: FileData = adaptor.getItem(viewHolder.adapterPosition )
             Timber.d( "deleted item object: $deletedItem")
            val deletedIndex: Int =  viewHolder.adapterPosition
             Timber.d( "deleted item index:  $deletedIndex")
            adaptor.removeItem(viewHolder.adapterPosition)
             Timber.d( "adaptor position is $deletedIndex")

            val snackbar = Snackbar.make( id_parent_layout, "Removed ", Snackbar.LENGTH_LONG)
            snackbar.setAction("UNDO", View.OnClickListener {
                adaptor.restore(deletedItem, deletedIndex)
            })

            snackbar.show()
        } /*end If*/
    }
}// end HomeActivity
