package io.audioshinigami.superd.addownload

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import io.audioshinigami.superd.SharedViewModel

import io.audioshinigami.superd.R
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.databinding.DownloadsFragmentBinding
import io.audioshinigami.superd.utility.*

class DownloadsFragment : Fragment() {


    private val viewModel by lazy { obtainViewModel(SharedViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DownloadsFragmentBinding.inflate(inflater, container, false)

        binding.apply {
            fabListener = createFabListener()
            lifecycleOwner = this@DownloadsFragment.viewLifecycleOwner
        }


        /* adaptor for download recyclerview*/
        val adaptor = DownloadAdaptor(R.layout.download_item_2)

        subscribeUi(binding, adaptor)


        /*check for url string from getUrl fragment*/
        arguments?.apply {
            val url =  this.getString(KEY_URL) ?: " no url found"
            activity?.toast(url)
        }

        return binding.root
    }

    private fun subscribeUi(binding: DownloadsFragmentBinding?, adaptor: DownloadAdaptor) {
        /* assign click listener*/
        adaptor.itemClickListener = downLoadItemClickListener()

        /* retrieve data from DB */
        viewModel.loadData()

        viewModel.downloads.observe(binding?.lifecycleOwner!!, Observer {
            downloads ->

            when(downloads){
                is Result.Success -> {
                    binding.progressBar.hide()
                    adaptor.setData(downloads.data.value!!)
                }
                is Result.Error -> {
                    binding.progressBar.hide()
                    binding.downloadsRview.showView()
                    this.sendSnack(getString(R.string.db_error_msg))

                }
                is Result.Loading -> {
                    binding.downloadsRview.hideView()
                    binding.progressBar.show()
                }
            }
        })
    }

    private fun createFabListener() : View.OnClickListener {
        /* launches GetUrlFragment */
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
