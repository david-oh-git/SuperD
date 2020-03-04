package io.audioshinigami.superd.selecttheme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.audioshinigami.superd.App
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
            lightTheme = AppCompatDelegate.MODE_NIGHT_NO
            darkTheme = AppCompatDelegate.MODE_NIGHT_YES
            systemDefaultTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            assignTheme = setTheme()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // auto select radio group to theme already set
        _viewModel.theme.observe(this) {

            when(it){
                AppCompatDelegate.MODE_NIGHT_YES -> theme_dark.isChecked = true
                AppCompatDelegate.MODE_NIGHT_NO -> theme_light.isChecked = true
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> theme_system_default.isChecked = true
            }
        }

    }

    fun setTheme() = View.OnClickListener {
        _viewModel.setTheme()
        findNavController().popBackStack()
    }
}
