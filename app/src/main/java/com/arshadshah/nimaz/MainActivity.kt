package com.arshadshah.nimaz

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager


/**
 * Launcher activity that works as a Splash screen
 * @author Arshad Shah
 */
class MainActivity : AppCompatActivity()
{

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        supportActionBar?.hide()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isFirstInstall = sharedPreferences.getBoolean("isFirstInstall" , true)

        val appWidgetManager = AppWidgetManager.getInstance(this)
        val appWidgetIds : IntArray = appWidgetManager.getAppWidgetIds(
            ComponentName(
                this ,
                Nimaz::class.java
                         )
                                                                      )
        for (appWidgetId in appWidgetIds)
        {
            updateAppWidget(this , appWidgetManager , appWidgetId)
        }

        if (! isFirstInstall)
        {
            Handler()
                .postDelayed(
                    {
                        val intent = Intent(this , homeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } ,
                    1000
                            ) // 1000 is the delayed time in milliseconds.
            supportActionBar?.hide()
        }
        else
        {
            Handler()
                .postDelayed(
                    {
                        val intent = Intent(this , IntroductionActivity::class.java)
                        startActivity(intent)
                        finish()
                    } ,
                    1000
                            ) // 1000 is the delayed time in milliseconds.
            supportActionBar?.hide()
        }
    }
    // end of oncreate
}
