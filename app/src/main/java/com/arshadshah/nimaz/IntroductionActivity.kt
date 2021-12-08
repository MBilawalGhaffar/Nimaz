package com.arshadshah.nimaz

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class IntroductionActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        supportActionBar?.hide()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        //buttons
        val enterLocation : Button = findViewById(R.id.locationYes)
        val goToHome : Button = findViewById(R.id.gotoHome)

        val IntroductoryText : TextView = findViewById(R.id.IntroductoryText)


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
                                   "        <b>Nimaz</b> Doesn't collect any Personal data,\n" +
                                   "        <br />\n" +
                                   "        Everything that you input is stored on your device alone." +
                                   "           <br/>For more information go to the privacy policy in settings" +
                                   "       <br/> If you need help in using Nimaz, checkout the help section in Settings\n" +
                                   "    </p>\n" +
                                   "<br/>\n" +
                                   "    <p>\n" +
                                   "        <b>Nimaz</b> needs the name of your city to get prayer times.\n" +
                                   "        <br />\n" +
                                   "        Press <b>SKIP</b> to enter later.\n" +
                                   "        <br />\n" +
                                   "        Press <b>CONTINUE</b> to Enter now.\n" +
                                   "    </p>"

        IntroductoryText.text = Html.fromHtml(IntroductoryTextHtml , Html.FROM_HTML_MODE_COMPACT)






        enterLocation.setOnClickListener {
            with(sharedPreferences.edit()) {
                putBoolean("isFirstInstall" , false)
                putBoolean("navigateToHome", true)
                putBoolean("channelLock" , false)
                apply()
            }
            val intent = Intent(this , SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }

        goToHome.setOnClickListener {
            with(sharedPreferences.edit()) {
                putBoolean("isFirstInstall" , false)
                putBoolean("channelLock" , false)
                apply()
            }
            val intent = Intent(this , HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}