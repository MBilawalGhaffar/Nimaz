package com.arshadshah.nimaz.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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

//        website button
        val website: ImageButton = root.findViewById(R.id.website)

        website.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://arshadshah.com")))
        }

        //        website button
        val linkedin: ImageButton = root.findViewById(R.id.linkedin)

        linkedin.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/arshadshah")))
        }

        //        website button
        val mail: ImageButton = root.findViewById(R.id.mail)

        mail.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("mailto: info@arshadshah.com")))
        }

        //        website button
        val github: ImageButton = root.findViewById(R.id.github)

        github.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/arshad-shah")))
        }


        //        website button
        val ref1: ImageButton = root.findViewById(R.id.ref1)

        ref1.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/batoulapps/adhan-java")))
        }
        //        website button
        val ref2: ImageButton = root.findViewById(R.id.ref2)

        ref2.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/authors/dinosoftlabs")))
        }
        //        website button
        val ref3: ImageButton = root.findViewById(R.id.ref3)

        ref3.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.freepik.com/")))
        }

        //        website button
        val ref: ImageButton = root.findViewById(R.id.ref)

        ref.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://tanzil.net/updates/")))
        }
        return root
    }
}