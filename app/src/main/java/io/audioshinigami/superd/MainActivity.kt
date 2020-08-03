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
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when (intent?.action) {
            Intent.ACTION_SEND -> {
                Timber.d("EXTRA SEND confirmed")
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

    private fun obtainTheme(): Int =
        PreferenceManager.getDefaultSharedPreferences(this@MainActivity ).getInt(THEME_PREF_KEY, MODE_NIGHT_FOLLOW_SYSTEM)

    private val fakeExtra = "When I finally buy this iPhone, it’s over for all of you \uD83E\uDD32 https://t.co/zoPlSf4XCc\n" +
            "    \n" +
            "    https://twitter.com/_VALKlNG/status/1289899309844979712"

    private val fakeTwitterShare = " https://twitter.com/almircolan/status/1289744460130050049?s=09"

    private val anodaFakeExtra = "When I finally buy this iPhone, it’s over for all of you \uD83E\uDD32 https://t.co/zoPlSf4XCc\n" +
            "    \n" +
            "    https://twitter.com/_VALKlNG/status/1289899309844979712"

    private fun handleIntent(intent: Intent){
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            Timber.d("Extra is :$it")
        }

    }

    private fun extractTwitterUrl(intentTwitterText: String): String {
        return  ""
    }

}
