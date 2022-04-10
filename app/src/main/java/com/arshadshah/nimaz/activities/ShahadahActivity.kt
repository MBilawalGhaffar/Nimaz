package com.arshadshah.nimaz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.arshadshah.nimaz.R

class ShahadahActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shahadah)
        supportActionBar?.hide()

        val backButton: ImageView = findViewById(R.id.backButton5)

        backButton.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(this , R.anim.expand_in)
            backButton.startAnimation(expandIn)
            //pop back stack to previous activity
            finish()
        }

    }
}