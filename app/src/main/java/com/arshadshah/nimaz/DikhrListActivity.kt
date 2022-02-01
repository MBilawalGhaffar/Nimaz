package com.arshadshah.nimaz

import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.helperClasses.tasbeeh.TasbeehListCustomAdapter
import com.arshadshah.nimaz.helperClasses.tasbeeh.TasbeehObject

class DikhrListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dikhr_list)
        supportActionBar?.hide()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)


        val backButton: ImageView = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        this.onBackPressedDispatcher.addCallback(this) {
            finish()
        }



        val list : ListView = findViewById(R.id.DhikrList)
        val arrayList : ArrayList<TasbeehObject> = ArrayList()
        val array = resources.getStringArray(R.array.tasbeehTransliteration)
        var indexNo : Int
        for (item in array)
        {
            indexNo = array.indexOf(item)
            arrayList.add(
                TasbeehObject(
                    english(indexNo) ,
                    arabic(indexNo) ,
                    translation(indexNo) ,
                )
            )
        }
        val TasbeehListCustomAdapter = TasbeehListCustomAdapter(this , arrayList)
        list.adapter = TasbeehListCustomAdapter

        list.setOnItemClickListener { parent, view, position, id ->
            with(sharedPreferences.edit()) {
                putString("tasbeehEnglish" , arrayList[position].english)
                putString("tasbeehArabic" , arrayList[position].arabic)
                putString("tasbeehTranslation" , arrayList[position].translation)
                apply()
            }
            finish()
        }
    }

    private fun english(indexNo : Int) : String
    {
        val array = resources.getStringArray(R.array.tasbeehTransliteration)
        val output = array[indexNo]
        return output
    }

    private fun arabic(indexNo : Int) : String
    {
        val array = resources.getStringArray(R.array.tasbeeharabic)
        val output = array[indexNo]
        return output
    }

    private fun translation(indexNo : Int) : String
    {
        val array = resources.getStringArray(R.array.tasbeehTranslation)
        val output = array[indexNo]
        return output
    }
}