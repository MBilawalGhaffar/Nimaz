package com.arshadshah.nimaz

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.arshadshah.nimaz.helperClasses.DatabaseHelper
import com.arshadshah.nimaz.helperClasses.QuranFragmentAdapter
import com.google.android.material.tabs.TabLayout

class QuranActivity : AppCompatActivity() {

    private lateinit var quranTabLayout: TabLayout
    private lateinit var quranViewPager: ViewPager2
    private lateinit var quranFragmentAdapter:FragmentStateAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quran)
        supportActionBar?.hide()

        quranTabLayout = findViewById(R.id.quranTabLayout)
        quranViewPager = findViewById(R.id.quranViewPager)

        val fragmentManager = supportFragmentManager
        quranFragmentAdapter = QuranFragmentAdapter(fragmentManager,lifecycle)
        quranViewPager.adapter = quranFragmentAdapter


        val tab1 = quranTabLayout.newTab().setText(R.string.juz)
        val tab2 = quranTabLayout.newTab().setText(R.string.surah)
        quranTabLayout.addTab(tab1)
        quranTabLayout.addTab(tab2)

        quranTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                quranViewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        quranViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int){
                quranTabLayout.selectTab(quranTabLayout.getTabAt(position))
            }
        })

        val backButton: ImageView = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }
    }
}