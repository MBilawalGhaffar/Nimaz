package com.arshadshah.nimaz.helperClasses

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.arabicReshaper.ArabicUtilities
import java.util.*

internal class SurahListCustomAdapter(
    var context : Context ,
    private var arrayList : ArrayList<SurahObject>
                            ) :
    ListAdapter
{

    override fun areAllItemsEnabled() : Boolean
    {
        return false
    }

    override fun isEnabled(position : Int) : Boolean
    {
        return true
    }

    override fun registerDataSetObserver(observer : DataSetObserver)
    {
    }

    override fun unregisterDataSetObserver(observer : DataSetObserver)
    {
    }

    override fun getCount() : Int
    {
        return arrayList.size
    }

    override fun getItem(position : Int) : Any
    {
        return position
    }

    override fun getItemId(position : Int) : Long
    {
        return position.toLong()
    }

    override fun hasStableIds() : Boolean
    {
        return false
    }

    override fun getView(position : Int , ConvertView : View? , parent : ViewGroup) : View?
    {
        var convertView : View? = ConvertView
        val SurahObject = arrayList[position]

        if(convertView == null){
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.surah_row_item , null)
        }
        val surahNumber = convertView?.findViewById<TextView>(R.id.surahNumber)
        val SurahArabicName = convertView?.findViewById<TextView>(R.id.SurahArabicName)
        val SurahEnglishName = convertView?.findViewById<TextView>(R.id.SurahEnglishName)
        val SurahType = convertView?.findViewById<TextView>(R.id.SurahType)
        val SurahAyas = convertView?.findViewById<TextView>(R.id.SurahAyas)
        val SurahRukus = convertView?.findViewById<TextView>(R.id.SurahRukus)

        surahNumber?.text = SurahObject.surahNumber
        SurahArabicName?.text = ArabicUtilities.reshape(SurahObject.arabic)
        SurahEnglishName?.text = SurahObject.english
        SurahType?.text = SurahObject.type
        SurahAyas?.text = SurahObject.ayas
        SurahRukus?.text = SurahObject.rukus
        return convertView
    }

    override fun getItemViewType(position : Int) : Int
    {
        return position
    }

    override fun getViewTypeCount() : Int
    {
        return arrayList.size
    }

    override fun isEmpty() : Boolean
    {
        return false
    }
}