package com.arshadshah.nimaz.helperClasses.quran

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.arshadshah.nimaz.fragments.quran.QuranJuzFragment
import com.arshadshah.nimaz.fragments.quran.QuranSurahFragment

class QuranFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> {
                QuranJuzFragment()
            }
            else -> {
                QuranSurahFragment()
            }
        }
    }
}