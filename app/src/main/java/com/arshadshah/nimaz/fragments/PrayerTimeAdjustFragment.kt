package com.arshadshah.nimaz.fragments

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.arshadshah.nimaz.R

class PrayerTimeAdjustFragment : PreferenceFragmentCompat()
{

    override fun onCreatePreferences(savedInstanceState : Bundle?, rootKey : String?)
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