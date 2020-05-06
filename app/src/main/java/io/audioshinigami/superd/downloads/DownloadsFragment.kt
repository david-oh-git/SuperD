package io.audioshinigami.superd.downloads

import android.media.MediaScannerConnection
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.audioshinigami.superd.App
import io.audioshinigami.superd.R
import io.audioshinigami.superd.data.source.State.*
import io.audioshinigami.superd.databinding.DownloadsFragmentBinding
import io.audioshinigami.superd.utility.extentions.copyToClipBoard
import io.audioshinigami.superd.utility.extentions.hideView
import io.audioshinigami.superd.utility.extentions.sendSnack
import io.audioshinigami.superd.utility.extentions.showView
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs

class DownloadsFragment :
    Fragment(), PopupMenu.OnMenuItemClickListener, DownloadItemActions {


//    private val viewModel by lazy { obtainViewModel(SharedViewModel::class.java) }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<DownloadsViewModel> {
        viewModelFactory
    }
    private var _itemUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ( requireActivity().application as App ).appComponent.downloadComponent().create().inject(this)
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDestination = findNavController().currentDestination?.label

        findNavController().addOnDestinationChangedListener { controller, destination, arguments ->

            if ( currentDestination == destination.label){
                // updates the isDownloading livedata to trigger the fetchlistener if download is active
                // or disable it if not active
                viewModel.refreshIsDownloading()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }

    override fun onPause() {
        super.onPause()

        Timber.d("onPause")
    }

    override fun onStop() {
        super.onStop()

        Timber.d("onStop")
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

    private fun subscribeUi(binding: DownloadsFragmentBinding, adaptor: FileInfoAdaptor) {

        /* shows loading progressBar , hides recyclerView*/
        binding?.apply {
            downloadsRview.hideView()
            progressBar.showView()
        }

        viewModel.pagedDownloads.observe(binding.lifecycleOwner!!, Observer {
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

        viewModel.snackBarMessage.observe(binding.lifecycleOwner!!, Observer {
            sendSnack(it.message)
        })

        viewModel.runMediaScanner.observe( viewLifecycleOwner, Observer {
            scanMedia(it)
        })

        viewModel.isDownLoading.observe(binding.lifecycleOwner!!, Observer {
            viewModel.enableFetchListener(it)
        } )
    }

    private fun scanMedia(filePath: String?) {

        if( filePath == null ) return

        Timber.d("File path is $filePath")
        val filesToScan = arrayOf( filePath)

        MediaScannerConnection.scanFile(context, filesToScan, null) { path, _ ->

                Timber.d("Scan complete : $path")
        }
    }

    private fun fabOnClickListener() : View.OnClickListener {
        /* launches GetUrlFragment */
        return View.OnClickListener {

            viewModel.enableFetchListener(true)
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
