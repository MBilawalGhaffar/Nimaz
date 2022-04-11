package com.arshadshah.nimaz.fragments.quran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.database.DatabaseAccessHelper
import com.arshadshah.nimaz.helperClasses.quran.AyaListCustomAdapter
import com.arshadshah.nimaz.helperClasses.quran.AyaObject

class AyaListJuzFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_aya_list, container, false)

        val helper = DatabaseAccessHelper(requireContext())
        helper.open()

        //get the juzNumber from bundle
        val number = requireArguments().getInt("number")

        val ayaForJuz = helper.getAllAyaForJuz(number+1)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val isEnglish = sharedPreferences.getBoolean("isEnglish", true)

        //add the following object to index 0 of ayaForSurah without losing value of index 0 in ayaForSurah
        val ayaNumberOfBismillah= "0"
        var ayaOfBismillah =""
        ayaOfBismillah = if(isEnglish){
            "In the name of Allah, the Entirely Merciful, the Especially Merciful."
        } else{
            "اللہ کے نام سے جو رحمان و رحیم ہے"
        }
        val ayaArabicOfBismillah = "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ"
        val bismillah  = AyaObject(ayaNumberOfBismillah,ayaOfBismillah,ayaArabicOfBismillah)
        
        //find all the objects in arraylist ayaForJuz where ayaForJuz[i]!!.ayaNumber = 1
        //add object bismillah before it for every occurance of ayaForJuz[i]!!.ayaNumber = 1
        var index = 0
        while(index < ayaForJuz.size){
            if(ayaForJuz[index]!!.ayaArabic != bismillah.ayaArabic){
                //add bismillah before ayaForJuz[i]
                if(ayaForJuz[index]!!.ayaNumber == "1"){
                    if(number+1 != 10 && index != 36){
                        ayaForJuz.add(index, bismillah)
                        //skip the next iteration
                        index++
                    }
                }
            }
            index++
        }

        val ayaList: ListView = root.findViewById(R.id.ayaList)

        ayaList.divider = null

        //create a custom adapter
        val ayaListCustomAdapter = AyaListCustomAdapter(requireContext(), ayaForJuz)

        //set the adapter to the listview
        ayaList.adapter = ayaListCustomAdapter

        helper.close()

        //check the preference for the bookmark
        val ayaNumberBookmark = sharedPreferences.getString("ayaNumberBookmark", "")
        val ayaTextBookmark = sharedPreferences.getString("ayaTextBookmark", "")
        val ayaArabicBookmark = sharedPreferences.getString("ayaArabicBookmark", "")

        //when this is first created, the listview is scrolled to the position where the user made a bookmark
        //this is retrieved from the shared preferences
        //check if the list has a bookmark
        if(ayaNumberBookmark != null && ayaTextBookmark != null && ayaArabicBookmark != null){
            //check if the bookmark is in the list by comparing the ayaNumberBookmark with the ayaNumber of the list
            //and the ayaTextBookmark with the ayaText of the list
            //and the ayaArabicBookmark with the ayaArabic of the list
            for(i in 0 until ayaForJuz.size){
                if(ayaNumberBookmark == ayaForJuz[i]?.ayaNumber && ayaTextBookmark == ayaForJuz[i]?.ayaEnglish && ayaArabicBookmark == ayaForJuz[i]?.ayaArabic){
                    //scroll to the position of the bookmark
                    ayaList.setSelection(i)
                    break
                }
            }
        }

        //on long click
        ayaList.setOnItemLongClickListener { parent, view, position, id ->
            //retrieve the aya object from the list
            val ayaObject = ayaForJuz[position]

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
                editor.putInt("surahNumberBookmark", number+1)
                editor.apply()
            }

            true
        }


        return root
    }


    //a function that saves the last position of the listview before the fragment is paused
    override fun onPause() {
        super.onPause()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = sharedPreferences.edit()
        val ayaList = requireView().findViewById<ListView>(R.id.ayaList)
        editor.putInt("lastPositionJuz", ayaList.firstVisiblePosition)
        editor.apply()
    }

    //a function that restores the last position of the listview before the fragment is created
    override fun onResume() {
        super.onResume()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val lastPosition = sharedPreferences.getInt("lastPositionJuz", 0)
        val ayaList = requireView().findViewById<ListView>(R.id.ayaList)
        ayaList.setSelection(lastPosition)
    }

}