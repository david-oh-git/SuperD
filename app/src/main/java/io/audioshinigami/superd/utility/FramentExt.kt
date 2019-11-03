package io.audioshinigami.superd.utility

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import io.audioshinigami.superd.App
import io.audioshinigami.superd.SharedViewModel
import io.audioshinigami.superd.data.repository.DefaultRepository

fun <T : ViewModel> Fragment.obtainViewModel(viewModel: Class<T>): T {

}
