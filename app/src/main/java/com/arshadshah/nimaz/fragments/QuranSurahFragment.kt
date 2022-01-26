package com.arshadshah.nimaz.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Xml
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.arshadshah.nimaz.QuranMainList
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.*
import com.arshadshah.nimaz.helperClasses.SurahListCustomAdapter
import org.xmlpull.v1.XmlPullParser

class QuranSurahFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_quran_surah, container, false)

        val surahList: ListView = root.findViewById(R.id.surahList)

        val helper = DatabaseAccessHelper(requireContext())
        helper.open()

        val cursorForSuras = helper.getAllSuras()

        val surahObjects = ArrayList<SurahObject>()
        while (cursorForSuras!!.moveToNext()) {

            val surah = SurahObject(cursorForSuras.getString(0), cursorForSuras.getString(3), cursorForSuras.getString(4), cursorForSuras.getString(5), cursorForSuras.getString(6), cursorForSuras.getString(1), cursorForSuras.getString(8))

            surahObjects.add(surah)
        }

        cursorForSuras.close()

        //create a custom adapter
        val surahListCustomAdapter = SurahListCustomAdapter(requireContext(), surahObjects)

        //set the adapter to the listview
        surahList.adapter = surahListCustomAdapter

        surahList.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(requireContext() , QuranMainList::class.java)
            intent.putExtra("number",position)
            intent.putExtra("fragment","surah")
            startActivity(intent)
        }

        helper.close()
        return root
    }

}