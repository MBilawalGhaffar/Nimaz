package com.arshadshah.nimaz.fragments.quran

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.arshadshah.nimaz.QuranMainList
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.database.DatabaseAccessHelper
import com.arshadshah.nimaz.helperClasses.quran.JuzListCustomAdapter
import com.arshadshah.nimaz.helperClasses.quran.JuzObject

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

}