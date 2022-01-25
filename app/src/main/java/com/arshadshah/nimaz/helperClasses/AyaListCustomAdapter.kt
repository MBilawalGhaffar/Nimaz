package com.arshadshah.nimaz.helperClasses

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import com.arshadshah.nimaz.R
import kotlin.collections.ArrayList

internal class AyaListCustomAdapter(
    var context: Context,
    private var arrayList: ArrayList<AyaObject?>
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
        val AyaObject = arrayList[position]

        if(convertView == null){
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.aya_list_row_item, null)
        }

        val EnglishName = convertView?.findViewById<TextView>(R.id.TranslationAya)
        val ArabicName = convertView?.findViewById<TextView>(R.id.ArabicAya)

        EnglishName?.text = AyaObject!!.ayaEnglish
        ArabicName?.text = AyaObject.ayaArabic
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