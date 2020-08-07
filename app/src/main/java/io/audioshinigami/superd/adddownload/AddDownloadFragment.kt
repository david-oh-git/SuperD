/*
 * MIT License
 *
 * Copyright (c) 2020  David Osemwota
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.audioshinigami.superd.adddownload

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import io.audioshinigami.superd.App
import io.audioshinigami.superd.R
import io.audioshinigami.superd.common.WRITE_EXTERNAL_REQUEST_CODE
import io.audioshinigami.superd.data.Result
import io.audioshinigami.superd.databinding.FragmentAddDownloadBinding
import io.audioshinigami.superd.utility.PermissionManager
import io.audioshinigami.superd.utility.ReUseMethods
import io.audioshinigami.superd.utility.extentions.sendToastMsg
import kotlinx.android.synthetic.main.fragment_add_download.*
import timber.log.Timber
import java.io.File
import javax.inject.Inject


/**
 * starts a download
 *
 */

class AddDownloadFragment : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AddDownloadViewModel> {
        viewModelFactory
    }

    val args: AddDownloadFragmentArgs by navArgs()

    private lateinit var binding: FragmentAddDownloadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ( requireActivity().application as App).appComponent.addDownload().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddDownloadBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.idEditGeturl.requestFocus()

        if ( args.url == null ){
            autoPaste()
        }else{
            binding.idEditGeturl.setText(args.url)
        }

        id_btn_send_url.setOnClickListener{

            val urlStr = binding.idEditGeturl.text.toString()
            if(urlStr.isNotEmpty()){
                sendUrl(urlStr)
            }
            else
                sendToastMsg( getString(R.string.download_url) )

        }

        setupRecyclerView(binding)
    }

    private fun setupRecyclerView(binding: FragmentAddDownloadBinding) {
        binding.vm?.listOfMedia?.observe(this, Observer { result ->

            when(result){
                is Result.Success -> {
                    if(result.data.isNotEmpty()){
                        log("size is ${result.data.size}")
                        binding.listView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,
                            false )
                        binding.listView.adapter = TweetMediaAdaptor(TweetMediaItemClickActionImpl(binding) )
                            .apply { submitList(result.data) }

                        viewModel.showListView()

                        log("list visible: ${viewModel.isListVisible.value} ")
                        log("isLoading: ${viewModel.isLoading.value}")
                    }
                }
                is Result.Error -> {
                    // TODO : Snack for error msg
                }
            }
        })
    }

    private fun sendUrl(url: String){

        val permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

        if( url.isEmpty() ){
            sendToastMsg( getString(R.string.download_url) )
            return
        }


        PermissionManager.requestPermission( requireContext(),
            permission,
            ::makePermissionRequest )

        if ( !PermissionManager.isPermissionGranted(requireContext(),permission) )
            return

        // TODO get URI from user

        val uri = Uri.parse( ReUseMethods.getPublicFileStorageDir().toString() +
        File.separator + url.substringAfterLast("/") )

        viewModel.startDownload(url, uri)
            .run {
            if (this)
                findNavController().popBackStack()
        }

    }

    private fun makePermissionRequest(permission: String){
        requestPermissions( arrayOf( permission ),
            WRITE_EXTERNAL_REQUEST_CODE
        )
    }

    /* automatically pastes url in editText box*/
    private fun autoPaste(){

        val clipBoard = activity?.getSystemService( Context.CLIPBOARD_SERVICE ) as ClipboardManager

        if( clipBoard.hasPrimaryClip() && ( clipBoard.primaryClipDescription?.hasMimeType( MIMETYPE_TEXT_PLAIN ) == true ) ){

            val pasteText = clipBoard.primaryClip?.getItemAt(0)?.text
            binding.idEditGeturl.setText(pasteText)

        }

    }

    private fun log(message: String){
        Timber.d(message)
    }

}
