package io.audioshinigami.superd.addownload

import android.view.View

/*
*  callback from recyclerView item
*/

interface DownloadItemActions {

    fun downloadButtonAction( view: View ,id: Int, url: String )
    fun showPopup( view: View , itemUrl: String )
}