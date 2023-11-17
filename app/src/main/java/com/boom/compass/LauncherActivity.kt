package com.boom.compass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Switch
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.boom.compass.databinding.ActivityMain2Binding

class LauncherActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        (navView.menu.findItem(R.id.app_bar_switch).actionView as RelativeLayout).findViewById<Switch>(R.id.switchAB).isChecked = AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES
        (navView.menu.findItem(R.id.app_bar_switch).actionView as RelativeLayout).findViewById<Switch>(R.id.switchAB).setOnCheckedChangeListener { buttonView, isChecked ->
            AppCompatDelegate.setDefaultNightMode(if(isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putInt("mode",AppCompatDelegate.getDefaultNightMode()).apply()
        }
        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> {
                   startActivity(Intent(applicationContext,MainActivity::class.java).apply {
                       putExtra("tmp","https://sites.google.com/view/compass---north-south/compass-north-south")
                   })
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        //navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}