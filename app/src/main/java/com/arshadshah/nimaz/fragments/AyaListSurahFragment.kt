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

        val ayaForsurah = helper.getAllAyaForSurah(number+1)

        val ayaList: ListView = root.findViewById(R.id.ayaListSurah)
        
        //create a custom adapter
        val ayaListCustomAdapter = AyaListCustomAdapter(requireContext(), ayaForsurah)

        //set the adapter to the listview
        ayaList.adapter = ayaListCustomAdapter

        helper.close()
        return root
    }

}