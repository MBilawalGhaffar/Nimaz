package com.arshadshah.nimaz.helperClasses.prayertimes

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.fragments.HomeFragment
import com.arshadshah.nimaz.helperClasses.utils.DateConvertor
import com.arshadshah.nimaz.prayerTimeApi.Prayer
import com.arshadshah.nimaz.prayerTimeApi.PrayerTimes

/**
 * class for timer creation
 * @author Arshad shah
 * */
class TimerCreater {

    fun getTimer(context: Context, endTime: Long, teller: TextView){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        if(sharedPreferences.getBoolean("ishaaTimeLonger", false)){
            teller.text = context.getString(R.string.maghrib)
        }
        else
        {
            createTimer(context,endTime, teller)
        }
    }

    /**
     * method to create timer using countdown timer funtion
     * @param context context of the activity
     * @param endTime end time of the timer
     * @param textView textview to display the timer
     */
    fun createTimer(context: Context, endTime: Long, textView: TextView) {
        // countdown timer for prayers
         var countDownTimer: CountDownTimer? = null
         // declare and initialize variables
         val start_time_in_milli = System.currentTimeMillis()
         val end_time_in_milli = endTime

         val difference = end_time_in_milli - start_time_in_milli

         // countdown timer object
         countDownTimer =
             object : CountDownTimer(difference, 1000) {
                 override fun onTick(millisUntilFinished: Long) {
                     var diff = millisUntilFinished
                     val secondsInMilli: Long = 1000
                     val minutesInMilli = secondsInMilli * 60
                     val hoursInMilli = minutesInMilli * 60

                     // elapsed hours
                     val elapsedHours = diff / hoursInMilli
                     diff %= hoursInMilli

                     // elapsed minutes
                     val elapsedMinutes = diff / minutesInMilli
                     diff %= minutesInMilli

                     // elapsed seconds
                     val elapsedSeconds = diff / secondsInMilli
                     diff %= secondsInMilli

                     val res: Resources = context.resources
                     val text = res.getString(R.string.timer)
                     val filledText =
                         String.format(text, elapsedHours, elapsedMinutes, elapsedSeconds)
                     textView.text = filledText
                 }

                 override fun onFinish() {
                    //if activity is not destroyed or paused
                    if (HomeFragment().activity != null && !HomeFragment().requireActivity().isFinishing && !HomeFragment().requireActivity().isDestroyed) {
                        //refresh fragment
                        val fragment = HomeFragment()
                        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                        fragmentTransaction.commit()
                    }

                     //cancel the countdown timer
                     countDownTimer?.cancel()

                     textView.isVisible = false
                 }
             }.start()
    }
}