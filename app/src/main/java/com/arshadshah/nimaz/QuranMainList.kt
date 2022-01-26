package com.arshadshah.nimaz

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.fragments.AyaListJuzFragment
import com.arshadshah.nimaz.fragments.AyaListSurahFragment

class QuranMainList : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quran_main_list)
        supportActionBar?.hide()

        val backButton: ImageView = findViewById(R.id.backButton2)

        backButton.setOnClickListener {
            finish()
        }

        //get the values from the intent
        val intent = intent
        val number = intent.getIntExtra("number", 0)
        val fragmentToUse = intent.getStringExtra("fragment")
        val moreButton: ImageButton = findViewById(R.id.moreButton)

        moreButton.setOnClickListener {
            // Create the object of
            // AlertDialog Builder class
            val builder : AlertDialog.Builder = AlertDialog.Builder(this)
            val inflater : LayoutInflater = layoutInflater
            val moreDialog = inflater.inflate(R.layout.moredialog , null)
            val englishTranslation : RadioButton = moreDialog.findViewById(R.id.englishTranslation)
            val urduTranslation : RadioButton = moreDialog.findViewById(R.id.urduTranslation)
            val submitBtn : Button = moreDialog.findViewById(R.id.dialogSubmit)
            val cancelbtn : Button = moreDialog.findViewById(R.id.dialogCancel)

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val isEnglish = sharedPreferences.getBoolean("isEnglish", true)
            builder.setView(moreDialog)

            // Set Cancelable false
            // for when the user clicks on the outside
            // the Dialog Box then it will remain show
            builder.setCancelable(false)
            // Create the Alert dialog
            val alertDialog : AlertDialog = builder.create()
            // Show the Alert Dialog box
            alertDialog.show()

            var translationSelected = ""
            if(isEnglish){
                englishTranslation.isChecked = true
            }
            else{
                urduTranslation.isChecked = true
            }

            englishTranslation.setOnClickListener {
                translationSelected = "english"
            }

            urduTranslation.setOnClickListener {
                translationSelected = "urdu"
            }

            submitBtn.setOnClickListener {
                if(translationSelected == "english")
                {
                    with(sharedPreferences.edit()) {
                        putBoolean("isEnglish" , true)
                        apply()
                    }
                    if(fragmentToUse == "juz"){
                        val bundle = Bundle()
                        bundle.putInt("number", number)
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            add<AyaListJuzFragment>(R.id.fragmentContainerView3, args = bundle)
                        }
                    }
                    else{
                        val bundle = Bundle()
                        bundle.putInt("number", number)
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            add<AyaListSurahFragment>(R.id.fragmentContainerView3, args = bundle)
                        }
                    }
                }
                else{
                    with(sharedPreferences.edit()) {
                        putBoolean("isEnglish" , false)
                        apply()
                    }
                    if(fragmentToUse == "juz"){
                        val bundle = Bundle()
                        bundle.putInt("number", number)
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            add<AyaListJuzFragment>(R.id.fragmentContainerView3, args = bundle)
                        }
                    }
                    else{
                        val bundle = Bundle()
                        bundle.putInt("number", number)
                        supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            add<AyaListSurahFragment>(R.id.fragmentContainerView3, args = bundle)
                        }
                    }
                }

                alertDialog.cancel()
            }

            cancelbtn.setOnClickListener {
                alertDialog.cancel()
            }
        }

        if(fragmentToUse == "juz"){
            val bundle = Bundle()
            bundle.putInt("number", number)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<AyaListJuzFragment>(R.id.fragmentContainerView3, args = bundle)
            }
        }
        else{
            val bundle = Bundle()
            bundle.putInt("number", number)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<AyaListSurahFragment>(R.id.fragmentContainerView3, args = bundle)
            }
        }
    }

}