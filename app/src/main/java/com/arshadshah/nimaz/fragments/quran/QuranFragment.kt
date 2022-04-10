package com.arshadshah.nimaz.fragments.quran

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.activities.HomeActivity
import com.arshadshah.nimaz.helperClasses.quran.QuranFragmentAdapter
import com.google.android.material.tabs.TabLayout

class QuranFragment : Fragment() {

    private lateinit var quranTabLayout: TabLayout
    private lateinit var quranViewPager: ViewPager2
    private lateinit var quranFragmentAdapter: FragmentStateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_quran, container, false)
        quranTabLayout = root.findViewById(R.id.quranTabLayout)
        quranViewPager = root.findViewById(R.id.quranViewPager)

        val fragmentManager = childFragmentManager
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
        return root
    }
}