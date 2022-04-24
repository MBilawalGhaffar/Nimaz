package com.arshadshah.nimaz.helperClasses.prayertimes

import android.content.Context
import android.os.Looper
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.helperClasses.alarms.CreateAlarms
import com.arshadshah.nimaz.helperClasses.utils.DateConvertor
import com.arshadshah.nimaz.helperClasses.utils.LocationFinder
import com.arshadshah.nimaz.helperClasses.utils.NetworkChecker
import com.arshadshah.nimaz.helperClasses.utils.prayerTimesUtils.*
import com.arshadshah.nimaz.helperClasses.utils.prayerTimesUtils.data.DateComponents
import java.util.*
import kotlin.properties.Delegates

class PrayerTimeThread(context: Context) : Thread() {

    var lati by Delegates.notNull<Double>()
    var longi by Delegates.notNull<Double>()

    val applicationContextVal = context

    override fun run() {
        Looper.prepare()
        //code to reset the alarm
        //shared preferences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContextVal)

        /*
        * Alarm setup for all the prayer time
        * Check network connectivity
        * */

        // get values from settings
        val name = sharedPreferences.getString("location_input", "Portlaoise")
        val calcMethod = sharedPreferences.getString("calcMethod", "IRELAND")

        // madhab adjustments
        val madhab = sharedPreferences.getBoolean("madhab", true)

        // angle input from settings
        val fajr_angle = sharedPreferences.getString("fajrAngle", "14.0")
        val ishaa_angle = sharedPreferences.getString("ishaaAngle", "14.0")

        // time adjustments
        val fajr_adjust = sharedPreferences.getString("fajr", "0")
        val sunrise_adjust = sharedPreferences.getString("sunrise", "0")
        val zuhar_adjust = sharedPreferences.getString("zuhar", "0")
        val asar_adjust = sharedPreferences.getString("asar", "0")
        val maghrib_adjust = sharedPreferences.getString("maghrib", "0")
        val ishaa_adjust = sharedPreferences.getString("ishaa", "0")


        val highlatRule = sharedPreferences.getString("highlatrule", "TWILIGHT_ANGLE")
        val locationTypeValue = sharedPreferences.getBoolean("locationType", true)

        val isNetworkAvailable = NetworkChecker().networkCheck(applicationContextVal)
        if (isNetworkAvailable) {
            if (!locationTypeValue) {
                //location finder class
                val lonAndLat = LocationFinder()
                lonAndLat.findLongAndLan(applicationContextVal, name!!)
            }
            lati = sharedPreferences.getString("latitude", "0.0")!!.toDouble()
            longi = sharedPreferences.getString("longitude", "0.0")!!.toDouble()
        } else {
            with(sharedPreferences.edit()) {
                putString("location_input", "No Network")
                apply()
            }
            lati = sharedPreferences.getString("latitude", "0.0")!!.toDouble()
            longi = sharedPreferences.getString("longitude", "0.0")!!.toDouble()
        }

        // calculation
        val coordinates = Coordinates(lati, longi)
        val calcdate = DateComponents.from(Date())

        /*
    * calculate the the time with the parameters
    * supplied to the API
    * */
        val parameters = CalculationMethod.valueOf(calcMethod!!).parameters
        if (madhab) {
            parameters.madhab = Madhab.SHAFI
        } else if (!madhab) {
            parameters.madhab = Madhab.HANAFI
        }
        parameters.fajrAngle = fajr_angle!!.toDouble()
        parameters.ishaAngle = ishaa_angle!!.toDouble()

        parameters.adjustments.fajr = fajr_adjust!!.toInt()
        parameters.adjustments.sunrise = sunrise_adjust!!.toInt()
        parameters.adjustments.dhuhr = zuhar_adjust!!.toInt()
        parameters.adjustments.asr = asar_adjust!!.toInt()
        parameters.adjustments.maghrib = maghrib_adjust!!.toInt()
        parameters.adjustments.isha = ishaa_adjust!!.toInt()
        parameters.highLatitudeRule = HighLatitudeRule.valueOf(highlatRule!!)

        val prayerTimes = PrayerTimes(coordinates, calcdate, parameters)


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
            putBoolean("alarmLock", false)
            apply()
        }

        //alarm lock
        val oneOClock = GregorianCalendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 1)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val current_time = System.currentTimeMillis()
        if (current_time > oneOClock.timeInMillis) {
            CreateAlarms().scheduleAlarms(
                applicationContextVal,
                fajrAlarm,
                sunriseAlarm,
                zuharAlarm,
                asarAlarm,
                maghribAlarm,
                ishaaAlarm
            )
            //reset alarms
            CreateAlarms().resetAlarms(
                applicationContextVal,
                fajrAlarm,
                sunriseAlarm,
                zuharAlarm,
                asarAlarm,
                maghribAlarm,
                ishaaAlarm
            )
        } else {
            //reset alarms
            CreateAlarms().resetAlarms(
                applicationContextVal,
                fajrAlarm,
                sunriseAlarm,
                zuharAlarm,
                asarAlarm,
                maghribAlarm,
                ishaaAlarm
            )
        }

    }
}