package com.arshadshah.nimaz.fragments

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
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
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.HomeActivity
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.DateConvertor
import com.arshadshah.nimaz.helperClasses.NetworkChecker
import com.arshadshah.nimaz.helperClasses.TimerCreater
import com.arshadshah.nimaz.helperClasses.locationFinder
import com.arshadshah.nimaz.prayerTimeApi.*
import com.arshadshah.nimaz.prayerTimeApi.data.DateComponents
import java.text.DateFormat
import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.prefs.Preferences
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
class HomeFragment : Fragment()
{

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate : LocalDate = LocalDate.now()


    var latitude by Delegates.notNull<Double>()
    var longitude by Delegates.notNull<Double>()

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater : LayoutInflater ,
        container : ViewGroup? ,
        savedInstanceState : Bundle?
                             ) : View?
    {
        val root = inflater.inflate(R.layout.fragment_home , container , false)


        // Gregorian Date
        val date : TextView = root.findViewById(R.id.editTextDate)
        val Gregformat = DateTimeFormatter.ofPattern(" EEEE , dd - MMMM - yyyy ")
        val GregDate = Gregformat.format(currentDate)
        date.text = GregDate.toString()

        // hijri date
        val Hijridate : TextView = root.findViewById(R.id.hijri_date)
        val islamicDate = HijrahDate.now()
        val islamformat = DateTimeFormatter.ofPattern(" dd - MMMM - yyyy ")
        val islamDate = islamformat.format(islamicDate)
        Hijridate.text = islamDate.toString()

        // Text views where the prayer times are displayed
        val fajr_time : TextView = root.findViewById(R.id.Fajr_time)
        val sunrise_time : TextView = root.findViewById(R.id.sunrise_time)
        val zuhar_time : TextView = root.findViewById(R.id.Zuhar_time)
        val asar_time : TextView = root.findViewById(R.id.Asar_time)
        val maghrib_time : TextView = root.findViewById(R.id.Maghrib_time)
        val ishaa_time : TextView = root.findViewById(R.id.Ishaa_time)


        //buttons for notifications
        val fajrSound : ImageView = root.findViewById(R.id.fajrSound)
        val sunriseSound : ImageView = root.findViewById(R.id.sunriseSound)
        val zuharSound : ImageView = root.findViewById(R.id.zuharSound)
        val asarSound : ImageView = root.findViewById(R.id.asarSound)
        val maghribSound : ImageView = root.findViewById(R.id.maghribSound)
        val ishaaSound : ImageView = root.findViewById(R.id.ishaaSound)

        //cityName
        val cityName : TextView = root.findViewById(R.id.cityName)

        // Retrieve values given in the settings activity
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val name = sharedPreferences.getString("location_input" , " ")
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


        //high latitude rule list
        val highlatRule = sharedPreferences.getString("highlatrule" , "TWILIGHT_ANGLE")

        val isNetworkAvailable = NetworkChecker().networkCheck(requireContext())
        if (isNetworkAvailable)
        {
                //location finder class
                val lonAndLat = locationFinder()
                lonAndLat.findLongAndLan(requireContext() , name !!)
                cityName.text = sharedPreferences.getString("location_input" , "Portlaoise").toString()
                latitude = sharedPreferences.getString("latitude" , "0.0") !!.toDouble()
                longitude = sharedPreferences.getString("longitude" , "0.0") !!.toDouble()
        }
        else
        {
            with(sharedPreferences.edit()) {
                putString("location_input" , "No Network")
                apply()
            }
            cityName.text = sharedPreferences.getString("location_input" , "Portlaoise")
            latitude = sharedPreferences.getString("latitude" , "0.0") !!.toDouble()
            longitude = sharedPreferences.getString("longitude" , "0.0") !!.toDouble()
        }

        // calculation
        val coordinates = Coordinates(latitude , longitude)
        val calcdate = DateComponents.from(Date())

        /*
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

        val formatter = DateFormat.getTimeInstance((DateFormat.SHORT))

        val prayerTimes = PrayerTimes(coordinates , calcdate , parameters)

        // display prayerTimes to the screen
        fajr_time.text = formatter.format(prayerTimes.fajr !!)
        sunrise_time.text = formatter.format(prayerTimes.sunrise !!)
        zuhar_time.text = formatter.format(prayerTimes.dhuhr !!)
        asar_time.text = formatter.format(prayerTimes.asr !!)
        maghrib_time.text = formatter.format(prayerTimes.maghrib !!)

        //check if ishaa time is too far away
        val ishaaTime = DateConvertor().convertTimeToLong(prayerTimes.isha.toString())
        val elevenOClock = GregorianCalendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY , 23)
            set(Calendar.MINUTE , 5)
            set(Calendar.SECOND , 0)
            set(Calendar.MILLISECOND , 0)
        }

        if (ishaaTime > elevenOClock.timeInMillis && latitude > 50.0)
        {
            val Maghrib_Text = getString(R.string.maghrib)
            ishaa_time.text = Maghrib_Text
            with(sharedPreferences.edit()) {
                putBoolean("ishaaTimeLonger" , true)
                apply()
            }
        }
        else
        {
            ishaa_time.text = formatter.format(prayerTimes.isha !!)
            with(sharedPreferences.edit()) {
                putBoolean("ishaaTimeLonger" , false)
                apply()
            }
        }

        // Display the countdown timers
        val Tofajr : TextView = root.findViewById(R.id.Tofajr)
        val Tosunrise : TextView = root.findViewById(R.id.Tosunrise)
        val ToZuhar : TextView = root.findViewById(R.id.ToZuhar)
        val ToAsar : TextView = root.findViewById(R.id.ToAsar)
        val ToMaghrib : TextView = root.findViewById(R.id.ToMaghrib)
        val ToIshaa : TextView = root.findViewById(R.id.ToIshaa)

        // places to highlight per timer instance
        val fajrhighlight : ConstraintLayout = root.findViewById(R.id.fajrhighlight)
        val sunrisehighlight : ConstraintLayout = root.findViewById(R.id.sunrisehighlight)
        val zuharhighlight : ConstraintLayout = root.findViewById(R.id.zuharhighlight)
        val asarhighlight : ConstraintLayout = root.findViewById(R.id.asarhighlight)
        val maghribhighlight : ConstraintLayout = root.findViewById(R.id.maghribhighlight)
        val ishaahighlight : ConstraintLayout = root.findViewById(R.id.ishaahighlight)

        //create timers
        TimerCreater().createTimer(
            requireContext() ,
            prayerTimes,
            Tofajr , fajrhighlight ,
            Tosunrise , sunrisehighlight ,
            ToZuhar , zuharhighlight ,
            ToAsar , asarhighlight ,
            ToMaghrib , maghribhighlight ,
            ToIshaa , ishaahighlight
                                  )


        // notification channelid
        val CHANNEL_ID = "channel_id_01"
        val CHANNEL_ID1 = "channel_id_02"
        val CHANNEL_ID2 = "channel_id_03"
        val CHANNEL_ID3 = "channel_id_04"
        val CHANNEL_ID4 = "channel_id_05"
        val CHANNEL_ID5 = "Channel_id_06"

        //mute functions
        fajrSound.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(requireContext() , R.anim.expand_in)
            fajrSound.startAnimation(expandIn)
            vibrate(30)
            openNotificationChannel(CHANNEL_ID)
        }

        sunriseSound.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(requireContext() , R.anim.expand_in)
            sunriseSound.startAnimation(expandIn)
            vibrate(30)
            openNotificationChannel(CHANNEL_ID1)
        }

        zuharSound.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(requireContext() , R.anim.expand_in)
            zuharSound.startAnimation(expandIn)
            vibrate(30)
            openNotificationChannel(CHANNEL_ID2)
        }

        asarSound.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(requireContext() , R.anim.expand_in)
            asarSound.startAnimation(expandIn)
            vibrate(30)
            openNotificationChannel(CHANNEL_ID3)
        }

        maghribSound.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(requireContext() , R.anim.expand_in)
            maghribSound.startAnimation(expandIn)
            vibrate(30)
            openNotificationChannel(CHANNEL_ID4)
        }

        ishaaSound.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(requireContext() , R.anim.expand_in)
            ishaaSound.startAnimation(expandIn)
            vibrate(30)
            openNotificationChannel(CHANNEL_ID5)
        }

        // end of main code
        return root
    }
    // end of oncreate


    private fun openNotificationChannel(channelId : String)
    {
        //open notification settings based on channel id
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_CHANNEL_ID , channelId)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE , requireContext().packageName)
        startActivity(intent)
    }

    private fun vibrate(amount : Long)
    {
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        vibrator.vibrate(
            VibrationEffect.createOneShot(amount , VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }
    
} // end of home fragment
