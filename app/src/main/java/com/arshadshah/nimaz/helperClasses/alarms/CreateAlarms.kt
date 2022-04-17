package com.arshadshah.nimaz.helperClasses.alarms

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.utils.NotificationHelper
import com.arshadshah.nimaz.recievers.AlarmResetReceiver
import com.arshadshah.nimaz.recievers.ReminderReciever
import java.text.DateFormat

/**
 * functions to create alarms
 * create channels
 * @author Arshad Shah
 * */
class CreateAlarms {


    /**
     * Schedule alarms for given times
     * @param context the context of the Application
     * @param fajrAlarm time in milliseconds for fajr
     * @param zuharAlarm time in milliseconds for zuhar
     * @param asarAlarm time in milliseconds for asar
     * @param maghribAlarm time in milliseconds for maghrib
     * @param ishaaAlarm time in milliseconds for ishaa
     * */
    @RequiresApi(Build.VERSION_CODES.M)
    fun scheduleAlarms(
        context: Context,
        fajrAlarm: Long,
        sunriseAlarm: Long,
        zuharAlarm: Long,
        asarAlarm: Long,
        maghribAlarm: Long,
        ishaaAlarm: Long
    ) {

        //change the long values to time
        val fajrTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(fajrAlarm)
        val zuharTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(zuharAlarm)
        val asarTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(asarAlarm)
        val maghribTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(maghribAlarm)
        val ishaaTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(ishaaAlarm)


        // notification channelid
        val CHANNEL_ID = "channel_id_01"
        val CHANNEL_ID1 = "channel_id_02"
        val CHANNEL_ID2 = "channel_id_03"
        val CHANNEL_ID3 = "channel_id_04"
        val CHANNEL_ID4 = "channel_id_05"
        val CHANNEL_ID5 = "Channel_id_06"

        // notification title
        val channelFajr = "Fajr at $fajrTime"
        val channelSunrise = context.getString(R.string.sunrise)
        val channelZuhar = "Dhuhr at $zuharTime"
        val channelAsar = "Asr at $asarTime"
        val channelMaghrib = "Maghrib at $maghribTime"
        val channelIshaa = "Isha at $ishaaTime"

        //shared preferences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        //alarm lock
        val alarmLock = sharedPreferences.getBoolean("alarmLock", false)

        if (!alarmLock) {

            // fajr alarm
            val fajrIntent =
                Intent(context, ReminderReciever::class.java).apply {
                    putExtra("title", channelFajr)
                    putExtra("channelid", CHANNEL_ID)
                    putExtra("notifyid", 2000)
                    putExtra("time", fajrAlarm)
                }
            val fajrPendingIntent =
                PendingIntent.getBroadcast(context, 1, fajrIntent, FLAG_IMMUTABLE)
            Alarms().setExactAlarm(context, fajrAlarm, fajrPendingIntent)

            // fajr alarm
            val sunriseIntent =
                Intent(context, ReminderReciever::class.java).apply {
                    putExtra("title", channelSunrise)
                    putExtra("channelid", CHANNEL_ID1)
                    putExtra("notifyid", 2001)
                    putExtra("time", sunriseAlarm)
                }
            val sunrisePendingIntent =
                PendingIntent.getBroadcast(context, 2, sunriseIntent, FLAG_IMMUTABLE)
            Alarms().setExactAlarm(context, sunriseAlarm, sunrisePendingIntent)

            // zuhar
            val zuharIntent =
                Intent(context, ReminderReciever::class.java).apply {
                    putExtra("title", channelZuhar)
                    putExtra("channelid", CHANNEL_ID2)
                    putExtra("notifyid", 2002)
                    putExtra("time", zuharAlarm)
                }
            val zuharPendingIntent =
                PendingIntent.getBroadcast(context, 3, zuharIntent, FLAG_IMMUTABLE)
            Alarms().setExactAlarm(context, zuharAlarm, zuharPendingIntent)

            // asar
            val asarintent =
                Intent(context, ReminderReciever::class.java).apply {
                    putExtra("title", channelAsar)
                    putExtra("channelid", CHANNEL_ID3)
                    putExtra("notifyid", 2003)
                    putExtra("time", asarAlarm)
                }
            val asarPendingIntent =
                PendingIntent.getBroadcast(context, 4, asarintent, FLAG_IMMUTABLE)
            Alarms().setExactAlarm(context, asarAlarm, asarPendingIntent)

            // maghrib
            val maghribIntent =
                Intent(context, ReminderReciever::class.java).apply {
                    putExtra("title", channelMaghrib)
                    putExtra("channelid", CHANNEL_ID4)
                    putExtra("notifyid", 2004)
                    putExtra("time", maghribAlarm)
                }
            val maghribPendingIntent =
                PendingIntent.getBroadcast(context, 5, maghribIntent, FLAG_IMMUTABLE)
            Alarms().setExactAlarm(context, maghribAlarm, maghribPendingIntent)

            //ishaa alarm
            val ishaaTimeLonger = sharedPreferences.getBoolean("ishaaTimeLonger", false)

            if (!ishaaTimeLonger) {
                // ishaa
                val ishaaIntent =
                    Intent(context, ReminderReciever::class.java).apply {
                        putExtra("title", channelIshaa)
                        putExtra("channelid", CHANNEL_ID5)
                        putExtra("notifyid", 2005)
                        putExtra("time", ishaaAlarm)
                    }
                val ishaaPendingIntent =
                    PendingIntent.getBroadcast(context, 6, ishaaIntent, FLAG_IMMUTABLE)
                Alarms().setExactAlarm(context, ishaaAlarm, ishaaPendingIntent)
            } else {
                val MaghribPlusBuffer = maghribAlarm + 1800000
                // ishaa
                val ishaaIntent =
                    Intent(context, ReminderReciever::class.java).apply {
                        putExtra("title", channelIshaa)
                        putExtra("channelid", CHANNEL_ID5)
                        putExtra("notifyid", 2005)
                        putExtra("time", MaghribPlusBuffer)
                    }
                val ishaaPendingIntent =
                    PendingIntent.getBroadcast(context, 6, ishaaIntent, FLAG_IMMUTABLE)
                Alarms().setExactAlarm(context, MaghribPlusBuffer, ishaaPendingIntent)
            }
            Log.i("Alarms for Adhan", "All alarms Alarms are scheduled successfully.")
            //write lock to storage
            with(sharedPreferences.edit()) {
                putBoolean("alarmLock", true)
                apply()
            }
        }

    }


    /**
     * reset the onetime exact alarms
     * @param context the context of the Application
     * @param fajrAlarm time in milliseconds for fajr alarm
     * @param zuharAlarm time in milliseconds for zuhar alarm
     * @param asarAlarm time in milliseconds for asar alarm
     * @param maghribAlarm time in milliseconds for maghrib alarm
     * @param ishaaAlarm time in milliseconds for ishaaAlarm
     * */
    @RequiresApi(Build.VERSION_CODES.M)
    fun resetAlarms(
        context: Context,
        fajrAlarm: Long,
        sunriseAlarm: Long,
        zuharAlarm: Long,
        asarAlarm: Long,
        maghribAlarm: Long,
        ishaaAlarm: Long
    ) {
        //recreate all alarms
        val resetIntent =
            Intent(context, AlarmResetReceiver::class.java).apply {
                putExtra("fajrTime", fajrAlarm)
                putExtra("sunriseTime", sunriseAlarm)
                putExtra("zuharTime", zuharAlarm)
                putExtra("asarTime", asarAlarm)
                putExtra("maghribTime", maghribAlarm)
                putExtra("ishaaTime", ishaaAlarm)
            }
        val resetPendingIntent = PendingIntent.getBroadcast(context, 7, resetIntent, FLAG_IMMUTABLE)
        Alarms().setAlarm(context, resetPendingIntent)
    }


    /**
     * Creates channel
     * */
    fun createChannel(context: Context) {

        //shared preferences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        // notification channelid
        val CHANNEL_ID = "channel_id_01"
        val CHANNEL_ID1 = "channel_id_02"
        val CHANNEL_ID2 = "channel_id_03"
        val CHANNEL_ID3 = "channel_id_04"
        val CHANNEL_ID4 = "channel_id_05"
        val CHANNEL_ID5 = "Channel_id_06"

        // notification title
        val channelFajr = context.getString(R.string.fajr)
        val channelSunrise = context.getString(R.string.sunrise)
        val channelZuhar = context.getString(R.string.zuhar)
        val channelAsar = context.getString(R.string.asar)
        val channelMaghrib = context.getString(R.string.maghrib)
        val channelIshaa = context.getString(R.string.ishaa)

        // notification description
        val descFajr = "Fajr Prayer Notification"
        val descSunrise = "Sunrise Notification"
        val descZuhar = "Zuhar Prayer Notification"
        val descAsar = "Asar Prayer Notification"
        val descMaghrib = "Maghrib Prayer Notification"
        val descIshaa = "Ishaa Prayer Notification"

        // make channels
        val channelLock = sharedPreferences.getBoolean("channelLock", false)
        if (!channelLock) {
            //adhan files
            val fajrAdhan = "android.resource://" + context.packageName + "/" + R.raw.fajr
            val zuharAdhan = "android.resource://" + context.packageName + "/" + R.raw.zuhar
            val asarAdhan = "android.resource://" + context.packageName + "/" + R.raw.asar
            val maghribAdhan = "android.resource://" + context.packageName + "/" + R.raw.maghrib
            val ishaaAdhan = "android.resource://" + context.packageName + "/" + R.raw.ishaa

            NotificationHelper()
                .createNotificationChannel(
                    context,
                    NotificationManagerCompat.IMPORTANCE_MAX,
                    true,
                    channelFajr,
                    descFajr,
                    CHANNEL_ID,
                    fajrAdhan
                )

            NotificationHelper()
                .NotificationChannelSilent(
                    context,
                    NotificationManagerCompat.IMPORTANCE_DEFAULT,
                    false,
                    channelSunrise,
                    descSunrise,
                    CHANNEL_ID1
                )


            NotificationHelper()
                .createNotificationChannel(
                    context,
                    NotificationManagerCompat.IMPORTANCE_MAX,
                    true,
                    channelZuhar,
                    descZuhar,
                    CHANNEL_ID2,
                    zuharAdhan
                )
            NotificationHelper()
                .createNotificationChannel(
                    context,
                    NotificationManagerCompat.IMPORTANCE_MAX,
                    true,
                    channelAsar,
                    descAsar,
                    CHANNEL_ID3,
                    asarAdhan
                )
            NotificationHelper()
                .createNotificationChannel(
                    context,
                    NotificationManagerCompat.IMPORTANCE_MAX,
                    true,
                    channelMaghrib,
                    descMaghrib,
                    CHANNEL_ID4,
                    maghribAdhan
                )
            NotificationHelper()
                .createNotificationChannel(
                    context,
                    NotificationManagerCompat.IMPORTANCE_MAX,
                    true,
                    channelIshaa,
                    descIshaa,
                    CHANNEL_ID5,
                    ishaaAdhan
                )

            //write lock to storage
            with(sharedPreferences.edit()) {
                putBoolean("channelLock", true)
                apply()
            }

        }
    }
}