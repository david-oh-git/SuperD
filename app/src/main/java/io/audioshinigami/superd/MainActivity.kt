package io.audioshinigami.superd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment_container)

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

        return when( item.itemId){

            R.id.action_settings -> {

                findNavController(R.id.nav_host_fragment_container)
                    .navigate(R.id.action_downloadsFragment_to_settingsFragment)

                true
            }
            else ->  super.onOptionsItemSelected(item)
        }
    }

}
