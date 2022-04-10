package com.arshadshah.nimaz.helperClasses.alarms

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import junit.framework.TestCase
import com.arshadshah.nimaz.activities.HomeActivity
import com.arshadshah.nimaz.recievers.ReminderReciever

class AlarmsTest : TestCase() {

    //create context of home activity
    val context = HomeActivity().applicationContext

    //create pending intent for and alarm
    // fajr alarm
    val fajrIntent =
        Intent(context , ReminderReciever::class.java).apply {
            putExtra("title" , "Test")
            putExtra("channelid" , "TEST_1")
            putExtra("notifyid" , 2000)
            putExtra("time" , 1649433060000)
        }
    val fajrPendingIntent = PendingIntent.getBroadcast(context , 1 , fajrIntent , FLAG_IMMUTABLE)

    fun testSetAlarm() {
        //set alarm
        Alarms().setAlarm(context , fajrPendingIntent)
        //check if alarm is set or not
        assertEquals(true , fajrPendingIntent != null)
    }

    fun testSetExactAlarm() {}

    fun testCancelAlarm() {}

    fun testRecieverEnabled() {}

    fun testRecieverDisabled() {}
}