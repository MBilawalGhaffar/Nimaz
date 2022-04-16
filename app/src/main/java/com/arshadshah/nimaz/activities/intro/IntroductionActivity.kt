package com.arshadshah.nimaz.activities.intro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arshadshah.nimaz.R

class IntroductionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        supportActionBar?.hide()

    }
}