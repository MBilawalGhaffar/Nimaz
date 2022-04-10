package com.arshadshah.nimaz

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.activities.HomeActivity
import com.arshadshah.nimaz.widgets.Nimaz
import com.arshadshah.nimaz.widgets.updateAppWidget


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
            val intent = Intent(this , HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        else
        {
            val intent = Intent(this , IntroductionActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    // end of oncreate
}
