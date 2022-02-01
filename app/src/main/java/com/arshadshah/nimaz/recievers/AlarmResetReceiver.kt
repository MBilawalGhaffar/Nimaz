package com.arshadshah.nimaz.recievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.arshadshah.nimaz.helperClasses.alarms.CreateAlarms

class AlarmResetReceiver : BroadcastReceiver()
{

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context : Context , intent : Intent)
    {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val fajr = intent.extras !!.getLong("fajrTime")
        val sunrise = intent.extras !!.getLong("sunriseTime")
        val zuhar = intent.extras !!.getLong("zuharTime")
        val asar = intent.extras !!.getLong("asarTime")
        val maghrib = intent.extras !!.getLong("maghribTime")
        val ishaa = intent.extras !!.getLong("ishaaTime")

        //create alarms
        CreateAlarms().scheduleAlarms(context , fajr , sunrise , zuhar , asar , maghrib , ishaa)

        Log.i("Alarm Reset" , "All alarms reset")
    }
}