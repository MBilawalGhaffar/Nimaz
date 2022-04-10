package com.arshadshah.nimaz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class IntroductionActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        supportActionBar?.hide()
    }
}