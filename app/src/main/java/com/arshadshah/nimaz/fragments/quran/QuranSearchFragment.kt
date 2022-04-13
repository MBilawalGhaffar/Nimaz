package com.arshadshah.nimaz.fragments.quran

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.database.BookmarkDatabaseAccessHelper
import com.arshadshah.nimaz.helperClasses.database.DatabaseAccessHelper
import com.arshadshah.nimaz.helperClasses.quran.AyaListCustomAdapter
import com.arshadshah.nimaz.helperClasses.quran.AyaObject

class QuranSearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val root = inflater.inflate(R.layout.fragment_quran_search, container, false)

        //get the arguments from the bundle
        val bundle = this.arguments
        val searchQuery = bundle?.getString("query")

        val ayaList: ListView = root.findViewById(R.id.ayaList)

        //create a custom adapter
        lateinit var ayaListCustomAdapter: AyaListCustomAdapter


        lateinit var ayaFound: ArrayList<AyaObject?>

        //create a separate thread to get the data from the database
        val thread = Thread {
            Looper.prepare()
            val helper = DatabaseAccessHelper(requireContext())
            helper.open()
            //create an instance of a cardview to the fragment
            val progressBarContainer = root.findViewById<CardView>(R.id.progressContainer)

            val progressBar = root.findViewById<ProgressBar>(R.id.progressBar)

            activity?.runOnUiThread {
                progressBarContainer.isVisible = true
                //show a progess bar while the data is being fetched
                progressBar.isVisible = true
            }
            ayaFound = helper.searchForAya(searchQuery.toString(), "en_sahih", "text" )

            activity?.runOnUiThread {
                progressBarContainer.isVisible = false
                progressBar.isVisible = false
                ayaList.divider = null
                ayaListCustomAdapter = AyaListCustomAdapter(requireContext(), ayaFound)
                ayaList.adapter = ayaListCustomAdapter
            }
            helper.close()
        }
        thread.start()

        return root
    }
}