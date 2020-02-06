package io.audioshinigami.superd.selecttheme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.audioshinigami.superd.App
import io.audioshinigami.superd.R
import io.audioshinigami.superd.databinding.ThemeBottomSheetFragmentBinding

class ThemeBottomSheetFragment : BottomSheetDialogFragment() {

    private val _viewModel by viewModels<ThemeBottomSheetViewModel>{
        ThemeBottomSheetViewModel.ThemeBottomSheetViewModelFactory( (requireContext().applicationContext as App).sharedPreferenceRepo )
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

    fun setTheme() = View.OnClickListener {
        _viewModel.setTheme()
        findNavController().popBackStack()
    }

}
