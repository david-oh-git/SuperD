package io.audioshinigami.superd.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import io.audioshinigami.superd.R
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
    private var listener: OnFragmentInteractionListener? = null

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
                listener?.sendUrlString(urlStr)
                dismiss()
            }
            else
                activity?.toast("Enter Url ")
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun sendUrlString(urlString: String)
    }

    companion object{

//        returns new instance of fragment
        fun newInstance(): GetUrlFragment =
    GetUrlFragment()
    } //end companion

}
