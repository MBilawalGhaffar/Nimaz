package com.arshadshah.nimaz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.*
import com.arshadshah.nimaz.helperClasses.AyaListCustomAdapter

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

        val ayaNumberOfBismillah= "0"
        val ayaEnglishOfBismillah = "In the name of Allah, the Entirely Merciful, the Especially Merciful."
        val ayaArabicOfBismillah = "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ"
        val bismillah  = AyaObject(ayaNumberOfBismillah,ayaEnglishOfBismillah,ayaArabicOfBismillah)
        
        //find all the objects in arraylist ayaForJuz where ayaForJuz[i]!!.ayaNumber = 1
        //add object bismillah before it for every occurance of ayaForJuz[i]!!.ayaNumber = 1
        var index = 0
        while(index < ayaForJuz.size){
            if(ayaForJuz[index]!!.ayaEnglish != bismillah.ayaEnglish && ayaForJuz[index]!!.ayaArabic != bismillah.ayaArabic){
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

        //create a custom adapter
        val ayaListCustomAdapter = AyaListCustomAdapter(requireContext(), ayaForJuz)

        //set the adapter to the listview
        ayaList.adapter = ayaListCustomAdapter

        helper.close()
        return root
    }

}