package com.arshadshah.nimaz.helperClasses.prayertimes

import android.content.Context
import android.content.res.Resources
import android.database.DataSetObserver
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.utils.DateConvertor
import com.arshadshah.nimaz.prayerTimeApi.PrayerTimes
import java.util.*

internal class PrayerTimesAdapter(
    var context: Context,
    private var arrayList: ArrayList<PrayerTimeObject?>,
    private var prayerTimes: PrayerTimes
) :
    ListAdapter {


    override fun areAllItemsEnabled(): Boolean {
        return false
    }

    override fun isEnabled(position: Int): Boolean {
        return true
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver) {
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getView(position: Int, ConvertView: View?, parent: ViewGroup): View? {
        var convertView: View? = ConvertView
        val PrayerTimeObject = arrayList[position]

        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.prayer_time_item, null)
        }

        val PrayerName = convertView?.findViewById<TextView>(R.id.PrayerName)
        val PrayerTime = convertView?.findViewById<TextView>(R.id.PrayerTime)
        val timer = convertView?.findViewById<TextView>(R.id.timer)

        PrayerName?.text = PrayerTimeObject?.prayerName

        //current time
        val currentTime = System.currentTimeMillis()

        //using DateConvertor().convertTimeToLong to convert time to long
        val prayerfajr = DateConvertor().convertTimeToLong(prayerTimes.fajr.toString())
        //sunrise
        val sunrise = DateConvertor().convertTimeToLong(prayerTimes.sunrise.toString())
        //dhuhr
        val dhuhr = DateConvertor().convertTimeToLong(prayerTimes.dhuhr.toString())
        //asr
        val asr = DateConvertor().convertTimeToLong(prayerTimes.asr.toString())
        //maghrib
        val maghrib = DateConvertor().convertTimeToLong(prayerTimes.maghrib.toString())
        //isha
        val isha = DateConvertor().convertTimeToLong(prayerTimes.isha.toString())

        val fajrTommorow = prayerfajr + 86400000


        val nextPrayerName = prayerTimes.nextPrayer()

        val nextPrayerTime = prayerTimes.timeForPrayer(nextPrayerName).toString()

        val nextPrayerTimeInLong = DateConvertor().convertTimeToLong(nextPrayerTime)

        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.cardBackground, typedValue, true)

        val defaultColor = context.getColor(typedValue.resourceId)

        theme.resolveAttribute(R.attr.highlight, typedValue, true)

        val highlight = context.getColor(typedValue.resourceId)

        if (currentTime in isha..fajrTommorow && position == 0) {

            convertView?.setBackgroundColor(highlight)
            timer?.isVisible = true
            TimerCreater().getTimer(context, fajrTommorow, timer!!)

        } else if (currentTime < prayerfajr && position == 0) {

            convertView?.setBackgroundColor(highlight)
            timer?.isVisible = true
            TimerCreater().getTimer(context, nextPrayerTimeInLong, timer!!)

        }else if (currentTime in prayerfajr..sunrise && position == 1) {

            //change the background color of position 0
            convertView?.setBackgroundColor(highlight)
            timer?.isVisible = true
            TimerCreater().getTimer(context, nextPrayerTimeInLong, timer!!)

        } else if (currentTime in sunrise..dhuhr && position == 2) {

            convertView?.setBackgroundColor(highlight)
            timer?.isVisible = true
            TimerCreater().getTimer(context, nextPrayerTimeInLong, timer!!)

        } else if (currentTime in dhuhr..asr && position == 3) {

            convertView?.setBackgroundColor(highlight)
            timer?.isVisible = true
            TimerCreater().getTimer(context, nextPrayerTimeInLong, timer!!)

        } else if (currentTime in asr..maghrib && position == 4) {

            convertView?.setBackgroundColor(highlight)
            timer?.isVisible = true
            TimerCreater().getTimer(context, nextPrayerTimeInLong, timer!!)

        } else if (currentTime in maghrib..isha && position == 5) {

            convertView?.setBackgroundColor(highlight)
            timer?.isVisible = true
            TimerCreater().getTimer(context, nextPrayerTimeInLong, timer!!)

        } else {
            convertView?.setBackgroundColor(defaultColor)
            timer?.isVisible = false
        }
        
        PrayerTime?.text = PrayerTimeObject?.prayerTime

        return convertView
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun isEmpty(): Boolean {
        return false
    }
}