package com.arshadshah.nimaz

import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.arshadshah.nimaz.fragments.Tasbeeh
import com.arshadshah.nimaz.helperClasses.TasbeehListCustomAdapter
import com.arshadshah.nimaz.helperClasses.TasbeehObject

class DikhrListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dikhr_list)
        supportActionBar?.hide()


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