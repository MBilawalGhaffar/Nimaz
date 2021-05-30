package com.arshadshah.nimaz

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.os.*
import android.provider.Settings
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.preference.*
import com.arshadshah.nimaz.helperClasses.*
import com.arshadshah.nimaz.prayerTimeApi.*
import com.arshadshah.nimaz.recievers.ReminderReciever
import java.util.*

/**
 * Settings for the application.
 * @author Arshad Shah
 */
private const val TITLE_TAG = "settingsActivityTitle"

class SettingsActivity :
    AppCompatActivity() , PreferenceFragmentCompat.OnPreferenceStartFragmentCallback
{


    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        // This callback will only be called when MyFragment is at least Started.
        val callback = this.onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            val intent = Intent(this@SettingsActivity , homeActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (savedInstanceState == null)
        {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings , HeaderFragment())
                .commit()
        }
        else
        {
            title = savedInstanceState.getCharSequence(TITLE_TAG)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0)
            {
                setTitle(R.string.title_activity_settings)
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSaveInstanceState(outState : Bundle)
    {
        super.onSaveInstanceState(outState)
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG , title)
    }

    override fun onSupportNavigateUp() : Boolean
    {
        if (supportFragmentManager.popBackStackImmediate())
        {
            return true
        }
        else
        {
            val intent = Intent(this@SettingsActivity , homeActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onSupportNavigateUp()
    }

    override fun onPreferenceStartFragment(
        caller : PreferenceFragmentCompat ,
        pref : Preference
                                          ) : Boolean
    {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment =
            supportFragmentManager.fragmentFactory.instantiate(classLoader , pref.fragment)
                .apply {
                    arguments = args
                    setTargetFragment(caller , 0)
                }
        // Replace the existing Fragment with the new Fragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings , fragment)
            .addToBackStack(null)
            .commit()
        title = pref.title
        return true
    }

    class HeaderFragment : PreferenceFragmentCompat()
    {

        @SuppressLint("NewApi")
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onCreatePreferences(savedInstanceState : Bundle? , rootKey : String?)
        {
            setPreferencesFromResource(R.xml.header_preferences , rootKey)

            val isNetworkAvailable = NetworkChecker().networkCheck(requireContext())

            val location : EditTextPreference? = findPreference("location_input")
            val latitude : EditTextPreference? = findPreference("latitude")
            val longitude : EditTextPreference? = findPreference("longitude")
            val setAlarmIn10 : Preference? = findPreference("setAlarmIn10")

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val location_value = sharedPreferences.getString("location_input" , "Portlaoise")

            val lat = sharedPreferences.getString("latitude" , "0.0")
            val lon = sharedPreferences.getString("longitude" , "0.0")


            //mute functions
            val AdhanSound : Preference? = findPreference("NotificationSound")
            AdhanSound !!.setOnPreferenceClickListener {
                val intent : Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE , requireContext().packageName)
                startActivity(intent)
                false
            }


            //reset function
            val forceReset : Preference? = findPreference("ForceReset")
            forceReset !!.setOnPreferenceClickListener {

                //**********************************************************************
                val prayerThread = prayerTimeThread(requireContext())
                prayerThread.start()
                //**********************************************************************

                Toast.makeText(requireContext() , "Alarms Reset!" , Toast.LENGTH_SHORT).show()
                false
            }


            setAlarmIn10 !!.setOnPreferenceClickListener {
                val current_timePlus10 = System.currentTimeMillis() + 10000
                val testAdhan =
                    "android.resource://" + requireContext().packageName + "/" + R.raw.zuhar
                NotificationHelper()
                    .createNotificationChannel(
                        requireContext() ,
                        NotificationManagerCompat.IMPORTANCE_HIGH ,
                        true ,
                        "Test Adhan" ,
                        "Test Adhan Channel" ,
                        "channel_id_06" ,
                        testAdhan
                                              )
                val ishaaIntent =
                    Intent(context , ReminderReciever::class.java).apply {
                        putExtra("title" , "Test Adhan")
                        putExtra("channelid" , "channel_id_06")
                        putExtra("notifyid" , 2005)
                        putExtra("time" , current_timePlus10)
                    }
                val PendingIntent = PendingIntent.getBroadcast(context , 8 , ishaaIntent , 0)
                Alarms().setExactAlarm(requireContext() , current_timePlus10 , PendingIntent)
                Toast.makeText(
                    requireContext() ,
                    "Test alarm set in 10 seconds" ,
                    Toast.LENGTH_SHORT
                              ).show()
                Log.i("Alarms Test" , "Test alarm set in 10 seconds")
                false
            }
            latitude !!.isPersistent = true
            longitude !!.isPersistent = true
            location !!.isPersistent = true

            location.setOnBindEditTextListener { editText ->
                editText.inputType = InputType.TYPE_CLASS_TEXT or
                        InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS or
                        InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE or
                        InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
            }

            latitude.setOnBindEditTextListener { editText ->
                editText.inputType = InputType.TYPE_CLASS_NUMBER or
                        InputType.TYPE_NUMBER_FLAG_SIGNED or
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
            }
            longitude.setOnBindEditTextListener { editText ->
                editText.inputType = InputType.TYPE_CLASS_NUMBER or
                        InputType.TYPE_NUMBER_FLAG_SIGNED or
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
            }

            latitude.text = lat !!.toDouble().toString()
            longitude.text = lon !!.toDouble().toString()
            location.text = location_value

            location.positiveButtonText = "SUBMIT"
            location.setOnBindEditTextListener { editText ->
                editText.inputType = InputType.TYPE_CLASS_TEXT
            }
            location.dialogMessage = "Enter City Name"
            location.setOnPreferenceChangeListener { preference , newValue : Any? ->
                val value = newValue as String
                if (value != lat)
                {
                    //write lock to storage
                    with(sharedPreferences.edit()) {
                        putBoolean("alarmLock" , false)
                        putString("location_input" , value)
                        apply()
                    }
                }
                true
            }

            if (isNetworkAvailable)
            {
                location.isEnabled = true
                latitude.isEnabled = false
                longitude.isEnabled = false
                location.text = sharedPreferences.getString("location_input" , "Portlaoise")
            }
            else
            {
                location.isEnabled = false
                location.text = "No Network"
                latitude.isEnabled = true
                longitude.isEnabled = true
            }


            latitude.setOnPreferenceChangeListener { preference , newValue : Any? ->
                val value = newValue as String
                if (value != lat)
                {
                    //write lock to storage
                    with(sharedPreferences.edit()) {
                        putBoolean("alarmLock" , false)
                        putString("latitude" , value)
                        apply()
                    }
                }
                true
            }

            longitude.setOnPreferenceChangeListener { preference , newValue : Any? ->
                val value = newValue as String
                if (value != lon)
                {
                    //write lock to storage
                    with(sharedPreferences.edit()) {
                        putBoolean("alarmLock" , false)
                        putString("longitude" , value)
                        apply()
                    }
                }
                true
            }

            //battery optimization
            val opBattery : Preference? = findPreference("opBattery")
            opBattery !!.setOnPreferenceClickListener {
                val pm : PowerManager =
                    requireContext().getSystemService(POWER_SERVICE) as PowerManager
                if (! pm.isIgnoringBatteryOptimizations(requireContext().packageName))
                {
                    val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                    opBattery.intent = intent
                    false
                }
                else
                {
                    // Create the object of
                    // AlertDialog Builder class
                    val builder : AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    val inflater : LayoutInflater = LayoutInflater.from(requireContext())
                    val locationDialog = inflater.inflate(R.layout.optimizebattery , null)
                    val submitBtn : Button = locationDialog.findViewById(R.id.dialogYes)

                    builder.setView(locationDialog)
                    builder.setCancelable(false)
                    // Create the Alert dialog
                    val alertDialog : AlertDialog = builder.create()
                    // Show the Alert Dialog box
                    alertDialog.show()

                    submitBtn.setOnClickListener {

                        alertDialog.cancel()
                    }
                    true
                }
            }
        }
    }


    class prayerTimeAdjust : PreferenceFragmentCompat()
    {

        override fun onCreatePreferences(savedInstanceState : Bundle? , rootKey : String?)
        {
            setPreferencesFromResource(R.xml.prayer_time_adjust , rootKey)

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val calcMethod : ListPreference? = findPreference("calcMethod")
            val latituderule : ListPreference? = findPreference("highlatrule")
            val latituderulevalue = sharedPreferences.getString("highlatrule" , "TWILIGHT_ANGLE")

            //calculation  method
            val calculationMethod = sharedPreferences.getString("calcMethod" , "IRELAND")

            //adjustements
            val fajrAngle : ListPreference? = findPreference("fajrAngle")
            val ishaaAngle : ListPreference? = findPreference("ishaaAngle")


            val fajr : ListPreference? = findPreference("fajr")
            val sunrise : ListPreference? = findPreference("sunrise")
            val zuhar : ListPreference? = findPreference("zuhar")
            val asar : ListPreference? = findPreference("asar")
            val maghrib : ListPreference? = findPreference("maghrib")
            val ishaa : ListPreference? = findPreference("ishaa")

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

            //madhab
            val madhab : SwitchPreference? = findPreference("madhab")
            var madhabCheck = sharedPreferences.getBoolean("madhab" , true)

            latituderule !!.setOnPreferenceChangeListener { preference , newValue : Any? ->
                val value = newValue as String
                if (value != latituderulevalue)
                {
                    //write lock to storage
                    with(sharedPreferences.edit()) {
                        putBoolean("alarmLock" , false)
                        apply()
                    }
                }
                true
            }

            fajrAngle !!.setOnPreferenceChangeListener { preference , newValue : Any? ->
                val value = newValue as String
                if (value != fajr_angle)
                {
                    //write lock to storage
                    with(sharedPreferences.edit()) {
                        putBoolean("alarmLock" , false)
                        apply()
                    }
                }
                true
            }

            ishaaAngle !!.setOnPreferenceChangeListener { preference , newValue : Any? ->
                val value = newValue as String
                if (value != ishaa_angle)
                {
                    //write lock to storage
                    with(sharedPreferences.edit()) {
                        putBoolean("alarmLock" , false)
                        apply()
                    }
                }
                true
            }

            fajr !!.setOnPreferenceChangeListener { preference , newValue : Any? ->
                val value = newValue as String
                if (value != fajr_adjust)
                {
                    //write lock to storage
                    with(sharedPreferences.edit()) {
                        putBoolean("alarmLock" , false)
                        apply()
                    }
                }
                true
            }

            sunrise !!.setOnPreferenceChangeListener { preference , newValue : Any? ->
                val value = newValue as String
                if (value != sunrise_adjust)
                {
                    //write lock to storage
                    with(sharedPreferences.edit()) {
                        putBoolean("alarmLock" , false)
                        apply()
                    }
                }
                true
            }

            zuhar !!.setOnPreferenceChangeListener { preference , newValue : Any? ->
                val value = newValue as String
                if (value != zuhar_adjust)
                {
                    //write lock to storage
                    with(sharedPreferences.edit()) {
                        putBoolean("alarmLock" , false)
                        apply()
                    }
                }
                true
            }
            asar !!.setOnPreferenceChangeListener { preference , newValue : Any? ->
                val value = newValue as String
                if (value != asar_adjust)
                {
                    //write lock to storage
                    with(sharedPreferences.edit()) {
                        putBoolean("alarmLock" , false)
                        apply()
                    }
                }
                true
            }
            maghrib !!.setOnPreferenceChangeListener { preference , newValue : Any? ->
                val value = newValue as String
                if (value != maghrib_adjust)
                {
                    //write lock to storage
                    with(sharedPreferences.edit()) {
                        putBoolean("alarmLock" , false)
                        apply()
                    }
                }
                true
            }

            ishaa !!.setOnPreferenceChangeListener { preference , newValue : Any? ->
                val value = newValue as String
                if (value != ishaa_adjust)
                {
                    //write lock to storage
                    with(sharedPreferences.edit()) {
                        putBoolean("alarmLock" , false)
                        apply()
                    }
                }
                true
            }


            //preset method selection
            calcMethod !!.setOnPreferenceChangeListener { preference , newValue : Any? ->
                val value = newValue as String
                if (value != calculationMethod)
                {
                    //write lock to storage
                    with(sharedPreferences.edit()) {
                        putBoolean("alarmLock" , false)
                        apply()
                    }
                }
                true
            }

            //madhab states
            if (madhabCheck)
            {
                madhab !!.setOnPreferenceChangeListener { preference , newValue ->
                    if (newValue as Boolean)
                    {
                        madhab.title = "Shafi,Maliki,Hanbali"

                        //reset alarms
                        with(sharedPreferences.edit()) {
                            putBoolean("alarmLock" , false)
                            apply()
                        }

                    }
                    else if (newValue == false)
                    {
                        madhab.title = "Hanafi"

                        //reset alarms
                        with(sharedPreferences.edit()) {
                            putBoolean("alarmLock" , false)
                            apply()
                        }

                    }
                    true
                }
            }
            else if (! madhabCheck)
            {
                madhab !!.setOnPreferenceChangeListener { preference , newValue ->
                    if (newValue as Boolean)
                    {
                        madhab.title = "Shafi,Maliki,Hanbali"

                        //reset alarms
                        with(sharedPreferences.edit()) {
                            putBoolean("alarmLock" , false)
                            apply()
                        }

                    }
                    else if (newValue == false)
                    {
                        madhab.title = "Hanafi"

                        //reset alarms
                        with(sharedPreferences.edit()) {
                            putBoolean("alarmLock" , false)
                            apply()
                        }

                    }
                    true
                }
            }


            //default values based on method selected
            calcMethod.setOnPreferenceChangeListener { preference , newValue ->
                val newvalue = newValue as String
                when (newvalue)
                {
                    "MUSLIM_WORLD_LEAGUE" ->
                    {
                        ishaaAngle.isVisible = true
                        fajrAngle.value = "18.0"
                        ishaaAngle.value = "17.0"
                        fajr.value = "2"
                        sunrise.value = "-1"
                        zuhar.value = "5"
                        asar.value = "0"
                        maghrib.value = "2"
                        ishaa.value = "-1"
                        madhabCheck = true
                    }

                    "EGYPTIAN" ->
                    {
                        ishaaAngle.isVisible = true
                        fajrAngle.value = "19.5"
                        ishaaAngle.value = "17.5"
                        fajr.value = "2"
                        sunrise.value = "-1"
                        zuhar.value = "5"
                        asar.value = "0"
                        maghrib.value = "2"
                        ishaa.value = "-1"
                        madhabCheck = true
                    }

                    "KARACHI" ->
                    {
                        ishaaAngle.isVisible = true
                        fajrAngle.value = "18.0"
                        ishaaAngle.value = "18.0"
                        fajr.value = "2"
                        sunrise.value = "-1"
                        zuhar.value = "5"
                        asar.value = "0"
                        maghrib.value = "2"
                        ishaa.value = "-1"
                        madhabCheck = true
                    }

                    "UMM_AL_QURA" ->
                    {
                        fajrAngle.value = "18.5"
                        ishaaAngle.isVisible = false
                        fajr.value = "0"
                        sunrise.value = "0"
                        zuhar.value = "0"
                        asar.value = "0"
                        maghrib.value = "0"
                        ishaa.value = "0"
                        madhabCheck = true
                    }

                    "DUBAI" ->
                    {
                        ishaaAngle.isVisible = true
                        fajrAngle.value = "18.5"
                        ishaaAngle.value = "18.5"
                        fajr.value = "2"
                        sunrise.value = "-1"
                        zuhar.value = "5"
                        asar.value = "0"
                        maghrib.value = "2"
                        ishaa.value = "-1"
                        madhabCheck = true
                    }

                    "MOON_SIGHTING_COMMITTEE" ->
                    {
                        ishaaAngle.isVisible = true
                        fajrAngle.value = "18.0"
                        ishaaAngle.value = "18.0"
                        fajr.value = "0"
                        sunrise.value = "0"
                        zuhar.value = "5"
                        asar.value = "0"
                        maghrib.value = "3"
                        ishaa.value = "0"
                        madhabCheck = true
                    }

                    "NORTH_AMERICA" ->
                    {
                        ishaaAngle.isVisible = true
                        fajrAngle.value = "15.0"
                        ishaaAngle.value = "15.0"
                        fajr.value = "2"
                        sunrise.value = "-1"
                        zuhar.value = "5"
                        asar.value = "0"
                        maghrib.value = "2"
                        ishaa.value = "-1"
                        madhabCheck = true
                    }

                    "KUWAIT" ->
                    {
                        ishaaAngle.isVisible = true
                        fajrAngle.value = "18.0"
                        ishaaAngle.value = "17.5"
                        fajr.value = "0"
                        sunrise.value = "0"
                        zuhar.value = "0"
                        asar.value = "0"
                        maghrib.value = "0"
                        ishaa.value = "0"
                        madhabCheck = true
                    }

                    "QATAR" ->
                    {
                        fajrAngle.value = "18.0"
                        ishaaAngle.isVisible = false
                        fajr.value = "0"
                        sunrise.value = "0"
                        zuhar.value = "0"
                        asar.value = "0"
                        maghrib.value = "0"
                        ishaa.value = "0"
                        madhabCheck = true
                    }

                    "SINGAPORE" ->
                    {
                        ishaaAngle.isVisible = true
                        fajrAngle.value = "20.0"
                        ishaaAngle.value = "18.0"
                        fajr.value = "2"
                        sunrise.value = "-1"
                        zuhar.value = "5"
                        asar.value = "0"
                        maghrib.value = "2"
                        ishaa.value = "-1"
                        madhabCheck = true
                    }

                    "FRANCE" ->
                    {
                        ishaaAngle.isVisible = true
                        fajrAngle.value = "12.0"
                        ishaaAngle.value = "12.0"
                        fajr.value = "2"
                        sunrise.value = "-1"
                        zuhar.value = "5"
                        asar.value = "0"
                        maghrib.value = "2"
                        ishaa.value = "-1"
                        madhabCheck = true
                    }

                    "RUSSIA" ->
                    {
                        ishaaAngle.isVisible = true
                        fajrAngle.value = "16.0"
                        ishaaAngle.value = "14.0"
                        fajr.value = "2"
                        sunrise.value = "-1"
                        zuhar.value = "5"
                        asar.value = "0"
                        maghrib.value = "2"
                        ishaa.value = "-1"
                        madhabCheck = true
                    }

                    "IRELAND" ->
                    {
                        ishaaAngle.isVisible = true
                        fajrAngle.value = "14.0"
                        ishaaAngle.value = "13.0"
                        fajr.value = "0"
                        sunrise.value = "0"
                        zuhar.value = "0"
                        asar.value = "0"
                        maghrib.value = "0"
                        ishaa.value = "0"
                        madhabCheck = true
                    }

                    "OTHER" ->
                    {
                        ishaaAngle.isVisible = true
                        fajrAngle.value = "0.0"
                        ishaaAngle.value = "0.0"
                        fajr.value = "0"
                        sunrise.value = "0"
                        zuhar.value = "0"
                        asar.value = "0"
                        maghrib.value = "0"
                        ishaa.value = "0"
                        madhabCheck = false
                    }

                }
                true
            }

        }//oncreate

    } // prayer time adjustments
} // class

