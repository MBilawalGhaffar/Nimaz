package com.arshadshah.nimaz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.add
import androidx.fragment.app.commit
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