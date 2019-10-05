package io.audioshinigami.superd.addeddownloads

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import io.audioshinigami.superd.R

class DownloadsFragment : Fragment() {

    companion object {
        fun newInstance() = DownloadsFragment()
    }

    private lateinit var viewModel: DownloadsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.downloads_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DownloadsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
