package com.arshadshah.nimaz.fragments.quran

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.database.DatabaseAccessHelper
import com.arshadshah.nimaz.helperClasses.quran.AyaListCustomAdapter
import com.arshadshah.nimaz.helperClasses.quran.AyaObject

class AyaListSurahFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_aya_surah_list, container, false)

        val helper = DatabaseAccessHelper(requireContext())
        helper.open()

        //get the juzNumber from bundle
        val number = requireArguments().getInt("number")

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val isEnglish = sharedPreferences.getBoolean("isEnglish", true)

        val ayaForsurah = helper.getAllAyaForSurah(number+1)

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
        //first check if an object like this is already in the list
        //check all the attributes of the object bisimillah with the attributes of the object in the list at index 0
        if(ayaForsurah[0]!!.ayaEnglish != bismillah.ayaEnglish && ayaForsurah[0]!!.ayaArabic != bismillah.ayaArabic || !isEnglish){
            if(number+1 != 9) {
                ayaForsurah.add(0, bismillah)
            }
        }

        val ayaList: ListView = root.findViewById(R.id.ayaListSurah)
        ayaList.divider = null
        
        //create a custom adapter
        val ayaListCustomAdapter = AyaListCustomAdapter(requireContext(), ayaForsurah)

        //set the adapter to the listview
        ayaList.adapter = ayaListCustomAdapter

        helper.close()
        return root
    }

}