package com.arshadshah.nimaz.fragments.intro

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.activities.HomeActivity
import com.arshadshah.nimaz.R

class LocationSelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_location_selection, container, false)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val manual: Button = root.findViewById(R.id.manual)
        val auto: Button = root.findViewById(R.id.auto)
        val locationSkip: Button = root.findViewById(R.id.locationSkip)

        manual.setOnClickListener {
            val navcontroller = requireActivity().findNavController(R.id.fragmentContainerView)
            navcontroller.navigate(R.id.locationInputFragment)
            with(sharedPreferences.edit()) {
                putBoolean("locationType" , false)
                apply()
            }
        }
        auto.setOnClickListener {

            with(sharedPreferences.edit()) {
                putBoolean("locationType" , true)
                putBoolean("isFirstInstall" , false)
                putBoolean("channelLock" , false)
                apply()
            }

            val intent = Intent(requireContext() , HomeActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        //if skipped
        locationSkip.setOnClickListener {
            with(sharedPreferences.edit()) {
                putBoolean("isFirstInstall" , false)
                putBoolean("channelLock" , false)
                apply()
            }
            createDialog()
        }

        return root
    }


    /***
     * create a dialog to add a reminder
     */
    private fun createDialog() {
        // Create the object of
        // AlertDialog Builder class
        val builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater? = activity?.layoutInflater
        val locationInputDialog: View = inflater!!.inflate(R.layout.locationskip, null)
        val dialogYes:Button = locationInputDialog.findViewById(R.id.dialogYes)
        val dialogNo:Button = locationInputDialog.findViewById(R.id.dialogNo)
        builder.setView(locationInputDialog)
        // Set Cancelable false
        builder.setCancelable(false)
        // Create the Alert dialog
        val alertDialog = builder.create()
        // Show the Alert Dialog box
        alertDialog.show()
        dialogYes.setOnClickListener { view: View? ->
            val intent = Intent(requireContext() , HomeActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        dialogNo.setOnClickListener { view: View? ->
            alertDialog.cancel()
        }
    }
}