package com.arshadshah.nimaz

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.helperClasses.NetworkChecker
import com.arshadshah.nimaz.helperClasses.locationFinder
import com.arshadshah.nimaz.prayerTimeApi.*
import com.arshadshah.nimaz.prayerTimeApi.data.DateComponents
import java.text.DateFormat
import java.util.*
import kotlin.properties.Delegates

/**
 * Widget that displays the Prayer times in a grid layout Updates every 24 hours
 * @author Arshad Shah
 */    // countdown timer for prayers

private lateinit var countDownTimer : CountDownTimer

class Nimaz : AppWidgetProvider()
{

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(
        context : Context ,
        appWidgetManager : AppWidgetManager ,
        appWidgetIds : IntArray
                         )
    {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds)
        {
            updateAppWidget(context , appWidgetManager , appWidgetId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onEnabled(context : Context)
    {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context : Context)
    {
        // Enter relevant functionality for when the last widget is disabled
    }

}

@RequiresApi(Build.VERSION_CODES.O)
internal fun updateAppWidget(
    context : Context ,
    appWidgetManager : AppWidgetManager ,
    appWidgetId : Int
                            )
{
    // Construct the RemoteViews object
    val intent = Intent(context , MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context , 9 , intent , 0)


    var latitude by Delegates.notNull<Double>()
    var longitude by Delegates.notNull<Double>()


    val views = RemoteViews(context.packageName , R.layout.nimaz)
    views.setOnClickPendingIntent(R.id.widget , pendingIntent)

    // put code for main here

    // Retrieve values given in the settings activity
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val name = sharedPreferences.getString("location_input" , "Portlaoise")
    val calcMethod = sharedPreferences.getString("calcMethod" , "IRELAND")

    // madhab adjustments
    val madhab = sharedPreferences.getBoolean("madhab" , true)

    // angle input from settings
    val fajr_angle = sharedPreferences.getString("fajrAngle" , "14.0")
    val ishaa_angle = sharedPreferences.getString("ishaaAngle" , "13.0")

    // time adjustments
    val fajr_adjust = sharedPreferences.getString("fajr" , "0")
    val sunrise_adjust = sharedPreferences.getString("sunrise" , "0")
    val zuhar_adjust = sharedPreferences.getString("zuhar" , "0")
    val asar_adjust = sharedPreferences.getString("asar" , "0")
    val maghrib_adjust = sharedPreferences.getString("maghrib" , "0")
    val ishaa_adjust = sharedPreferences.getString("ishaa" , "0")


    val highlatRule = sharedPreferences.getString("highlatrule" , "TWILIGHT_ANGLE")


    val isNetworkAvailable = NetworkChecker().networkCheck(context)
    if (isNetworkAvailable)
    {
        //location finder class
        val lonAndLat = locationFinder()
        lonAndLat.findLongAndLan(context , name !!)
        latitude = sharedPreferences.getString("latitude" , "0.0") !!.toDouble()
        longitude = sharedPreferences.getString("longitude" , "0.0") !!.toDouble()
    }
    else
    {
        with(sharedPreferences.edit()) {
            putString("location_input" , "No Network")
            apply()
        }
        latitude = sharedPreferences.getString("latitude" , "0.0") !!.toDouble()
        longitude = sharedPreferences.getString("longitude" , "0.0") !!.toDouble()
    }

    val coordinates = Coordinates(latitude , longitude)
    val calcdate = DateComponents.from(Date())

    val parameters = CalculationMethod.valueOf(calcMethod !!).parameters
    if (madhab)
    {
        parameters.madhab = Madhab.SHAFI
    }
    else if (! madhab)
    {
        parameters.madhab = Madhab.HANAFI
    }
    parameters.fajrAngle = fajr_angle !!.toDouble()
    parameters.ishaAngle = ishaa_angle !!.toDouble()

    parameters.adjustments.fajr = fajr_adjust !!.toInt()
    parameters.adjustments.sunrise = sunrise_adjust !!.toInt()
    parameters.adjustments.dhuhr = zuhar_adjust !!.toInt()
    parameters.adjustments.asr = asar_adjust !!.toInt()
    parameters.adjustments.maghrib = maghrib_adjust !!.toInt()
    parameters.adjustments.isha = ishaa_adjust !!.toInt()

    parameters.highLatitudeRule = HighLatitudeRule.valueOf(highlatRule !!)

    val formatter = DateFormat.getTimeInstance((DateFormat.SHORT))
    val prayerTimes = PrayerTimes(coordinates , calcdate , parameters)

    // show data
    // prayer times in string format used to start a timer to the next prayer
    val fajr_value = prayerTimes.fajr.toString()
    val zuhar_value = prayerTimes.dhuhr.toString()
    val asar_value = prayerTimes.asr.toString()
    val maghrib_value = prayerTimes.maghrib.toString()
    val ishaa_value = prayerTimes.isha.toString()

    // print times
    views.setTextViewText(R.id.Fajr_time , formatter.format(fajr_value))
    views.setTextViewText(R.id.Zuhar_time , formatter.format(zuhar_value))
    views.setTextViewText(R.id.Asar_time , formatter.format(asar_value))
    views.setTextViewText(R.id.Maghrib_time , formatter.format(maghrib_value))
    views.setTextViewText(R.id.Ishaa_time , formatter.format(ishaa_value))

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId , views)
}

