package io.audioshinigami.superd.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.audioshinigami.superd.BuildConfig.VERSION_NAME
import io.audioshinigami.superd.databinding.AboutFragmentBinding

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
            versionName = VERSION_NAME
            lifecycleOwner = viewLifecycleOwner
            openWebPage = ::loadPage
        }
        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }

    private fun loadPage( url: String){
        val intent =  Intent( Intent.ACTION_VIEW, Uri.parse(url) )
        startActivity(intent)
    }

}

