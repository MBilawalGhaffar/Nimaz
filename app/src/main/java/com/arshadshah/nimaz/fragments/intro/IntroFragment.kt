package com.arshadshah.nimaz.fragments.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.arshadshah.nimaz.R

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_intro, container, false)

        //buttons
        val introContinue: Button = root.findViewById(R.id.introContinue)

        introContinue.setOnClickListener {
            val navcontroller = requireActivity().findNavController(R.id.fragmentContainerView)
            navcontroller.navigate(R.id.introTandCFragment)
        }

        return root
    }
}