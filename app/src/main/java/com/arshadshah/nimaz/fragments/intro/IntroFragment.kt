package com.arshadshah.nimaz.fragments.intro

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.HomeActivity
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.SettingsActivity

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_intro, container, false)

        //buttons
        val introContinue : Button = root.findViewById(R.id.introContinue)

        val IntroductoryText : TextView = root.findViewById(R.id.IntroductoryText)


        val IntroductoryTextHtml = "<b>In the name of Allah, Most Gracious,Most Merciful</b>\n" +
                "    <br />" +
                "    <b>As-salamu alaykum</b>\n" +
                "    <p>\n" +
                "        <b>Nimaz</b> is created with the help of <b>Allah</b> for the Sake of <b>Allah</b> and his Prophet <b>Muhammad (S.A.W)</b>.\n" +
                "        <br />\n" +
                "        Please keep me and my family in your dua.\n" +
                "    </p>\n" +
                "<br/>\n" +
                "    <p>\n" +
                "        <b>Nimaz</b> Does not collect any Personal data,\n" +
                "        <br />\n" +
                "        Everything that you input is stored on your device alone." +
                "           <br/>For more information go to the privacy policy in settings" +
                "       <br/> If you need help in using Nimaz, checkout the help section in Settings\n" +
                "    </p>\n" +
                "<br/>\n" +
                "    <p>\n" +
                "        <b>Nimaz</b> needs the name of your city to get prayer times.\n" +
                "        <br />\n" +
                "        Press <b>CONTINUE</b> to Enter now.\n" +
                "    </p>"

        IntroductoryText.text = Html.fromHtml(IntroductoryTextHtml , Html.FROM_HTML_MODE_COMPACT)

        introContinue.setOnClickListener {
            val navcontroller = requireActivity().findNavController(R.id.fragmentContainerView)
            navcontroller.navigate(R.id.introTandCFragment)
        }

        return root
    }
}