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

package io.audioshinigami.superd

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.preference.PreferenceManager
import io.audioshinigami.superd.common.THEME_PREF_KEY
import io.audioshinigami.superd.utility.TwitterUrlUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if (intent.type == "text/html" || intent.type == "text/plain")
                    handleIntent(intent)
            }
        }

        /*sets the theme*/
        val theme = obtainTheme()
        AppCompatDelegate.setDefaultNightMode(
            theme
        )

        setSupportActionBar(toolbar)

        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    /*handles back or up actions
    * + fragments back stack*/
    override fun onSupportNavigateUp() : Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)

    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//
//        intent?.getStringExtra(Intent.EXTRA_TEXT)?.let {
//            val action = DownloadsFragmentDirections.actionDownloadsFragmentToAddDownloadFragment(
//                TwitterUrlUtil.extractTwitterUrlFromShareIntent(it)
//            )
//            navController.navigate(action)
//        }
//    }

    private fun obtainTheme(): Int =
        PreferenceManager.getDefaultSharedPreferences(this@MainActivity ).getInt(THEME_PREF_KEY, MODE_NIGHT_FOLLOW_SYSTEM)

    // launch AddDownload fragment and pass string value as an argument
    private fun handleIntent(intent: Intent){
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            val action = NavigationGraphDirections.actionGlobalAddDownloadFragment(
                TwitterUrlUtil.extractTwitterUrlFromShareIntent(it)
            )
            navController.navigate(action)
        }

    }

}
