package com.arshadshah.nimaz

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.helperClasses.CreateAlarms
import com.arshadshah.nimaz.helperClasses.locationFinder
import com.arshadshah.nimaz.helperClasses.prayerTimeThread
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


/**
 * The main activity that contains the code base for Alarms, and navigation
 * @author Arshad shah
 */
class HomeActivity : AppCompatActivity()
{
    override fun onResume()
    {
        super.onResume()
        val navView : BottomNavigationView = findViewById(R.id.nav_view)
        navView.selectedItemId = R.id.navigation_home
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Retrieve values given in the settings activity
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val navView : BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        this.onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            if(navController.currentDestination?.id == R.id.navigation_home){
                finish()
            }
            else{
                navController.navigate(R.id.navigation_home)
            }
        }
        navView.menu.getItem(0).isCheckable = true

        navView.setOnNavigationItemSelectedListener {menu ->

            when(menu.itemId){

                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }

                R.id.navigation_tasbeeh -> {
                    navController.navigate(R.id.navigation_tasbeeh)
                    true
                }

                R.id.navigation_compass -> {
                    navController.navigate(R.id.navigation_compass)
                    true
                }
                R.id.navigation_names -> {
                    navController.navigate(R.id.navigation_names)
                    true
                }
                R.id.navigation_setting -> {
                    navController.navigate(R.id.navigation_setting)
                    true
                }

                else -> false
            }
        }

        supportActionBar?.hide()

        //create channels
        CreateAlarms().createChannel(this)

        //alarm lock
        val alarmLock = sharedPreferences.getBoolean("alarmLock" , false)
        if (! alarmLock)
        {
            val prayerThread = prayerTimeThread(this.applicationContext)
            prayerThread.start()
        }

    } // end of oncreate
} // end of class
