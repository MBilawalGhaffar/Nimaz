package com.arshadshah.nimaz.fragments.settings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.preference.*
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.alarms.Alarms
import com.arshadshah.nimaz.helperClasses.fusedLocations.LocationFinderAuto
import com.arshadshah.nimaz.helperClasses.prayertimes.prayerTimeThread
import com.arshadshah.nimaz.helperClasses.utils.NetworkChecker
import com.arshadshah.nimaz.helperClasses.utils.NotificationHelper
import com.arshadshah.nimaz.helperClasses.utils.locationFinder
import com.arshadshah.nimaz.recievers.ReminderReciever

class MainPage : PreferenceFragmentCompat() {

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.header_preferences, rootKey)

        val isNetworkAvailable = NetworkChecker().networkCheck(requireContext())

        val location: EditTextPreference? = findPreference("location_input")

        val locationType: SwitchPreference? = findPreference("locationType")

        val latitude: EditTextPreference? = findPreference("latitude")
        val longitude: EditTextPreference? = findPreference("longitude")
        val setAlarmIn10: Preference? = findPreference("setAlarmIn10")

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val location_value = sharedPreferences.getString("location_input", "Portlaoise")

        val locationTypeValue = sharedPreferences.getBoolean("locationType", true)

        val lat = sharedPreferences.getString("latitude", "0.0")
        val lon = sharedPreferences.getString("longitude", "0.0")

        if (locationTypeValue) {
            locationType!!.title = "Automatic"
            location!!.isVisible = false
            latitude!!.isVisible = false
            longitude!!.isVisible = false
            locationType.setOnPreferenceChangeListener { preference, newValue ->
                if (newValue as Boolean) {
                    locationType.title = "Automatic"
                    location.isVisible = false
                    latitude.isVisible = false
                    longitude.isVisible = false

                    with(sharedPreferences.edit()) {
                        putBoolean("locationType", true)
                        apply()
                    }
                    LocationFinderAuto().getLocations(requireActivity(), 12345)
                } else if (newValue == false) {
                    locationType.title = "Manual"
                    location.isVisible = true
                    latitude.isVisible = true
                    longitude.isVisible = true
                    //reset alarms
                    with(sharedPreferences.edit()) {
                        putBoolean("locationType", false)
                        apply()
                    }

                }
                true
            }

        } else {
            location!!.isVisible = true
            latitude!!.isVisible = true
            longitude!!.isVisible = true
            locationType!!.title = "Manual"
            locationType.setOnPreferenceChangeListener { preference, newValue ->
                if (newValue as Boolean) {
                    locationType.title = "Automatic"
                    location.isVisible = false
                    latitude.isVisible = false
                    longitude.isVisible = false

                    with(sharedPreferences.edit()) {
                        putBoolean("locationType", true)
                        apply()
                    }
                    LocationFinderAuto().getLocations(requireActivity(), 12345)
                } else if (newValue == false) {
                    locationType.title = "Manual"
                    location.isVisible = true
                    latitude.isVisible = true
                    longitude.isVisible = true
                    //reset alarms
                    with(sharedPreferences.edit()) {
                        putBoolean("locationType", false)
                        apply()
                    }

                }
                true
            }
        }


        //navigation in settings
        val prayerTimeAdjust: Preference? = findPreference("time_calc")

        changeFragment(prayerTimeAdjust, PrayerTimeAdjustFragment(), "Prayer Times Adjustments")

        val tandc: Preference? = findPreference("tandc")

        changeFragment(tandc, TandC(), "Terms and Conditions")

        val privacy: Preference? = findPreference("privacy")

        changeFragment(privacy, Privacypolicy(), "Privacy Policy")

        val about: Preference? = findPreference("about")

        changeFragment(about, About(), "About")


        //mute functions
        val AdhanSound: Preference? = findPreference("NotificationSound")
        AdhanSound!!.setOnPreferenceClickListener {
            val intent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            startActivity(intent)
            false
        }


        //reset function
        val forceReset: Preference? = findPreference("ForceReset")
        forceReset!!.setOnPreferenceClickListener {

            //**********************************************************************
            val prayerThread = prayerTimeThread(requireContext())
            prayerThread.start()
            //**********************************************************************

            Toast.makeText(requireContext(), "Alarms Reset!", Toast.LENGTH_SHORT).show()
            false
        }


        setAlarmIn10!!.setOnPreferenceClickListener {
            val current_timePlus10 = System.currentTimeMillis() + 10000
            val testAdhan =
                "android.resource://" + requireContext().packageName + "/" + R.raw.zuhar
            NotificationHelper()
                .createNotificationChannel(
                    requireContext(),
                    NotificationManagerCompat.IMPORTANCE_HIGH,
                    true,
                    "Test Adhan",
                    "Test Adhan Channel",
                    "channel_id_06",
                    testAdhan
                )
            val ishaaIntent =
                Intent(context, ReminderReciever::class.java).apply {
                    putExtra("title", "Test Adhan")
                    putExtra("channelid", "channel_id_06")
                    putExtra("notifyid", 2005)
                    putExtra("time", current_timePlus10)
                }
            val PendingIntent = PendingIntent.getBroadcast(context, 8, ishaaIntent, FLAG_IMMUTABLE)
            Alarms().setExactAlarm(requireContext(), current_timePlus10, PendingIntent)
            Toast.makeText(
                requireContext(),
                "Test alarm set in 10 seconds",
                Toast.LENGTH_SHORT
            ).show()
            Log.i("Alarms Test", "Test alarm set in 10 seconds")
            false
        }
        latitude.isPersistent = true
        longitude.isPersistent = true
        location.isPersistent = true

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

        latitude.text = lat!!.toDouble().toString()
        longitude.text = lon!!.toDouble().toString()
        location.text = location_value

        location.positiveButtonText = "SUBMIT"
        location.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_TEXT
        }
        location.dialogMessage = "Enter City Name"
        location.setOnPreferenceChangeListener { preference, newValue: Any? ->
            val value = newValue as String
            if (value != lat) {
                locationFinder().findLongAndLan(requireContext(), value)
                //write lock to storage
                with(sharedPreferences.edit()) {
                    putBoolean("alarmLock", false)
                    putString("location_input", value)
                    apply()
                }
            }
            true
        }

        if (isNetworkAvailable) {
            location.isEnabled = true
            latitude.isEnabled = false
            longitude.isEnabled = false
            location.text = sharedPreferences.getString("location_input", "Portlaoise")
        } else {
            location.isEnabled = false
            location.text = "No Network"
            latitude.isEnabled = true
            longitude.isEnabled = true
        }


        latitude.setOnPreferenceChangeListener { preference, newValue: Any? ->
            val value = newValue as String
            if (value != lat) {
                //write lock to storage
                with(sharedPreferences.edit()) {
                    putBoolean("alarmLock", false)
                    putString("latitude", value)
                    apply()
                }
            }
            true
        }

        longitude.setOnPreferenceChangeListener { preference, newValue: Any? ->
            val value = newValue as String
            if (value != lon) {
                //write lock to storage
                with(sharedPreferences.edit()) {
                    putBoolean("alarmLock", false)
                    putString("longitude", value)
                    apply()
                }
            }
            true
        }

        //battery optimization
        val opBattery: Preference? = findPreference("opBattery")
        opBattery!!.setOnPreferenceClickListener {
            val pm: PowerManager =
                requireContext().getSystemService(AppCompatActivity.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(requireContext().packageName)) {
                val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                opBattery.intent = intent
                false
            } else {
                // Create the object of
                // AlertDialog Builder class
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                val inflater: LayoutInflater = LayoutInflater.from(requireContext())
                val locationDialog = inflater.inflate(R.layout.optimizebattery, null)
                val submitBtn: Button = locationDialog.findViewById(R.id.dialogYes)

                builder.setView(locationDialog)
                builder.setCancelable(false)
                // Create the Alert dialog
                val alertDialog: AlertDialog = builder.create()
                // Show the Alert Dialog box
                alertDialog.show()

                submitBtn.setOnClickListener {

                    alertDialog.cancel()
                }
                true
            }
        }
    }
    //oncreate

    private fun changeFragment(
        preferenceClicked: Preference?,
        fragmentToGoTo: Fragment,
        fragmentName: String
    ) {
        preferenceClicked?.setOnPreferenceClickListener {
            val bundle = Bundle()
            bundle.putString("fragmentName", fragmentName)

            //navigate to fragment with bundle
            fragmentToGoTo.arguments = bundle
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.settings, fragmentToGoTo)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            false
        }

    }
}