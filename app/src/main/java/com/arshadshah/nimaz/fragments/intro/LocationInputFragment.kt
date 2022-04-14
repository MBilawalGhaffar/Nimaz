package com.arshadshah.nimaz.fragments.intro

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.activities.HomeActivity

class LocationInputFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_location_input, container, false)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val locationFinish: Button = root.findViewById(R.id.locationFinish)
        val locationSkip: Button = root.findViewById(R.id.locationSkip)

        locationFinish.setOnClickListener {
            val city: EditText = root.findViewById(R.id.city)

            if (city.text.length > 2) {
                val currentLocation = city.text.toString()
                with(sharedPreferences.edit()) {
                    putString("location_input", currentLocation)
                    putBoolean("isFirstInstall", false)
                    putBoolean("navigateToHome", true)
                    putBoolean("channelLock", false)
                    apply()
                }
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
                Toast.makeText(
                    requireContext(),
                    "Showing Prayer times for $currentLocation",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        //if skipped
        locationSkip.setOnClickListener {
            with(sharedPreferences.edit()) {
                putBoolean("isFirstInstall", false)
                putBoolean("channelLock", false)
                apply()
            }
            createDialog()
        }
        return root
    }

    /***
     * create a dialog to add a reminder
     */
    fun createDialog() {
        // Create the object of
        // AlertDialog Builder class
        val builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater? = activity?.layoutInflater
        val locationInputDialog: View = inflater!!.inflate(R.layout.locationskip, null)
        val dialogYes: Button = locationInputDialog.findViewById(R.id.dialogYes)
        val dialogNo: Button = locationInputDialog.findViewById(R.id.dialogNo)
        builder.setView(locationInputDialog)
        // Set Cancelable false
        builder.setCancelable(false)
        // Create the Alert dialog
        val alertDialog = builder.create()
        // Show the Alert Dialog box
        alertDialog.show()
        dialogYes.setOnClickListener { view: View? ->
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        dialogNo.setOnClickListener { view: View? ->
            alertDialog.cancel()
        }
    }
}