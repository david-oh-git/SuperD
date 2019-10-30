package io.audioshinigami.superd.addownload

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import io.audioshinigami.superd.SharedViewModel

import io.audioshinigami.superd.R
import io.audioshinigami.superd.databinding.DownloadsFragmentBinding
import io.audioshinigami.superd.utility.KEY_URL
import io.audioshinigami.superd.utility.toast

class DownloadsFragment : Fragment() {


    private val viewModel: SharedViewModel by lazy { ViewModelProviders.of(this).get(
        SharedViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DownloadsFragmentBinding.inflate(inflater, container, false)

        /* click action for FAB button, starts GetUrlFragment */
        binding.fabListener = createFabListener()

        /* adaptor for download recyclerview*/
        val adaptor = DownloadAdaptor(R.layout.download_item_2)


        /*check for url string from getUrl fragment*/
        arguments?.apply {
            val url =  this.getString(KEY_URL) ?: " no url found"
            activity?.toast(url)
        }

        return binding.root
    }

    private fun createFabListener() : View.OnClickListener {

        return View.OnClickListener {
            findNavController().navigate(R.id.action_downloadsFragment_to_getUrlFragment)
        }
    }

    private fun downLoadItemClickListener() : View.OnClickListener {

        /* what happens when the download recyclerView item is click*/
        return View.OnClickListener {
            // TODO :
        }
    }








}
