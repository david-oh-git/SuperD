package io.audioshinigami.superd.downloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.audioshinigami.superd.App
import io.audioshinigami.superd.R
import io.audioshinigami.superd.databinding.DownloadsFragmentBinding
import io.audioshinigami.superd.utility.extentions.copyToClipBoard
import io.audioshinigami.superd.utility.extentions.hideView
import io.audioshinigami.superd.utility.extentions.sendSnack
import io.audioshinigami.superd.utility.extentions.showView
import io.audioshinigami.superd.data.source.State.*
import timber.log.Timber
import kotlin.math.abs

class DownloadsFragment :
    Fragment(), PopupMenu.OnMenuItemClickListener, DownloadItemActions {


//    private val viewModel by lazy { obtainViewModel(SharedViewModel::class.java) }
    private val viewModel by viewModels<DownloadsViewModel> {
        DownloadsViewModelFactory( (requireContext().applicationContext as App ).fileInfoRepository )
    }
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
        val adaptor = FileInfoAdaptor(  this )
        subscribeUi(binding, adaptor)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.enableFetchListener()
    }

    override fun onPause() {
        super.onPause()

        Timber.d("onPause")
        viewModel.disableFetchListener()
    }

    override fun onStop() {
        super.onStop()

        Timber.d("onStop")
        viewModel.disableFetchListener()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.disableFetchListener()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when(item?.itemId){

            R.id.action_copy_url -> {
                _itemUrl?.apply {
                    // copies url to clipboard
                    context?.copyToClipBoard(this)
                    sendSnack( getString( R.string.url_copied ) )
                }

                _itemUrl = null
                true
            }

            R.id.action_delete -> {
                _itemUrl?.apply {
                    viewModel.delete( this )
                    sendSnack( getString( R.string.deleted ) )
                }
                true
            }

            else -> false
        }
    }

    override fun downloadButtonAction( view: View ,id: Int, url: String) {
        viewModel.downloadAction( id, url, (requireContext().applicationContext as App).activeDownloads[id] )
        setItemIcon( id , view as ImageButton )
    }

    override fun showPopup(view: View, itemUrl: String ) {
        showMenu(view)

        /* assigns popup url*/
        _itemUrl = itemUrl
    }

    override fun absValue(value: Int): Int = abs(value)

    private fun subscribeUi(binding: DownloadsFragmentBinding?, adaptor: FileInfoAdaptor) {

        /* shows loading progressBar , hides recyclerView*/
        binding?.apply {
            downloadsRview.hideView()
            progressBar.showView()
        }

        viewModel.pagedDownloads.observe(binding?.lifecycleOwner!!, Observer {
            data ->
            Timber.d("data size is ${data.size}")

            data?.apply {
                adaptor.submitList(this)

                binding.apply {
                    progressBar.hideView()
                    downloadsRview.adapter = adaptor
                    downloadsRview.showView()
                }

            }
        })

        viewModel.snackBarMessage.observe(binding.lifecycleOwner!!, Observer {
            sendSnack(it.message)
        })
    }

    private fun fabOnClickListener() : View.OnClickListener {
        /* launches GetUrlFragment */
        return View.OnClickListener {

            findNavController().navigate(R.id.action_downloadsFragment_to_getUrlFragment)
            viewModel.enableFetchListener()
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

    private fun setItemIcon( id: Int , button : ImageButton ){

        when( (requireContext().applicationContext as App).activeDownloads[id] ){
            DOWNLOADING -> button.setImageResource(R.drawable.ic_pause)
            PAUSED -> button.setImageResource(R.drawable.ic_play)
            ERROR -> button.setImageResource(R.drawable.ic_error)
            COMPLETE -> {
                button.setImageResource(R.drawable.ic_done)
                button.isEnabled = false
            }
            NONE -> {
                // TODO sumamabitch !!! RIP Bernie !!
            }
        }
    }
}
