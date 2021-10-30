package com.arshadshah.nimaz

import android.os.Build
import android.os.Bundle
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.helperClasses.CreateAlarms
import com.arshadshah.nimaz.helperClasses.prayerTimeThread
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.properties.Delegates


/**
 * The main activity that contains the code base for Alarms, and navigation
 * @author Arshad shah
 */
class homeActivity : AppCompatActivity()
{

    var latitude by Delegates.notNull<Double>()
    var longitude by Delegates.notNull<Double>()


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
//                    val i = Intent(this , SettingsActivity::class.java)
//                    startActivity(i)
                    true
                }

                else -> false
            }
        }

        supportActionBar?.hide()

        //shared preferences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        //create channels
        CreateAlarms().createChannel(this)

        //alarm lock
        val alarmLock = sharedPreferences.getBoolean("alarmLock" , false)
        if (! alarmLock)
        {
            val prayerThread = prayerTimeThread(this)
            prayerThread.start()
        }

    } // end of oncreate



} // end of class
