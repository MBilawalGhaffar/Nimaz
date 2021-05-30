package com.arshadshah.nimaz.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.arshadshah.nimaz.BuildConfig
import com.arshadshah.nimaz.R

/**
 * The Fragment where the Information about
 * Author,
 * Application Version,
 * References to Other Author Media
 * is displayed.
 * @author Arshad Shah
 * */
class About : Fragment()
{

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
                             ) : View?
    {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_about , container , false)

        val version : TextView = root.findViewById(R.id.version)
        val versionCode = BuildConfig.VERSION_NAME

        version.text = versionCode

        return root
    }
}