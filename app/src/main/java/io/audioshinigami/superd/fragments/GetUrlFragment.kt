package io.audioshinigami.superd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import io.audioshinigami.superd.R
import io.audioshinigami.superd.SharedViewModel
import io.audioshinigami.superd.utility.*
import kotlinx.android.synthetic.main.fragment_get_url.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [GetUrlFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
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

        id_btn_send_url.setOnClickListener{
            val urlStr = id_edit_geturl.text.toString()
            if(urlStr.isNotEmpty()){
                // TODO : code to send url back , this is makeshift
                sendUrl(urlStr)
            }
            else
                activity?.toast(getString(R.string.download_url))
        }
    }

    private fun sendUrl(url: String){

        val permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

        if( url.isEmpty() ){
            activity?.toast(getString(R.string.download_url))
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
        requestPermissions( arrayOf( permission ), WRITE_EXTERNAL_REQUEST_CODE )
    }


}
