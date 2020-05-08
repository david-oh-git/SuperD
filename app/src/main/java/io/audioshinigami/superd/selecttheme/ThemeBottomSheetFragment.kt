package io.audioshinigami.superd.selecttheme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.audioshinigami.superd.App
import io.audioshinigami.superd.common.DARK_THEME
import io.audioshinigami.superd.common.FOLLOW_SYSTEM
import io.audioshinigami.superd.common.LIGHT_THEME
import io.audioshinigami.superd.databinding.ThemeBottomSheetFragmentBinding
import kotlinx.android.synthetic.main.theme_bottom_sheet_fragment.*

class ThemeBottomSheetFragment : BottomSheetDialogFragment() {

    private val _viewModel by viewModels<ThemeBottomSheetViewModel>{
        ThemeBottomSheetViewModelFactory( (requireContext().applicationContext as App).sharedPreferenceRepo )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = ThemeBottomSheetFragmentBinding.inflate( inflater, container, false)

        binding.apply {
            viewModel = _viewModel
            lifecycleOwner = this@ThemeBottomSheetFragment
        }

        subscribeLiveData(binding)

        return binding.root
    }

    private fun subscribeLiveData(binding: ThemeBottomSheetFragmentBinding) {

        binding.lifecycleOwner?.let {
            _viewModel.closeFragment.observe(it,
                Observer {
                    closeFragment ->
                        if (closeFragment) findNavController().popBackStack()
            })
        }
    }

    override fun onStart() {
        super.onStart()

        // auto select radio group to theme already set
        _viewModel.theme.observe(this) {

            when(it){
                DARK_THEME -> theme_dark.isChecked = true
                LIGHT_THEME -> theme_light.isChecked = true
                FOLLOW_SYSTEM -> theme_system_default.isChecked = true
            }
        }

    }
}
