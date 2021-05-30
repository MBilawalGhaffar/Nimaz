package com.arshadshah.nimaz

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
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


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val navView : BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.navigation_home ,
                    R.id.navigation_tasbeeh ,
                    R.id.navigation_compass ,
                    R.id.navigation_names,
                    R.id.navigation_setting
                     )
                               )
        navView.setupWithNavController(navController)

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
