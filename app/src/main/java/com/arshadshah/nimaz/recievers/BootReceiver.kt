package com.arshadshah.nimaz.recievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.helperClasses.alarms.CreateAlarms
import com.arshadshah.nimaz.helperClasses.utils.DateConvertor
import com.arshadshah.nimaz.helperClasses.utils.NetworkChecker
import com.arshadshah.nimaz.helperClasses.utils.locationFinder
import com.arshadshah.nimaz.prayerTimeApi.*
import com.arshadshah.nimaz.prayerTimeApi.data.DateComponents
import java.util.*
import kotlin.properties.Delegates

/**
 * A BroadcastReceiver that Resets the Alarms on Device bootup
 * @author Arshad Shah
 */
class bootReceiver : BroadcastReceiver()
{

    var latitude by Delegates.notNull<Double>()
    var longitude by Delegates.notNull<Double>()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onReceive(context : Context , intent : Intent)
    {
        if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED) ||
            intent.action.equals(Intent.ACTION_LOCKED_BOOT_COMPLETED)
        )
        {
            Log.i("Boot Completed" , "Intent action from Boot Complete Received")
            Log.i("Alarms for Adhan" , "Resetting Alarms after BootUp!")


            //shared preferences
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

            /**
             * Alarm setup for all the prayer time
             * Check network connectivity
             * */

            // get values from settings
            val name = sharedPreferences.getString("location_input" , "Portlaoise")
            val calcMethod = sharedPreferences.getString("calcMethod" , "IRELAND")

            // madhab adjustments
            val madhab = sharedPreferences.getBoolean("madhab" , true)

            // angle input from settings
            val fajr_angle = sharedPreferences.getString("fajrAngle" , "14.0")
            val ishaa_angle = sharedPreferences.getString("ishaaAngle" , "13.0")

            // time adjustments
            val fajr_adjust = sharedPreferences.getString("fajr" , "0")
            val sunrise_adjust = sharedPreferences.getString("sunrise" , "0")
            val zuhar_adjust = sharedPreferences.getString("zuhar" , "0")
            val asar_adjust = sharedPreferences.getString("asar" , "0")
            val maghrib_adjust = sharedPreferences.getString("maghrib" , "0")
            val ishaa_adjust = sharedPreferences.getString("ishaa" , "0")

            val highlatRule = sharedPreferences.getString("highlatrule" , "TWILIGHT_ANGLE")


            val isNetworkAvailable = NetworkChecker().networkCheck(context)
            if (isNetworkAvailable)
            {
                //location finder class
                //location finder class
                val lonAndLat = locationFinder()
                lonAndLat.findLongAndLan(context , name !!)
                latitude = sharedPreferences.getString("latitude" , "0.0") !!.toDouble()
                longitude = sharedPreferences.getString("longitude" , "0.0") !!.toDouble()
            }
            else
            {
                with(sharedPreferences.edit()) {
                    putString("location_input" , "No Network")
                    apply()
                }
                latitude = sharedPreferences.getString("latitude" , "0.0") !!.toDouble()
                longitude = sharedPreferences.getString("longitude" , "0.0") !!.toDouble()
            }

            // calculation
            val coordinates = Coordinates(latitude , longitude)
            val calcdate = DateComponents.from(Date())

            /**
             * calculate the the time with the parameters
             * supplied to the API
             * */
            val parameters = CalculationMethod.valueOf(calcMethod !!).parameters
            if (madhab)
            {
                parameters.madhab = Madhab.SHAFI
            }
            else if (! madhab)
            {
                parameters.madhab = Madhab.HANAFI
            }
            parameters.fajrAngle = fajr_angle !!.toDouble()
            parameters.ishaAngle = ishaa_angle !!.toDouble()

            parameters.adjustments.fajr = fajr_adjust !!.toInt()
            parameters.adjustments.sunrise = sunrise_adjust !!.toInt()
            parameters.adjustments.dhuhr = zuhar_adjust !!.toInt()
            parameters.adjustments.asr = asar_adjust !!.toInt()
            parameters.adjustments.maghrib = maghrib_adjust !!.toInt()
            parameters.adjustments.isha = ishaa_adjust !!.toInt()
            parameters.highLatitudeRule = HighLatitudeRule.valueOf(highlatRule !!)

            val prayerTimes = PrayerTimes(coordinates , calcdate , parameters)


            // time for alarm
            // prayer times in string format used to create the alarms
            val fajr_value = prayerTimes.fajr.toString()
            val sunrise_value = prayerTimes.sunrise.toString()
            val zuhar_value = prayerTimes.dhuhr.toString()
            val asar_value = prayerTimes.asr.toString()
            val maghrib_value = prayerTimes.maghrib.toString()
            val ishaa_value = prayerTimes.isha.toString()

            val fajrAlarm = DateConvertor().convertTimeToLong(fajr_value)
            val sunriseAlarm = DateConvertor().convertTimeToLong(sunrise_value)
            val zuharAlarm = DateConvertor().convertTimeToLong(zuhar_value)
            val asarAlarm = DateConvertor().convertTimeToLong(asar_value)
            val maghribAlarm = DateConvertor().convertTimeToLong(maghrib_value)
            val ishaaAlarm = DateConvertor().convertTimeToLong(ishaa_value)

            //write lock to storage
            with(sharedPreferences.edit()) {
                putBoolean("alarmLock" , false)
                apply()
            }

            CreateAlarms().scheduleAlarms(
                context ,
                fajrAlarm ,
                sunriseAlarm ,
                zuharAlarm ,
                asarAlarm ,
                maghribAlarm ,
                ishaaAlarm
                                         )
            Log.i("Alarms for Adhan" , "Alarms Reset and executed")
            //reset alarms
            CreateAlarms().resetAlarms(
                context ,
                fajrAlarm ,
                sunriseAlarm ,
                zuharAlarm ,
                asarAlarm ,
                maghribAlarm ,
                ishaaAlarm
                                      )
            Log.i("Alarms for Adhan" , "Alarms will be reset on 1 oclock every night")
        }
    } // end of on receive function
} // end of boot receiver
