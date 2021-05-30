package com.arshadshah.nimaz.helperClasses

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R

/**
 * class for timer creation
 * @author Arshad shah
 * */
class TimerCreater
{

    // countdown timer for prayers
    private lateinit var countDownTimer : CountDownTimer

    /**
     * Creates a timer for given time
     * and takes care of highlighting
     * and removing hightlight
     *
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    fun createTimer(
        context : Context ,
        fajr : String ,
        sunrise : String ,
        Zuhar : String ,
        asar : String ,
        maghrib : String ,
        ishaa : String ,
        Tofajr : TextView ,
        fajrhighlight : ConstraintLayout ,
        Tosunrise : TextView ,
        sunrisehighlight : ConstraintLayout ,
        ToZuhar : TextView ,
        zuharhighlight : ConstraintLayout ,
        ToAsar : TextView ,
        asarhighlight : ConstraintLayout ,
        ToMaghrib : TextView ,
        maghribhighlight : ConstraintLayout ,
        ToIshaa : TextView ,
        ishaahighlight : ConstraintLayout
                   )
    {

        //shared preferences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val current_time = System.currentTimeMillis()

        val fajr_value_Long = DateConvertor().convertTimeToLong(fajr)
        val Sunrise_value_Long = DateConvertor().convertTimeToLong(sunrise)
        val zuhar_value_Long = DateConvertor().convertTimeToLong(Zuhar)
        val asar_value_Long = DateConvertor().convertTimeToLong(asar)
        val maghrib_value_Long = DateConvertor().convertTimeToLong(maghrib)
        val ishaa_value_Long = DateConvertor().convertTimeToLong(ishaa)


        val fajr_value_current = DateConvertor().convertLongToTime(fajr_value_Long)
        val fajr_tommorow = fajr_value_Long + 86400000
        val fajr_tommorrow_string = DateConvertor().convertLongToTime(fajr_tommorow)
        /**
         * Main logic for the countdown Timer
         * it first checks what timer needs to start
         * than makes that timer TextView visible
         * highlights the area of the prayer time
         * starts countdown
         * at finish of the timer it removes highlight
         * refreshes the fragment to ensure that the code is run again
         * makes the TextView of timer invisible
         * Makes the next area highlighted
         * */

        fajrhighlight.setBackgroundResource(0)
        sunrisehighlight.setBackgroundResource(0)
        zuharhighlight.setBackgroundResource(0)
        asarhighlight.setBackgroundResource(0)
        maghribhighlight.setBackgroundResource(0)
        ishaahighlight.setBackgroundResource(0)

        if (current_time in (ishaa_value_Long + 1) until fajr_tommorow)
        {
            Tofajr.isVisible = true
            fajrhighlight.setBackgroundResource(R.drawable.prayertimehighlight)
            Log.i("Nimaz Timer" , "Timer Started")
            Count_DownTimer(context , fajr_tommorrow_string , Tofajr)
        }
        else
        {
            if (current_time < fajr_value_Long)
            {
                Tofajr.isVisible = true
                fajrhighlight.setBackgroundResource(R.drawable.prayertimehighlight)
                Log.i("Nimaz Timer" , "Timer Started")
                Count_DownTimer(context , fajr_value_current , Tofajr)
            }
            else
            {
                fajrhighlight.setBackgroundResource(R.drawable.linesinprayerbottom)
                Tofajr.isVisible = false
                Tosunrise.isVisible = true
            }
        }

        // sunrise
        if (current_time in (fajr_value_Long + 1) until Sunrise_value_Long)
        {
            fajrhighlight.setBackgroundResource(0)
            Tofajr.isVisible = false
            Tosunrise.isVisible = true
            sunrisehighlight.setBackgroundResource(R.drawable.prayertimehighlight)
            Log.i("Nimaz Timer" , "Timer Started")
            Count_DownTimer(context , sunrise , Tosunrise)
        }
        else
        {

            sunrisehighlight.setBackgroundResource(R.drawable.linesinprayerbottom)
            Tosunrise.isVisible = false
            ToZuhar.isVisible = true
        }
        // zuhar
        if (current_time in (Sunrise_value_Long + 1) until zuhar_value_Long)
        {
            fajrhighlight.setBackgroundResource(R.drawable.linesinprayerbottom)
            zuharhighlight.setBackgroundResource(R.drawable.prayertimehighlight)
            Log.i("Nimaz Timer" , "Timer Started")
            Count_DownTimer(context , Zuhar , ToZuhar)
        }
        else
        {

            zuharhighlight.setBackgroundResource(R.drawable.linesinprayerbottom)
            ToZuhar.isVisible = false
            ToAsar.isVisible = true
        }
        // asar
        if (current_time in (zuhar_value_Long + 1) until asar_value_Long)
        {
            fajrhighlight.setBackgroundResource(R.drawable.linesinprayerbottom)
            asarhighlight.setBackgroundResource(R.drawable.prayertimehighlight)
            Log.i("Nimaz Timer" , "Timer Started")
            Count_DownTimer(context , asar , ToAsar)

        }
        else
        {

            asarhighlight.setBackgroundResource(R.drawable.linesinprayerbottom)
            ToAsar.isVisible = false
            ToMaghrib.isVisible = true
        }

        // maghrib
        if (current_time in (asar_value_Long + 1) until maghrib_value_Long)
        {
            fajrhighlight.setBackgroundResource(R.drawable.linesinprayerbottom)
            maghribhighlight.setBackgroundResource(R.drawable.prayertimehighlight)
            Log.i("Nimaz Timer" , "Timer Started")
            Count_DownTimer(context , maghrib , ToMaghrib)
        }
        else
        {

            maghribhighlight.setBackgroundResource(R.drawable.linesinprayerbottom)
            ToMaghrib.isVisible = false
            ToIshaa.isVisible = true
        }

        val ishaaTimeLonger = sharedPreferences.getBoolean("ishaaTimeLonger" , false)
        if (! ishaaTimeLonger)
        {
            // ishaa
            if (current_time in (maghrib_value_Long + 1) until ishaa_value_Long)
            {
                fajrhighlight.setBackgroundResource(R.drawable.linesinprayerbottom)
                ishaahighlight.setBackgroundResource(R.drawable.prayertimehighlight)
                Log.i("Nimaz Timer" , "Timer Started")
                Count_DownTimer(context , ishaa , ToIshaa)
            }
            else
            {
                ishaahighlight.setBackgroundResource(0)
                ToIshaa.isVisible = false
            }

        }
        else
        {
            val MaghribPlusBuffer = maghrib_value_Long + 1800000
            val maghribPlusBufferString = DateConvertor().convertLongToTime(MaghribPlusBuffer)

            if (current_time > MaghribPlusBuffer && current_time < fajr_tommorow)
            {
                Tofajr.isVisible = true
                fajrhighlight.setBackgroundResource(R.drawable.prayertimehighlight)
                Log.i("Nimaz Timer" , "Timer Started")
                Count_DownTimer(context , fajr_tommorrow_string , Tofajr)
                ishaahighlight.setBackgroundResource(0)
                ToIshaa.isVisible = true
                ToIshaa.text = "After"

            }
            else
            {
                if (current_time < MaghribPlusBuffer && current_time > maghrib_value_Long)
                {
                    ishaahighlight.setBackgroundResource(R.drawable.prayertimehighlight)
                    Count_DownTimer2(context , maghribPlusBufferString)
                    ToIshaa.isVisible = true
                    ToIshaa.text = "After"
                }
                else
                {
                    ishaahighlight.setBackgroundResource(0)
                    ToIshaa.isVisible = true
                    ToIshaa.text = "After"
                }
            }

        }
        // end of coundown timer


    }

    /**
     * It creates a Count Down timer to a time in the future
     *  that is in a form of a date component
     *  Example ("E MMM dd HH:mm:ss z yyyy")
     * @author Arshad shah
     * @param endTime The future time
     * @param timeTeller The TextView to show timer in
     * @return A timer to a TextView
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    fun Count_DownTimer(context : Context , endTime : String , timeTeller : TextView)
    {

        // declare and initialize variables
        val start_time_in_milli = System.currentTimeMillis()
        val end_time_in_milli = DateConvertor().convertTimeToLong(endTime)

        val difference = end_time_in_milli - start_time_in_milli

        // countdown timer object
        countDownTimer =
            object : CountDownTimer(difference , 1000)
            {
                override fun onTick(millisUntilFinished : Long)
                {
                    var diff = millisUntilFinished
                    val secondsInMilli : Long = 1000
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

                    timeTeller.text =
                        " - $elapsedHours : $elapsedMinutes : $elapsedSeconds "
                }

                override fun onFinish()
                {
                    timeTeller.isVisible = false
                    Log.i("Nimaz Timer" , "Timer ->{$timeTeller}<- finished")

                }
            }.start()
    } // end of countdown function


    /**
     * It creates a Count Down timer to a time in the future
     *  that is in a form of a date component
     *  Example ("E MMM dd HH:mm:ss z yyyy")
     * @author Arshad shah
     * @param endTime The future time
     * @param timeTeller The TextView to show timer in
     * @return A timer to a TextView
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    fun Count_DownTimer2(context : Context , endTime : String)
    {

        // declare and initialize variables
        val start_time_in_milli = System.currentTimeMillis()
        val end_time_in_milli = DateConvertor().convertTimeToLong(endTime)

        val difference = end_time_in_milli - start_time_in_milli

        // countdown timer object
        countDownTimer =
            object : CountDownTimer(difference , 1000)
            {
                override fun onTick(millisUntilFinished : Long)
                {
                    var diff = millisUntilFinished
                    val secondsInMilli : Long = 1000
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

                }

                override fun onFinish()
                {
                }
            }.start()
    } // end of countdown function

}