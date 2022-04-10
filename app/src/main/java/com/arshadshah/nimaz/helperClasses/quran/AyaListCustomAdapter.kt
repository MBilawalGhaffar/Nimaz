package com.arshadshah.nimaz.helperClasses.quran

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.arabicReshaper.ArabicUtilities
import java.lang.String
import java.text.NumberFormat
import java.util.*
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.Long

internal class AyaListCustomAdapter(
    var context: Context,
    private var arrayList: ArrayList<AyaObject?>
                            ) :
    ListAdapter
{
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

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
        val AyaObject = arrayList[position]

        val isEnglish = sharedPreferences.getBoolean("isEnglish", true)

        if(convertView == null){
            val layoutInflater = LayoutInflater.from(context)
            if(isEnglish){
                convertView = layoutInflater.inflate(R.layout.aya_list_row_item, null)
            }
            else{
                convertView = layoutInflater.inflate(R.layout.aya_list_urdu_row_item, null)
            }
        }

        val EnglishName = convertView?.findViewById<TextView>(R.id.TranslationAya)
        val ArabicName = convertView?.findViewById<TextView>(R.id.ArabicAya)

        //take AyaObject!!.ayaNumber and append it at the end of AyaObject.ayaArabic inside an ayat end unicode
        val unicodeAyaEndEnd = "\uFD3E"
        val unicodeAyaEndStart = "\uFD3F"
        val nf: NumberFormat = NumberFormat.getInstance(Locale.forLanguageTag("AR"))
        val endOfAyaWithNumber= String.valueOf(nf.format(AyaObject!!.ayaNumber.toInt())).toString()

        val unicodeWithNumber = unicodeAyaEndStart + endOfAyaWithNumber + unicodeAyaEndEnd

        EnglishName?.text = AyaObject.ayaEnglish
        ArabicName?.text = ArabicUtilities.reshape(AyaObject.ayaArabic + " " + unicodeWithNumber)

        return convertView
    }

    override fun getItemViewType(position : Int) : Int
    {
        return position
    }

    override fun getViewTypeCount() : Int
    {
        return 1
    }

    override fun isEmpty() : Boolean
    {
        return false
    }
}