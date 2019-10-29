package io.audioshinigami.superd.addownload

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import io.audioshinigami.superd.R
import io.audioshinigami.superd.databinding.DownloadsFragmentBinding
import io.audioshinigami.superd.utility.KEY_URL
import io.audioshinigami.superd.utility.toast

class DownloadsFragment : Fragment() {


    private val viewModel: DownloadsViewModel by lazy { ViewModelProviders.of(this).get(DownloadsViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DownloadsFragmentBinding.inflate(inflater, container, false)

        /* click action for FAB button */
        binding.fabListener = createFabListener()

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








}
