package io.audioshinigami.superd.addownload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.audioshinigami.superd.R
import io.audioshinigami.superd.SharedViewModel
import io.audioshinigami.superd.databinding.DownloadsFragmentBinding
import io.audioshinigami.superd.utility.hideView
import io.audioshinigami.superd.utility.recyclerview.extentions.copyToClipBoard
import io.audioshinigami.superd.utility.recyclerview.extentions.obtainViewModel
import io.audioshinigami.superd.utility.showView

class DownloadsFragment :
    Fragment(), PopupMenu.OnMenuItemClickListener, DownloadItemActions {


    private val viewModel by lazy { obtainViewModel(SharedViewModel::class.java) }
    private var _itemUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DownloadsFragmentBinding.inflate(inflater, container, false)

        binding.apply {
            fabListener = fabOnClickListener()
            lifecycleOwner = this@DownloadsFragment.viewLifecycleOwner
        }


        /* adaptor for download recyclerView*/
//        val adaptor = DownloadAdaptor(R.layout.download_item_2, viewModel )
        val adaptor = FileDataAdaptor(  this )
        subscribeUi(binding, adaptor)

        return binding.root
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when(item?.itemId){

            R.id.action_copy_url -> {
                _itemUrl?.apply {
                    // copies url to clipboard
                    context?.copyToClipBoard(this)
                    Toast.makeText(context, "URL copied !", Toast.LENGTH_LONG ).show()
                }
                true
            }

            R.id.action_delete -> {
                true
            }

            else -> false
        }
    }

    override fun downloadButtonAction(id: Int, url: String) {
        viewModel.downloadAction( id, url )
    }

    override fun showPopup(view: View, itemUrl: String ) {
        showMenu(view)

        /* assigns popup url*/
        _itemUrl = itemUrl
    }

    private fun subscribeUi(binding: DownloadsFragmentBinding?, adaptor: FileDataAdaptor) {
        /* assign click listener*/
//        adaptor.itemClickListener = downLoadItemClickListener()

        /* assign download_item binding to viewModel*/

//        viewModel.downloads.observe(binding?.lifecycleOwner!!, Observer {
//            results ->
//
//            when(results){
//                is Result.Success -> {
//                    binding.progressBar.hideView()
//                    binding.downloadsRview.showView()
//                    results.data.let { adaptor.setData(it) }
//                    binding.downloadsRview.adapter = adaptor
//                    Timber.d( " Success ....")
//                }
//                is Result.Error -> {
//
//                    binding.progressBar.hideView()
//                    binding.downloadsRview.hideView()
//                    sendSnack( getString(R.string.db_error_msg) )
//                    Timber.d( " Error ....")
//
//                }
//                is Result.Loading -> {
//                    binding.downloadsRview.hideView()
//                    binding.progressBar.showView()
//                    Timber.d( " loading now ....")
//                }
//            }
//        })

        binding?.apply {
            downloadsRview.hideView()
            progressBar.showView()
        }

        viewModel.pagedDownloads.observe(binding?.lifecycleOwner!!, Observer {
            data ->
            data?.apply {
                adaptor.submitList(this)

                binding.apply {
                    progressBar.hideView()
                    downloadsRview.adapter = adaptor
                    downloadsRview.showView()
                }
            }
        })
    }

    private fun fabOnClickListener() : View.OnClickListener {
        /* launches GetUrlFragment */
        return View.OnClickListener {
            findNavController().navigate(R.id.action_downloadsFragment_to_getUrlFragment)
        }
    }

    /* inflates popup menu */
    private fun showMenu( view: View ){
        PopupMenu(context, view ).apply {
            // this fragments implements onMenuItemClickListener
            setOnMenuItemClickListener(this@DownloadsFragment)
            inflate(R.menu.item_popup)
            show()
        }
    }
}
