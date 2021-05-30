package com.arshadshah.nimaz.helperClasses

import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color.GREEN
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arshadshah.nimaz.MainActivity
import com.arshadshah.nimaz.R

/**
 * A Helper class that creates both the Notifications and the notification channels
 * @author Arshad Shah
 */
class NotificationHelper
{

    /**
     * Creates Notification Channel in Android O and Above
     * @author Arshad Shah
     * @param context The Context of the Application
     * @param importance The importance of the Channel
     * @param showBadge Show Badge for Channel
     * @param name The name of the channel
     * @param description The Description for the channel
     * @param channel_id The id of the Channel Creates a Channel
     * @param sound the sound to be used for the notification
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(
        context : Context ,
        importance : Int ,
        showBadge : Boolean ,
        name : String ,
        description : String ,
        channel_id : String ,
        sound : String
                                 )
    {
        val notificationManager : NotificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as
                    NotificationManager

        val Adhan : Uri = if (sound != null)
        {
            Uri.parse(sound)
        }
        else
        {
            Uri.parse("android.resource://" + context.packageName + "/" + R.raw.adhan)
        }
        val attributes =
            AudioAttributes.Builder()
                .setContentType(
                    AudioAttributes
                        .CONTENT_TYPE_SONIFICATION
                               )
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
        // 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            // 2
            val channel = NotificationChannel(channel_id , name , importance)
            channel.description = description
            channel.setShowBadge(showBadge)
            channel.enableLights(true)
            channel.lightColor = GREEN
            channel.setSound(Adhan , attributes)
            channel.lockscreenVisibility = VISIBILITY_PUBLIC
            channel.enableVibration(true)
            channel.vibrationPattern =
                longArrayOf(100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400)
            // 3
            notificationManager.createNotificationChannel(channel)
        }
        Log.i("Notifications" , "Notification Channel $name Successfully created")
    }


    /**
     * Creates Notification Channel in Android O and Above
     * @author Arshad Shah
     * @param context The Context of the Application
     * @param importance The importance of the Channel
     * @param showBadge Show Badge for Channel
     * @param name The name of the channel
     * @param description The Description for the channel
     * @param channel_id The id of the Channel Creates a Channel
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun NotificationChannelSilent(
        context : Context ,
        importance : Int ,
        showBadge : Boolean ,
        name : String ,
        description : String ,
        channel_id : String)
    {
        val notificationManager : NotificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as
                    NotificationManager
        // 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            // 2
            val channel = NotificationChannel(channel_id , name , importance)
            channel.description = description
            channel.setShowBadge(showBadge)
            channel.enableLights(true)
            channel.lightColor = GREEN
            channel.lockscreenVisibility = VISIBILITY_PUBLIC
            channel.enableVibration(true)
            channel.vibrationPattern =
                longArrayOf(100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400)
            // 3
            notificationManager.createNotificationChannel(channel)
        }
        Log.i("Notifications" , "Notification Channel $name Successfully created")
    }





    /**
     * Creates a Notification
     * @author Arshad Shah
     * @param context The Context of the Application
     * @param channel_id The id of the Channel
     * @param title the title of the notification
     * @param notification_id The id of the Notification ( Unique Integer)
     */
    fun createNotification(
        context : Context ,
        channel_id : String ,
        title : String ,
        notification_id : Int
                          )
    {
        // Create an explicit intent for an Activity in your app
        val notificationIntent =
            Intent(context , MainActivity::class.java).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent
                                .FLAG_ACTIVITY_CLEAR_TASK
            }
        val notificationPendingIntent : PendingIntent =
            PendingIntent.getActivity(context , 8 , notificationIntent , 0)

        if (title == "Test Adhan")
        {

            val builder =
                NotificationCompat.Builder(context , channel_id).apply {
                    setSmallIcon(R.drawable.ic_prayer)
                    setContentTitle(title)
                    setContentText("This is a test alarm notification")
                    priority = NotificationCompat.PRIORITY_HIGH
                    setContentIntent(notificationPendingIntent)
                    setAutoCancel(true)
                }
            with(NotificationManagerCompat.from(context)) {
                notify(notification_id , builder.build())
            }

        }
        else
        {

            val builder =
                NotificationCompat.Builder(context , channel_id).apply {
                    setSmallIcon(R.drawable.ic_prayer)
                    setContentTitle(title)
                    setContentText("Time to Offer $title salat")
                    priority = NotificationCompat.PRIORITY_HIGH
                    setContentIntent(notificationPendingIntent)
                    setAutoCancel(true)
                }
            with(NotificationManagerCompat.from(context)) {
                notify(notification_id , builder.build())
            }

        }
        Log.i("Notifications" , "Notification $title Successfully Displayed")
    }
}
