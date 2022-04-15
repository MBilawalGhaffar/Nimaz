package com.arshadshah.nimaz.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.prayertimes.PrayerTimeObject
import com.arshadshah.nimaz.helperClasses.prayertimes.PrayerTimesAdapter
import com.arshadshah.nimaz.helperClasses.utils.DateConvertor
import com.arshadshah.nimaz.helperClasses.utils.NetworkChecker
import com.arshadshah.nimaz.helperClasses.utils.locationFinder
import com.arshadshah.nimaz.prayerTimeApi.*
import com.arshadshah.nimaz.prayerTimeApi.data.DateComponents
import java.text.DateFormat
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.properties.Delegates


/**
 * home fragment that displays
 * Date in gregorian Calendar
 * Date in Hijri Calendar
 * Current Location
 * Current Time as a Digital clock
 * The prayer times from the API
 * The countdown timers for each prayer time
 * @author Arshad Shah
 * */
class HomeFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate: LocalDate = LocalDate.now()


    var latitude by Delegates.notNull<Double>()
    var longitude by Delegates.notNull<Double>()

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // Gregorian Date
        val date: TextView = root.findViewById(R.id.editTextDate)
        val Gregformat = DateTimeFormatter.ofPattern(" EEEE , dd - MMMM - yyyy ")
        val GregDate = Gregformat.format(currentDate)
        date.text = GregDate.toString()

        // hijri date
        val Hijridate: TextView = root.findViewById(R.id.hijri_date)
        val islamicDate = HijrahDate.now()
        val islamformat = DateTimeFormatter.ofPattern(" dd - MMMM - yyyy ")
        val islamDate = islamformat.format(islamicDate)
        Hijridate.text = islamDate.toString()


        //buttons for notifications
//        val fajrSound: ImageView = root.findViewById(R.id.fajrSound)

        //cityName
        val cityName: TextView = root.findViewById(R.id.cityName)

        // Retrieve values given in the settings activity
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val name = sharedPreferences.getString("location_input", "Portlaoise")
        val calcMethod = sharedPreferences.getString("calcMethod", "IRELAND")

        // madhab adjustments
        val madhab = sharedPreferences.getBoolean("madhab", true)

        // angle input from settings
        val fajr_angle = sharedPreferences.getString("fajrAngle", "14.0")
        val ishaa_angle = sharedPreferences.getString("ishaaAngle", "13.0")

        // time adjustments
        val fajr_adjust = sharedPreferences.getString("fajr", "0")
        val sunrise_adjust = sharedPreferences.getString("sunrise", "0")
        val zuhar_adjust = sharedPreferences.getString("zuhar", "0")
        val asar_adjust = sharedPreferences.getString("asar", "0")
        val maghrib_adjust = sharedPreferences.getString("maghrib", "0")
        val ishaa_adjust = sharedPreferences.getString("ishaa", "0")


        //high latitude rule list
        val highlatRule = sharedPreferences.getString("highlatrule", "TWILIGHT_ANGLE")
        val locationTypeValue = sharedPreferences.getBoolean("locationType", true)
        val isNetworkAvailable = NetworkChecker().networkCheck(requireContext())
        if (isNetworkAvailable) {
            if (!locationTypeValue) {
                //location finder class
                val lonAndLat = locationFinder()
                lonAndLat.findLongAndLan(requireContext(), name!!)
            }
            cityName.text = sharedPreferences.getString("location_input", "Portlaoise").toString()
            latitude = sharedPreferences.getString("latitude", "0.0")!!.toDouble()
            longitude = sharedPreferences.getString("longitude", "0.0")!!.toDouble()
        } else {
            with(sharedPreferences.edit()) {
                putString("location_input", "No Network")
                apply()
            }
            cityName.text = sharedPreferences.getString("location_input", "Portlaoise")
            latitude = sharedPreferences.getString("latitude", "0.0")!!.toDouble()
            longitude = sharedPreferences.getString("longitude", "0.0")!!.toDouble()
        }

        // calculation
        val coordinates = Coordinates(latitude, longitude)
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

        val formatter = DateFormat.getTimeInstance((DateFormat.SHORT))

        val prayerTimes = PrayerTimes(coordinates, calcdate, parameters)


        //check if ishaa time is too far away
        val ishaaTime = DateConvertor().convertTimeToLong(prayerTimes.isha.toString())
        val elevenOClock = GregorianCalendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 5)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (ishaaTime > elevenOClock.timeInMillis && latitude > 50.0) {
            with(sharedPreferences.edit()) {
                putBoolean("ishaaTimeLonger", true)
                apply()
            }
        } else {
            with(sharedPreferences.edit()) {
                putBoolean("ishaaTimeLonger", false)
                apply()
            }
        }


        //add the times to an array list of PrayerTimeObjects
        val prayerTimesArrayList = ArrayList<PrayerTimeObject?>()
        prayerTimesArrayList.add(PrayerTimeObject(getString(R.string.fajr),formatter.format(prayerTimes.fajr!!)))
        prayerTimesArrayList.add(PrayerTimeObject(getString(R.string.sunrise), formatter.format(prayerTimes.sunrise!!)))
        prayerTimesArrayList.add(PrayerTimeObject(getString(R.string.zuhar), formatter.format(prayerTimes.dhuhr!!)))
        prayerTimesArrayList.add(PrayerTimeObject(getString(R.string.asar), formatter.format(prayerTimes.asr!!)))
        prayerTimesArrayList.add(PrayerTimeObject(getString(R.string.maghrib), formatter.format(prayerTimes.maghrib!!)))
        prayerTimesArrayList.add(PrayerTimeObject(getString(R.string.ishaa), formatter.format(prayerTimes.isha!!)))

        //get the listView
        val prayerTimesList: ListView = root.findViewById(R.id.prayerTimes)

        //get custom adapter
        val adapter = PrayerTimesAdapter(requireContext(), prayerTimesArrayList,prayerTimes)

        prayerTimesList.adapter = adapter


        // notification channelid
        val CHANNEL_ID = "channel_id_01"
        val CHANNEL_ID1 = "channel_id_02"
        val CHANNEL_ID2 = "channel_id_03"
        val CHANNEL_ID3 = "channel_id_04"
        val CHANNEL_ID4 = "channel_id_05"
        val CHANNEL_ID5 = "Channel_id_06"

//        //mute functions
//        fajrSound.setOnClickListener {
//            val expandIn: Animation =
//                AnimationUtils.loadAnimation(requireContext(), R.anim.expand_in)
//            fajrSound.startAnimation(expandIn)
//            vibrate(30)
//            openNotificationChannel(CHANNEL_ID)
//        }

        // end of main code
        return root
    }
    // end of oncreate


    private fun openNotificationChannel(channelId: String) {
        //open notification settings based on channel id
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
        startActivity(intent)
    }

    private fun vibrate(amount: Long) {
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        vibrator.vibrate(
            VibrationEffect.createOneShot(amount, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }

} // end of home fragment
