package com.arshadshah.nimaz.activities.quran

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.fragments.quran.AyaListJuzFragment
import com.arshadshah.nimaz.fragments.quran.AyaListSurahFragment
import com.arshadshah.nimaz.fragments.quran.QuranSearchFragment
import com.arshadshah.nimaz.helperClasses.database.DatabaseAccessHelper

class QuranMainList : AppCompatActivity() {

    var nameOfPage: TextView? = null
    var numberOfPage: TextView? = null
    var fragmentToUse = ""
    var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quran_main_list)
        supportActionBar?.hide()

        val helper = DatabaseAccessHelper(this)
        helper.open()

        val backButton: ImageView = findViewById(R.id.backButton2)

        val keyword: TextView = findViewById(R.id.keyword)

        val keywordAmount: TextView= findViewById(R.id.keywordAmount)

        val searchFragmentTitle: ConstraintLayout= findViewById(R.id.searchFragmentTitle)

        backButton.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(this , R.anim.expand_in)
            backButton.startAnimation(expandIn)
            finish()
        }

        nameOfPage= findViewById(R.id.NameToChange)
        numberOfPage= findViewById(R.id.numberOfPage)

        val moreButton: ImageView = findViewById(R.id.moreButton2)

        moreButton.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(this , R.anim.expand_in)
            moreButton.startAnimation(expandIn)

            val number = intent.getIntExtra("number", 0)
            val name = intent.getStringExtra("name")

            launchMenu( this, name.toString(), number)
        }

        //get the values from the intent
        val intent = intent

        fragmentToUse = intent.getStringExtra("fragment").toString()
        if (fragmentToUse == "search"){
            query = intent.getStringExtra("query").toString()

            val numberOfAyas = helper.searchForAyaAmountFound(query, "en_sahih", "text")

            if(numberOfAyas != 0){
                val bundle = Bundle()
                bundle.putString("query", query)
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    //add new fragment
                    add<QuranSearchFragment>(R.id.fragmentContainerView3, args = bundle)
                }
                //get string from resources and add the number of ayas found and the query to it
                searchFragmentTitle.isVisible = true
                keyword.text = query
                keywordAmount.text = getString(R.string.times,numberOfAyas.toString())
                numberOfPage!!.isVisible = false
                nameOfPage?.isVisible = false
                helper.close()
            }else{
                nameOfPage!!.text = "Not Found"
                numberOfPage!!.isVisible = false
                searchFragmentTitle.isVisible = false

                //show a a custom dialog
                val builder = AlertDialog.Builder(this)

                //set the custom view
                val customView = LayoutInflater.from(this).inflate(R.layout.resultnotfounddialog, null)
                builder.setView(customView)

                //set the message
                val message = customView.findViewById<TextView>(R.id.message)
                message.text = "No Results Found for the Keyword: $query"

                val dialog: AlertDialog = builder.create()
                //set the button
                val button = customView.findViewById<Button>(R.id.retry)
                button.setOnClickListener {
                    finish()
                    dialog.dismiss()
                }

                dialog.show()

            }
        }
        else{
            numberOfPage!!.isVisible = true
            nameOfPage?.isVisible = true
            searchFragmentTitle.isVisible = false
            val number = intent.getIntExtra("number", 0)
            val name = intent.getStringExtra("name")
            fragmentSelecterForListDisplay(name!!,number)
            helper.close()
        }
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

    fun launchMenu(context: Context, name: String, number: Int){
            // Create the object of
            // AlertDialog Builder class
            val builder : AlertDialog.Builder = AlertDialog.Builder(context)
            //get layout inflater
            val inflater : LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val moreDialog = inflater.inflate(R.layout.moredialog, null)
            val englishTranslation : RadioButton = moreDialog.findViewById(R.id.englishTranslation)
            val urduTranslation : RadioButton = moreDialog.findViewById(R.id.urduTranslation)
            val submitBtn : Button = moreDialog.findViewById(R.id.dialogSubmit)
            val cancelbtn : Button = moreDialog.findViewById(R.id.dialogCancel)

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
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
                }
                else{
                    with(sharedPreferences.edit()) {
                        putBoolean("isEnglish" , false)
                        apply()
                    }
                }

                alertDialog.cancel()

                if (fragmentToUse == "search"){
                    val bundle = Bundle()
                    bundle.putString("query", query)
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        //add new fragment
                        add<QuranSearchFragment>(R.id.fragmentContainerView3, args = bundle)
                    }
                }
                else{
                    //swap the fragment using fragmentSelecterForListDisplay()
                    fragmentSelecterForListDisplay(name, number)
                }
            }

            cancelbtn.setOnClickListener {
                alertDialog.cancel()
            }

    }
}