package com.arshadshah.nimaz.recievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.arshadshah.nimaz.helperClasses.utils.NotificationHelper

/**
 * A BroadcastReceiver That Shows notification
 * @author Arshad Shah
 */
class ReminderReciever : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val title = intent.extras!!.getString("title").toString()
        val CHANNEL_ID = intent.extras!!.getString("channelid").toString()
        val Notify_Id = intent.extras!!.getInt("notifyid")
        val Time_of_alarm = intent.extras!!.getLong("time")

        val current_time = System.currentTimeMillis()
        val diff = current_time - Time_of_alarm
        val graceP = 60000 * 2

        Log.i("Alarms for Adhan", "Alarm for $title is being executed!")
        // check if it is time to notify
        if (diff in 1 until graceP) {
            Log.i("Alarms for Adhan", "Notification for $title is being executed!")
            Toast.makeText(context, "Time for $title", Toast.LENGTH_LONG).show()
            NotificationHelper().createNotification(context, CHANNEL_ID, title, Notify_Id)

            Log.i("Alarms for Adhan", "Alarm for $title is Successfully executed!")
        } // end of if
        else {
            Log.i(
                "Alarms for Adhan",
                "Notification for $title is not executed! The time has passed"
            )
        }
    }
}
