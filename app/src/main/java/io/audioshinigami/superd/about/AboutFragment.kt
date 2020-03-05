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
import io.audioshinigami.superd.databinding.AboutFragmentBinding
import kotlinx.android.synthetic.main.about_fragment.*

class AboutFragment : Fragment() {

    private val developerGithubUrl = "https://www.github.com/david-oh-git"
    private val freepikUrl = "https://www.flaticon.com/authors/freepik"
    private val catalinUrl = "https://www.flaticon.com/authors/catalin-fertu"
    private val alfredoUrl = "https://www.flaticon.com/authors/alfredo-hernandez"
    private val kotlinUrl = "https://www.kotlinlang.org"
    private val jetpackUrl = "https://developer.android.com/jetpack"
    private val fetchUrl = "https://www.github.com/tonyofrancis/Fetch"
    private val mockitoUrl = "https://site.mockito.org"
    private val timberUrl = "https://www.github.com/JakeWharton/timber"
    private val hamcrestUrl = "https://www.hamcrest.org"

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
            openWebPage(developerGithubUrl)
        }

        freepik_icon_item.setOnClickListener {
            openWebPage(freepikUrl)
        }

        download_icon_item.setOnClickListener {
            openWebPage(catalinUrl)
        }

        alfredo_icon_item.setOnClickListener {
            openWebPage(alfredoUrl)
        }

        kotlin_item.setOnClickListener {
            openWebPage(kotlinUrl)
        }

        jetpack_item.setOnClickListener {
            openWebPage(jetpackUrl)
        }

        fetch2_item.setOnClickListener {
            openWebPage(fetchUrl)
        }

        mockito_item.setOnClickListener {
            openWebPage(mockitoUrl)
        }

        timber_item.setOnClickListener {
            openWebPage(timberUrl)
        }

        hamcrest_item.setOnClickListener {
            openWebPage(hamcrestUrl)
        }
    }

    private fun openWebPage( url: String){
        val webPage = Uri.parse(url)
        val intent =  Intent( Intent.ACTION_VIEW, webPage )
        startActivity(intent)
    }

}

