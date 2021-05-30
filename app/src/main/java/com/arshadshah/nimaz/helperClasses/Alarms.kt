package com.arshadshah.nimaz.helperClasses

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.arshadshah.nimaz.recievers.ReminderReciever
import java.util.*

class Alarms
{

    /**
     * Sets a Repeating alarm
     * @author Arshad Shah
     * @param context The Context of the Application
     * @param pendingIntent The pending Intent for the alarm
     * @return Alarm
     * */
    @RequiresApi(Build.VERSION_CODES.M)
    fun setAlarm(context : Context , pendingIntent : PendingIntent)
    {
        // get alarm manager
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

        val calendar = GregorianCalendar.getInstance().apply {
            if (get(Calendar.HOUR_OF_DAY) >= 1)
            {
                add(Calendar.DAY_OF_MONTH , 1)
            }
            set(Calendar.HOUR_OF_DAY , 1)
            set(Calendar.MINUTE , 0)
            set(Calendar.SECOND , 0)
            set(Calendar.MILLISECOND , 0)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP ,
            calendar.timeInMillis ,
            AlarmManager.INTERVAL_DAY ,
            pendingIntent
                                 )

        //recieverEnabled(context)

        //logs
        Log.i("Alarms for Adhan" , "Reset alarm for 1 Oclock each night set.")
    } // end of alarm set


    /**
     * Sets a Exact alarm that is allowed in doze mode
     * @author Arshad Shah
     * @param context The Context of the Application
     * @param timeToNotify The Alarm time in milliseconds
     * @param pendingIntent The pending Intent for the alarm
     * @return Alarm
     * */
    @RequiresApi(Build.VERSION_CODES.M)
    fun setExactAlarm(context : Context , timeToNotify : Long , pendingIntent : PendingIntent)
    {
        // get alarm manager
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP , timeToNotify , pendingIntent
                                              )


        //recieverEnabled(context)

        //logs
        Log.i("Alarms for Adhan" , "Alarm for $timeToNotify is successfully created")
    } // end of alarm set


    /**
     * Cancels an alarm
     * @author Arshad Shah
     * @param pendingIntent the intent of the alarm to be canceled
     * @param context the context of the Application
     * */
    fun cancelAlarm(context : Context , pendingIntent : PendingIntent)
    {
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        Log.i("Alarms for Adhan" , "ALL alarms are cancelled")
    }


    /**
     * Enables the Broadcast receiver for the alarms
     * @author Arshad Shah
     * @param context the context of the Application
     * */
    fun recieverEnabled(context : Context)
    {
        //ReminderReciever Enabled
        val reciever = ComponentName(context , ReminderReciever::class.java)
        context.packageManager.setComponentEnabledSetting(
            reciever ,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED ,
            PackageManager.DONT_KILL_APP
                                                         )
    }


    /**
     * disables the Broadcast receiver for the alarms
     * @author Arshad Shah
     * @param context the context of the Application
     * */
    fun recieverDisabled(context : Context)
    {
        //ReminderReciever Enabled
        val reciever = ComponentName(context , ReminderReciever::class.java)
        context.packageManager.setComponentEnabledSetting(
            reciever ,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED ,
            PackageManager.DONT_KILL_APP
                                                         )
    }
}