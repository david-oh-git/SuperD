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

package io.audioshinigami.superd.selecttheme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.audioshinigami.superd.App
import io.audioshinigami.superd.common.DARK_THEME
import io.audioshinigami.superd.common.FOLLOW_SYSTEM
import io.audioshinigami.superd.common.LIGHT_THEME
import io.audioshinigami.superd.databinding.ThemeBottomSheetFragmentBinding
import kotlinx.android.synthetic.main.theme_bottom_sheet_fragment.*
import javax.inject.Inject

class ThemeBottomSheetFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val _viewModel by viewModels<ThemeBottomSheetViewModel>{
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init dagger
        ( requireActivity().application as App).appComponent.settingsComponent().create()
            .themeSelectComponent().create().inject(this)
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
        when(_viewModel.getCurrentTheme()){
            DARK_THEME -> theme_dark.isChecked = true
            LIGHT_THEME -> theme_light.isChecked = true
            FOLLOW_SYSTEM -> theme_system_default.isChecked = true
        }

    }
}
