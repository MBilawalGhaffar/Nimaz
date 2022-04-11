package com.arshadshah.nimaz.fragments.more

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.activities.more.HelpActivity
import com.arshadshah.nimaz.activities.more.NamesOfAllahActivity
import com.arshadshah.nimaz.activities.more.ShahadahActivity
import com.arshadshah.nimaz.activities.tasbeeh.TasbeehActivity

class MoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_more, container, false)

//        buttons
        val tasbeeh : ImageView = root.findViewById(R.id.Tasbeeh)
        val namesOfAllah : ImageView = root.findViewById(R.id.NamesOfAllah)
        val shahadah : ImageView = root.findViewById(R.id.Shahadah)
        val help : ImageView = root.findViewById(R.id.help)


        startActivityUsingIntent(requireContext(), tasbeeh, TasbeehActivity())

        startActivityUsingIntent(requireContext(), namesOfAllah, NamesOfAllahActivity())

        startActivityUsingIntent(requireContext(), shahadah, ShahadahActivity())

        startActivityUsingIntent(requireContext(), help, HelpActivity())


        return root
    }

    fun startActivityUsingIntent(context: Context,nameOfButton: ImageView,  ActivityToStart: Activity){
        nameOfButton.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(context , R.anim.expand_in)
            nameOfButton.startAnimation(expandIn)

            //navigate to activity
            val activityIntent = Intent(context, ActivityToStart::class.java)
            startActivity(activityIntent)
        }
    }
}