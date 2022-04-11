package com.arshadshah.nimaz.fragments.quran

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
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
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        //get the arguments from the bundle
        val bundle = this.arguments
        val searchQuery = bundle?.getString("query")

        val ayaList: ListView = root.findViewById(R.id.ayaList)

        //create a custom adapter
        lateinit var ayaListCustomAdapter: AyaListCustomAdapter


        //check the preference for the bookmark
        val ayaNumberBookmark = sharedPreferences.getString("ayaNumberBookmark", "")
        val ayaTextBookmark = sharedPreferences.getString("ayaTextBookmark", "")
        val ayaArabicBookmark = sharedPreferences.getString("ayaArabicBookmark", "")
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

                //when this is first created, the listview is scrolled to the position where the user made a bookmark
                //this is retrieved from the shared preferences
                //check if the list has a bookmark
                if(ayaNumberBookmark != null && ayaTextBookmark != null && ayaArabicBookmark != null){
                    //check if the bookmark is in the list by comparing the ayaNumberBookmark with the ayaNumber of the list
                    //and the ayaTextBookmark with the ayaText of the list
                    //and the ayaArabicBookmark with the ayaArabic of the list
                    for(i in 0 until ayaFound.size){
                        if(ayaNumberBookmark == ayaFound[i]?.ayaNumber && ayaTextBookmark == ayaFound[i]?.ayaEnglish && ayaArabicBookmark == ayaFound[i]?.ayaArabic){
                            //scroll to the position of the bookmark
                            ayaList.setSelection(i)
                            break
                        }
                    }
                }
            }
            helper.close()
        }
        thread.start()

        //on long click
        ayaList.setOnItemLongClickListener { parent, view, position, id ->
            //retrieve the aya object from the list
            val ayaObject = ayaFound[position]

            if(ayaNumberBookmark == ayaObject?.ayaNumber && ayaTextBookmark == ayaObject?.ayaEnglish && ayaArabicBookmark == ayaObject?.ayaArabic){
                //remove the bookmark and set the preference to null
                sharedPreferences.edit().putString("ayaNumberBookmark", "").apply()
                sharedPreferences.edit().putString("ayaTextBookmark", "").apply()
                sharedPreferences.edit().putString("ayaArabicBookmark", "").apply()

                //change the background color of the item back to normal
                view.setBackgroundColor(resources.getColor(R.color.background))
            }
            else{
                //change the color of the item that is clicked on to green
                view.setBackgroundColor(resources.getColor(R.color.bookmark))

                //save the data of the item that is clicked on to shared preferences
                val editor = sharedPreferences.edit()
                editor.putString("ayaNumberBookmark", ayaObject?.ayaNumber)
                editor.putString("ayaTextBookmark", ayaObject?.ayaEnglish)
                editor.putString("ayaArabicBookmark", ayaObject?.ayaArabic)
                editor.apply()
            }

            true
        }
        return root
    }
}