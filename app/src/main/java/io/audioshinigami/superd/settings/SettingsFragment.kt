package io.audioshinigami.superd.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import io.audioshinigami.superd.App
import io.audioshinigami.superd.R
import io.audioshinigami.superd.databinding.SettingsFragmentBinding
import kotlinx.android.synthetic.main.content_settings.*


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment()  {

    private val _viewModel by viewModels<SettingsViewModel> {
        SettingsViewModelFactory( (requireContext().applicationContext as App).sharedPreferenceRepo )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = SettingsFragmentBinding.inflate( inflater, container, false)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = _viewModel
        }

        subscribe(binding)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
//        remove optionsMenu
        menu.clear()
    }

    override fun onStart() {
        super.onStart()

        // load the latest theme value
        _viewModel.loadThemeValue()


    }

    private fun initClickListeners(){
        select_theme.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_themeBottomSheetFragment)
        }

        about.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_aboutFragment)
        }
    }

    private fun subscribe(binding: SettingsFragmentBinding){

    }


}
