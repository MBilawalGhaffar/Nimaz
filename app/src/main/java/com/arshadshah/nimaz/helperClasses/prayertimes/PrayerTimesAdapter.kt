package com.arshadshah.nimaz.helperClasses.prayertimes

import android.content.Context
import android.database.DataSetObserver
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import com.arshadshah.nimaz.R

internal class PrayerTimesAdapter(
    var context: Context,
    private var arrayList: ArrayList<PrayerTimeObject?>,
    private var highlightPosition: Int
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

        PrayerName?.text = PrayerTimeObject?.prayerName

        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.cardBackground, typedValue, true)

        val defaultColor = context.getColor(typedValue.resourceId)

        theme.resolveAttribute(R.attr.highlight, typedValue, true)

        val highlight = context.getColor(typedValue.resourceId)

        if (highlightPosition == 5 && position == 5) {

            convertView?.setBackgroundColor(highlight)

        } else if (highlightPosition == 5 && position == 5) {

            convertView?.setBackgroundColor(highlight)

        } else if (highlightPosition == 0 && position == 0) {

            //change the background color of position 0
            convertView?.setBackgroundColor(highlight)

        } else if (highlightPosition == 1 && position == 1) {

            convertView?.setBackgroundColor(highlight)

        } else if (highlightPosition == 2 && position == 2) {

            convertView?.setBackgroundColor(highlight)

        } else if (highlightPosition == 3 && position == 3) {

            convertView?.setBackgroundColor(highlight)

        } else if (highlightPosition == 4 && position == 4) {

            convertView?.setBackgroundColor(highlight)

        } else {
            convertView?.setBackgroundColor(defaultColor)
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