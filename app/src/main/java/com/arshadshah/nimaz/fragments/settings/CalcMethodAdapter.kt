package com.arshadshah.nimaz.fragments.settings

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.RadioButton
import com.arshadshah.nimaz.R


internal class CalcMethodAdapter(
    var context: Context,
    private var arrayList: Array<String>
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

    override fun getView(position: Int, ConvertView: View?, parent: ViewGroup): View {
        var convertView: View? = ConvertView
        val methodAtPosition = arrayList[position]

        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.calc_method_row_item, null)
        }

        val calcRadioButton = convertView!!.findViewById<RadioButton>(R.id.calcMethodRadioButton)

        calcRadioButton.text = methodAtPosition

        return convertView
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getViewTypeCount(): Int {
        return arrayList.size
    }

    override fun isEmpty(): Boolean {
        return false
    }
}