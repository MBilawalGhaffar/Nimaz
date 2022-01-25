package com.arshadshah.nimaz.helperClasses

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.arshadshah.nimaz.fragments.QuranJuzFragment
import com.arshadshah.nimaz.fragments.QuranSurahFragment

class QuranFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> {
                QuranSurahFragment()
            }
            else -> {
                QuranJuzFragment()
            }
        }
    }
}