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

internal class CustomAdapter(
    var context : Context ,
    private var arrayList : ArrayList<SubjectData>
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
        val subjectData = arrayList[position]

        if(convertView == null){
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.row_item , null)
        }
        val number = convertView?.findViewById<TextView>(R.id.number)
        val EnglishName = convertView?.findViewById<TextView>(R.id.EnglishName)
        val ArabicName = convertView?.findViewById<TextView>(R.id.ArabicName)
        val Translation = convertView?.findViewById<TextView>(R.id.Translation)

        number?.text = subjectData.number
        EnglishName?.text = subjectData.englishName
        ArabicName?.text = ArabicUtilities.reshape(subjectData.arabicName)
        Translation?.text = subjectData.translation
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