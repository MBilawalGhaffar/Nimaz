package com.arshadshah.nimaz.helperClasses.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * A conversion class for Long to Date and date To long values for a Given date
 * @author Arshad Shah
 */
class DateConvertor
{

    /**
     * Converts a String Pattern ("E MMM dd HH:mm:ss z yyyy") Date into a value in milliseconds
     * @param date The String Date
     * @return The value in milliseconds
     */
    fun convertTimeToLong(date : String) : Long
    {
        val localeValue : Locale = Locale.getDefault()
        val sdf = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy" , localeValue)
        val time : Date = sdf.parse(date) !!
        return time.time
    }

    /**
     * Converts a value in milliseconds into a date component String of pattern ("E MMM dd HH:mm:ss
     * z yyyy")
     * @param time The long value in milliseconds
     * @return A Date Component in String
     */
    fun convertLongToTime(time : Long) : String
    {
        val localeValue : Locale = Locale.getDefault()

        val date = Date(time)
        val format = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy" , localeValue)

        return format.format(date)
    }
}
