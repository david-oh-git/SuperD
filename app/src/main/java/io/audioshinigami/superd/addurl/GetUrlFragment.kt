package io.audioshinigami.superd.addurl

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import io.audioshinigami.superd.R
import io.audioshinigami.superd.SharedViewModel
import io.audioshinigami.superd.utility.PermissionManager
import io.audioshinigami.superd.utility.extentions.WRITE_EXTERNAL_REQUEST_CODE
import io.audioshinigami.superd.utility.extentions.obtainViewModel
import io.audioshinigami.superd.utility.extentions.sendToastMsg
import kotlinx.android.synthetic.main.fragment_get_url.*


/**
 * starts a download
 *
 */

class GetUrlFragment : DialogFragment() {

    private val viewModel by lazy { obtainViewModel(SharedViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_url, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id_edit_geturl.requestFocus()
        autoPaste()

        id_btn_send_url.setOnClickListener{
            val urlStr = id_edit_geturl.text.toString()
            if(urlStr.isNotEmpty()){
                sendUrl(urlStr)
            }
            else
                sendToastMsg( getString(R.string.download_url) )

        }
    }

    private fun sendUrl(url: String){

        val permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

        if( url.isEmpty() ){
            sendToastMsg( getString(R.string.download_url) )
            return
        }


        PermissionManager.requestPermission( context!!,
            permission,
            ::makePermissionRequest )

        if ( !PermissionManager.isPermissionGranted(context!!,permission) )
            return


        viewModel.startDownload(url)

        findNavController().popBackStack()
    }

    private fun makePermissionRequest(permission: String){
        requestPermissions( arrayOf( permission ),
            WRITE_EXTERNAL_REQUEST_CODE
        )
    }

    /* automatically pastes url in editText box*/
    private fun autoPaste(){

        val clipBoard = activity?.getSystemService( Context.CLIPBOARD_SERVICE ) as ClipboardManager

        if( clipBoard.hasPrimaryClip() && ( clipBoard.primaryClipDescription?.hasMimeType( MIMETYPE_TEXT_PLAIN ) == true ) ){

            val pasteText = clipBoard.primaryClip?.getItemAt(0)?.text
            id_edit_geturl.setText(pasteText)

        }

    }

}
