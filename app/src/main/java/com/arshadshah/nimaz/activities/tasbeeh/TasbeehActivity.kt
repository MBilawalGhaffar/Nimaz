package com.arshadshah.nimaz.activities.tasbeeh

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arshadshah.nimaz.R


class TasbeehActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasbeeh)
        supportActionBar?.hide()
    }

}