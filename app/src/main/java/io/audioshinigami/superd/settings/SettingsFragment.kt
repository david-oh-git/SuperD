package io.audioshinigami.superd.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.audioshinigami.superd.R
import kotlinx.android.synthetic.main.content_settings.*
import kotlinx.android.synthetic.main.theme_bottom_sheet.*


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_bottom.setOnClickListener {
            // launch themes selection bottomSheet
            findNavController().navigate(R.id.action_settingsFragment_to_themeBottomSheetFragment)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
//        remove optionsMenu
        menu.clear()
    }

}
