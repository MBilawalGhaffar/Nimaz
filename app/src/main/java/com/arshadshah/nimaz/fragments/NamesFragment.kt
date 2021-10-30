package com.arshadshah.nimaz.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.CustomAdapter
import com.arshadshah.nimaz.helperClasses.SubjectData


class NamesFragment : Fragment()
{

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
                             ) : View?
    {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_names , container , false)



        val list : ListView = root.findViewById(R.id.allahNames)
        val arrayList : ArrayList<SubjectData> = ArrayList()
        val array = resources.getStringArray(R.array.English)
        var indexNo : Int
        for (item in array)
        {
            indexNo = array.indexOf(item)
            arrayList.add(
                SubjectData(
                    englishName(indexNo) ,
                    arabicName(indexNo) ,
                    translation(indexNo) ,
                    (indexNo + 1).toString()
                           )
                         )
        }
        val customAdapter = CustomAdapter(requireContext() , arrayList)
        list.adapter = customAdapter

        return root
    }

    private fun englishName(indexNo : Int) : String
    {
        val array = resources.getStringArray(R.array.English)
        val output = array[indexNo]
        return output
    }

    private fun arabicName(indexNo : Int) : String
    {
        val array = resources.getStringArray(R.array.Arabic)
        val output = array[indexNo]
        return output
    }

    private fun translation(indexNo : Int) : String
    {
        val array = resources.getStringArray(R.array.translation)
        val output = array[indexNo]
        return output
    }
}