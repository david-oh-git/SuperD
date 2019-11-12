package io.audioshinigami.superd.addownload

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.audioshinigami.superd.R
import io.audioshinigami.superd.SharedViewModel
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.databinding.DownloadsFragmentBinding
import io.audioshinigami.superd.utility.*
import kotlinx.android.synthetic.main.downloads_fragment.*

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


        /* adaptor for download recyclerView*/
        val adaptor = DownloadAdaptor(R.layout.download_item_2)
//        val adaptor = FIleDataAdaptor()
        subscribeUi(binding, adaptor)

        return binding.root
    }

    private fun subscribeUi(binding: DownloadsFragmentBinding?, adaptor: DownloadAdaptor) {
        /* assign click listener*/
//        adaptor.itemClickListener = downLoadItemClickListener()

        /* assign download_item binding to viewModel*/
        adaptor.binding?.viewModel = viewModel

        viewModel.downloads.observe(binding?.lifecycleOwner!!, Observer {
            results ->

            when(results){
                is Result.Success -> {
                    binding.progressBar.hideView()
                    binding.downloadsRview.showView()
                    results.data.let { adaptor.setData(it) }
                    binding.downloadsRview.adapter = adaptor
                    Log.d(TAG, " Success ....")
                }
                is Result.Error -> {

                    binding.progressBar.hideView()
                    binding.downloadsRview.hideView()
                    sendSnack( getString(R.string.db_error_msg) )
                    Log.d(TAG, " Error ....")

                }
                is Result.Loading -> {
                    binding.downloadsRview.hideView()
                    binding.progressBar.showView()
                    Log.d(TAG , " loading now ....")
                }
            }
        })

//        viewModel.pagedDownloads.observe(binding?.lifecycleOwner!!, Observer {
//            data ->
//            data?.apply {
//                adaptor.submitList(this)
//
//                binding.apply {
//                    progressBar.hideView()
//                    downloadsRview.adapter = adaptor
//                    downloadsRview.showView()
//                }
//            }
//        })
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

    companion object {
        private const val TAG = "DownloadFrag"
    }

}
