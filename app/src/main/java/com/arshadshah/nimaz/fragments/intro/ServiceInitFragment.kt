package com.arshadshah.nimaz.fragments.intro

import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.activities.HomeActivity
import com.arshadshah.nimaz.helperClasses.fusedLocations.LocationFinderAuto
import com.arshadshah.nimaz.helperClasses.utils.LocationFinder
import com.google.android.material.switchmaterial.SwitchMaterial

class ServiceInitFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_service_init, container, false)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val locationTypeValue = sharedPreferences.getBoolean("locationType", true)

        val locationFinish: Button = root.findViewById(R.id.locationFinish)

        if (locationTypeValue) {
            LocationFinderAuto().getLocations(requireContext(), 12345)
        }

        val latitude = sharedPreferences.getString("latitude", "0.0")!!.toDouble()
        val longitude = sharedPreferences.getString("longitude", "0.0")!!.toDouble()
        LocationFinder().findCityName(requireContext(), latitude, longitude)

        val batteryOpt: SwitchMaterial = root.findViewById(R.id.batteryOpt)
        val pm: PowerManager =
            requireContext().getSystemService(AppCompatActivity.POWER_SERVICE) as PowerManager

        batteryOpt.isChecked = pm.isIgnoringBatteryOptimizations(requireContext().packageName)

        //if batterOpt is true then disable the switch
        batteryOpt.isEnabled = !batteryOpt.isChecked


        batteryOpt.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                startActivity(intent)
            }
        }

        locationFinish.setOnClickListener {
            with(sharedPreferences.edit()) {
                putBoolean("isFirstInstall", false)
                putBoolean("navigateToHome", true)
                putBoolean("channelLock", false)
                apply()
            }
            //navigate to homeactivity and finish this activity
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return root
    }
}