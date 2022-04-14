package com.arshadshah.nimaz.fragments.quran

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.activities.quran.QuranMainList
import com.arshadshah.nimaz.helperClasses.database.DatabaseAccessHelper
import com.arshadshah.nimaz.helperClasses.quran.SurahListCustomAdapter
import com.arshadshah.nimaz.helperClasses.quran.SurahObject

class QuranSurahFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_quran_surah, container, false)

        val surahList: ListView = root.findViewById(R.id.surahList)

        surahList.divider = null

        val helper = DatabaseAccessHelper(requireContext())
        helper.open()

        val cursorForSuras = helper.getAllSuras()

        val surahObjects = ArrayList<SurahObject>()
        while (cursorForSuras!!.moveToNext()) {

            val surah = SurahObject(
                cursorForSuras.getString(0),
                cursorForSuras.getString(3),
                cursorForSuras.getString(4),
                cursorForSuras.getString(5),
                cursorForSuras.getString(6),
                cursorForSuras.getString(1),
                cursorForSuras.getString(8)
            )

            surahObjects.add(surah)
        }

        cursorForSuras.close()

        //create a custom adapter
        val surahListCustomAdapter = SurahListCustomAdapter(requireContext(), surahObjects)

        //set the adapter to the listview
        surahList.adapter = surahListCustomAdapter

        surahList.setOnItemClickListener { parent, view, position, id ->
            val surahClicked = surahObjects[position]
            val intent = Intent(requireContext(), QuranMainList::class.java)
            intent.putExtra("number", position)
            intent.putExtra("fragment", "surah")
            intent.putExtra("name", surahClicked.arabic)
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
        val surahList = requireView().findViewById<ListView>(R.id.surahList)
        editor.putInt("lastPositionSurahMain", surahList.firstVisiblePosition)
        editor.apply()
    }

    //a function that restores the last position of the listview before the fragment is created
    override fun onResume() {
        super.onResume()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val lastPosition = sharedPreferences.getInt("lastPositionSurahMain", 0)
        val surahList = requireView().findViewById<ListView>(R.id.surahList)
        surahList.setSelection(lastPosition)
    }

}