package io.audioshinigami.superd.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.audioshinigami.superd.BuildConfig
import io.audioshinigami.superd.R
import io.audioshinigami.superd.databinding.AboutFragmentBinding
import kotlinx.android.synthetic.main.about_fragment.*

class AboutFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AboutFragmentBinding.inflate( inflater, container, false)

        binding.apply {
            versionName = BuildConfig.VERSION_NAME
            lifecycleOwner = this@AboutFragment.viewLifecycleOwner
        }
        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
    }

    private fun initClickListeners(){
        developer_item.setOnClickListener {
            openWebPage( getString(R.string.developer_url) )
        }

        freepik_icon_item.setOnClickListener {
            openWebPage( getString(R.string.freepik_url) )
        }

        download_icon_item.setOnClickListener {
            openWebPage( getString(R.string.catalin_url) )
        }

        alfredo_icon_item.setOnClickListener {
            openWebPage( getString(R.string.alfredo_url) )
        }

        kotlin_item.setOnClickListener {
            openWebPage( getString(R.string.kotlin_url) )
        }

        jetpack_item.setOnClickListener {
            openWebPage( getString(R.string.jetpack_url) )
        }

        fetch2_item.setOnClickListener {
            openWebPage( getString(R.string.fetch_url) )
        }

        mockito_item.setOnClickListener {
            openWebPage( getString(R.string.mockito_url) )
        }

        timber_item.setOnClickListener {
            openWebPage( getString(R.string.timber_url) )
        }

        hamcrest_item.setOnClickListener {
            openWebPage( getString(R.string.hamcrest_url) )
        }
    }

    private fun openWebPage( url: String){
        val webPage = Uri.parse(url)
        val intent =  Intent( Intent.ACTION_VIEW, webPage )
        startActivity(intent)
    }

}

