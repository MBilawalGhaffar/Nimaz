package com.arshadshah.nimaz.fragments.quran

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.database.DatabaseAccessHelper
import com.arshadshah.nimaz.helperClasses.quran.JuzListCustomAdapter
import com.arshadshah.nimaz.helperClasses.quran.JuzObject
import com.arshadshah.nimaz.activities.quran.QuranMainList

class QuranJuzFragment : Fragment() {

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_quran_juz, container, false)

        val juzList: ListView = root.findViewById(R.id.juzList)
        juzList.divider = null

        val helper = DatabaseAccessHelper(requireContext())
        helper.open()

        val cursorForJuzName = helper.getAllJuzs()
        
        //get an array list of JuzObjects
        val juzObjects = ArrayList<JuzObject>()
        while(cursorForJuzName!!.moveToNext()){
            val juzObject = JuzObject(cursorForJuzName.getString(0), cursorForJuzName.getString(3), cursorForJuzName.getString(2))
            juzObjects.add(juzObject)
        }
        cursorForJuzName.close()


        //create a custom adapter
        val JuzListCustomAdapter = JuzListCustomAdapter(requireContext(), juzObjects)

        //set the adapter to the listview
        juzList.adapter = JuzListCustomAdapter


        juzList.setOnItemClickListener { parent, view, position, id ->
            val juzClicked = juzObjects[position]
            val intent = Intent(requireContext() , QuranMainList::class.java)
            intent.putExtra("number",position)
            intent.putExtra("fragment","juz")
            intent.putExtra("name",juzClicked.arabic)
            startActivity(intent)
        }

        helper.close()

        return root
    }

        //a function that saves the last position of the listview before the fragment is paused
    override fun onPause() {
        super.onPause()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = sharedPreferences.edit()
        val juzList = requireView().findViewById<ListView>(R.id.juzList)
        editor.putInt("lastPositionJuzMain", juzList.firstVisiblePosition)
        editor.apply()
    }

    //a function that restores the last position of the listview before the fragment is created
    override fun onResume() {
        super.onResume()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val lastPosition = sharedPreferences.getInt("lastPositionJuzMain", 0)
        val juzList = requireView().findViewById<ListView>(R.id.juzList)
        juzList.setSelection(lastPosition)
    }
}