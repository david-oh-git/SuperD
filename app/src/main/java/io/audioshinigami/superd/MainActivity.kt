package io.audioshinigami.superd

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import io.audioshinigami.superd.common.SETTINGS_PREF_NAME
import io.audioshinigami.superd.common.THEME_PREF_KEY
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*sets the theme*/
        val theme = obtainTheme()

        Timber.d("Theme is $theme")
        AppCompatDelegate.setDefaultNightMode(
            theme
        )

        setSupportActionBar(toolbar)

        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    /*handles back or up actions
    * + fragments back stack*/
    override fun onSupportNavigateUp() : Boolean {
        return findNavController(R.id.nav_host_fragment_container).navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)

    }

    private fun obtainTheme(): Int = runBlocking {
        val theme = ServiceLocator.provideSharedPreferenceRepository(SETTINGS_PREF_NAME,  applicationContext )
            .getInt(THEME_PREF_KEY)

        when(theme){
            -999 -> MODE_NIGHT_FOLLOW_SYSTEM
            null -> MODE_NIGHT_FOLLOW_SYSTEM
            else -> theme
        }
    }

}
