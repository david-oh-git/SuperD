package io.audioshinigami.superd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import io.audioshinigami.superd.R
import io.audioshinigami.superd.utility.KEY_URL
import io.audioshinigami.superd.utility.toast
import kotlinx.android.synthetic.main.fragment_get_url.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [GetUrlFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */

class GetUrlFragment : DialogFragment() {

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
                activity?.toast("Enter Url ")
        }
    }

    private fun sendUrl(url: String){

        // add url to bundle
        val args = Bundle()
        args.putString(KEY_URL, url)

        findNavController().setGraph(R.navigation.navigation_graph, args)
    }
}
