package com.arshadshah.nimaz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.fragments.quran.AyaListJuzFragment
import com.arshadshah.nimaz.fragments.quran.AyaListSurahFragment

class QuranMainList : AppCompatActivity() {

    var nameOfPage: TextView? = null
    var numberOfPage: TextView? = null
    var fragmentToUse = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quran_main_list)
        supportActionBar?.hide()

        val backButton: ImageView = findViewById(R.id.backButton2)

        backButton.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(this , R.anim.expand_in)
            backButton.startAnimation(expandIn)
            finish()
        }

        //get the values from the intent
        val intent = intent
        val number = intent.getIntExtra("number", 0)
        val name = intent.getStringExtra("name")
        fragmentToUse = intent.getStringExtra("fragment").toString()
        val moreButton: ImageButton = findViewById(R.id.moreButton)
        nameOfPage= findViewById(R.id.NameToChange)
        numberOfPage= findViewById(R.id.numberOfPage)

        moreButton.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(this , R.anim.expand_in)
            moreButton.startAnimation(expandIn)


            //open the menu called quran_menu
            val menu = PopupMenu(this, moreButton)
            menu.inflate(R.menu.quran_menu)

            menu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.translation -> {
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
                                    fragmentSelecterForListDisplay(name!!,number)
                                }
                                else{
                                    with(sharedPreferences.edit()) {
                                        putBoolean("isEnglish" , false)
                                        apply()
                                    }
                                    fragmentSelecterForListDisplay(name!!,number)
                                }

                                alertDialog.cancel()
                            }

                            cancelbtn.setOnClickListener {
                                alertDialog.cancel()
                            }
                    }
                }
                true
            }
            menu.show()
        }

        fragmentSelecterForListDisplay(name!!,number)
    }


    private fun fragmentSelecterForListDisplay(name: String, number:Int){
        if(fragmentToUse == "juz"){
            val numberOfJuz = number+1
            nameOfPage!!.text = name
            numberOfPage!!.text = numberOfJuz.toString()
            val bundle = Bundle()
            bundle.putInt("number", number)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                    //add new fragment
                    add<AyaListJuzFragment>(R.id.fragmentContainerView3, args = bundle)
            }
        }
        else{
            val numberOfSurah = number+1
            nameOfPage!!.text = name
            numberOfPage!!.text = numberOfSurah.toString()
            val bundle = Bundle()
            bundle.putInt("number", number)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<AyaListSurahFragment>(R.id.fragmentContainerView3, args = bundle)
            }
        }
    }
}