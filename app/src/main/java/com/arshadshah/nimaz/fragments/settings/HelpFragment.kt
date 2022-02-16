package com.arshadshah.nimaz.fragments.settings

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.arshadshah.nimaz.R

class HelpFragment : Fragment()
{

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
                             ) : View?
    {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_help , container , false)
        val fragmentName = requireArguments().getString("fragmentName")

        //change the action bar title to fragmentName
        activity?.title = fragmentName

        val internet : TextView = root.findViewById(R.id.internet)

        val internettext = "      <p>\n<h1> Introduction </h1>" +
                           "<br/>" +
                           "<b>Nimaz</b> Uses mathematical formulae to find the Prayer times from the location of the sun using the current device time, timezone, and the location." +
                           "<br/> And therefore the values will be approximated. <br/> <b>Nimaz</b> has an error margin of approximately 5 minutes to the location(Provided) prayer times." +
                           "<br/> if the error in Prayer Times produced by <b>Nimaz</b> is greater than 5 minutes than the calculation adjustments are wrong and therefore a new calculation method," +
                           "<br/> or adjustments to the times is required." +
                           "<br/>" +
                           "<p><b>Nimaz</b> will not show notification or do Adhan if <b>Do Not Disturb</b> is set on this is to prevent it" +
                           "<br/> from creating a disturbance in critical circumstances." +
                           "<br/> However you can make it bypass <b>Do Not Disturb</b> in the device Notification settings." +
                           "<br/> that is accessible from the settings page of <b>Nimaz</b>. here you can change the defaults for each Adhan.</p>" +
                           "<br/> <b>Nimaz</b> is designed to work with the device clock. an the time zone of the device." +
                           "<br/>so for Example: " +
                           "<br/>if the device clock is 24 hours, and timezone is GMT+5" +
                           "<br/><b>Nimaz</b> will show all the time related data in 24 hours format. using Timezone GMT+5." +
                           "Therefore if you need to get Prayer times in other format or another time zone you need to change the settings inside the Device settings." +
                           "<p>" +
                           "<h1>Nimaz Setup Tip</h1>" +
                           "The best way to set up <b>Nimaz</b> is to get your local Mosque times for the day, make adjustments to <b>Nimaz</b> to the times of the Mosque and restart the alarms." +
                           "<br/> After this <b>Nimaz</b> will display the correct times for that mosque up to a margin of error of 5 minutes (overtime).</p>" +
                           "<br/> " +
                           "        <strong>\n" +
                           "            <h1>Requirements</h1>\n" +
                           "        </strong>\n" +
                           "\n" +
                           "\n" +
                           "        <b>Nimaz</b>" +
                           " has the following CORE requirements to work correctly:\n" +
                           "    <ul>\n" +
                           "        <li>Internet Connection</li>\n" +
                           "        <li>City Name</li>\n" +
                           "        <li>Calculation Method</li>\n" +
                           "        <li>Calculation Adjustments</li>\n" +
                           "    </ul>\n" +
                           "\n" +
                           "    <h3>Internet Connection</h3>\n" +
                           "    <p>\n" +
                           "        <b>Nimaz</b>" +
                           " must have Internet connection at first install as it is required to get the location data from the city\n" +
                           "        name that will be discussed below.\n" +
                           "        <br />\n" +
                           "        After first install it saves the name of the City and the data acquired when there was connection last time it\n" +
                           "        was launched it will work with that city name only until connection is reestablished.\n" +
                           "    </p>\n" +
                           "\n" +
                           "    <h3>City Name</h3>\n" +
                           "    <p>\n" +
                           "        <b>Nimaz</b>" +
                           " Finds Prayer times from Latitude and Longitude Coordinates of the City name which can be changed as\n" +
                           "        long as there is internet connection when that change happens.\n" +
                           "        <br/><b>Nimaz</b> will switch between offline and online systems automatically. if the system changes between offline and online while <b>Nimaz</b> is open then " +
                           "<b>Nimaz</b> must be refreshed to apply the offline system or if not refreshed it will apply the offline system using data from last online session, the next time <b>Nimaz</b> is open<br/>" +
                           "you can change these values in settings.<br/>\n" +
                           "        The city name or in the case of offline use the Coordinates can be changed by Following steps:\n" +
                           "    <ol>\n" +
                           "        <li>Select Settings option from main page</li>\n" +
                           "        <li>At the top of the page Select Location</li>\n" +
                           "        <li>In the Dialog enter your city name</li>\n" +
                           "        <b>Nimaz</b> accepts input such as the full address, Country name, and is not case sensitive.\n" +
                           "        but it will select only the name of the city in cases such as:\n" +
                           "        <ul>\n" +
                           "            <li>Full Address</li>\n" +
                           "            <li>Partial Address</li>\n" +
                           "            <li>City Name</li>\n" +
                           "        </ul>\n" +
                           "        And in cases such as:\n" +
                           "        <ul>\n" +
                           "            <li>Country Name</li>\n" +
                           "        </ul>\n" +
                           "        It will take the country name as is and use that for prayer times.\n" +
                           "        <br />\n" +
                           "        If the city name is wrong it will use the default city name provided to it.\n" +
                           "    </ol>\n" +
                           "\n" +
                           "    </p>\n" +
                           "\n" +
                           "    <br />\n" +
                           "    <p>\n" +
                           "    <h3>Calculation Method</h3>\n" +
                           "    The Calculation methods can be found in Time Calculation tab in settings of <b>Nimaz</b>\n" +
                           "    <b>Nimaz</b> has Several calculation methods based on location and they are as follows with the names of its\n" +
                           "    corresponding location.\n" +
                           "    <ul>\n" +
                           "        <li><b> MUSLIM_WORLD_LEAGUE</b></li>\n" +
                           "        Used By Europe, Far East, parts of US\n" +
                           "\n" +
                           "        <li><b> EGYPTIAN</b></li>\n" +
                           "        Used by Africa, Syria, Lebanon, Malaysia\n" +
                           "\n" +
                           "        <li><b> KARACHI</b></li>\n" +
                           "        Used by Pakistan, Afghanistan, Bangladesh, India\n" +
                           "\n" +
                           "        <li><b>UMM_AL_QURA</b></li>\n" +
                           "        Used by Arabian Peninsula\n" +
                           "\n" +
                           "        <li><b> DUBAI</b></li>\n" +
                           "        used by the Gulf Region\n" +
                           "\n" +
                           "        <li><b> QATAR</b></li>\n" +
                           "        used by Qatar\n" +
                           "\n" +
                           "        <li><b> KUWAIT</b></li>\n" +
                           "        Used by Kuwait\n" +
                           "\n" +
                           "        <li><b> MOON_SIGHTING_COMMITTEE</b></li>\n" +
                           "        This method uses fajr angle of 18 and ishaa angle of 18, it also uses seasonal adjustment values" +
                           "         <br/> for summer and winter." +
                           "\n" +
                           "        <li><b> SINGAPORE</b></li>\n" +
                           "        Used by singapore, Malaysia\n" +
                           "\n" +
                           "        <li><b> NORTH_AMERICA</b></li>\n" +
                           "        Used by NorthAmerica\n" +
                           "\n" +
                           "        <li><b> FRANCE</b></li>\n" +
                           "        Used by France and surrounding Countries\n" +
                           "\n" +
                           "        <li><b> RUSSIA</b></li>\n" +
                           "        Used by Russia\n" +
                           "\n" +
                           "        <li><b> IRELAND</b></li>\n" +
                           "        Used by ireland and surrounding countries\n" +
                           "\n" +
                           "        A custom method with no preset values if the current method do not cover your need\n" +
                           "    </ul>\n" +
                           "\n" +
                           "    </p>\n" +
                           "\n" +
                           "\n" +
                           "    <p>\n" +
                           "    <h3>Calculation adjustments</h3>\n" +
                           "    <b>Nimaz</b> allows calculation adjustments up to an hour for all prayer times including the Sunrise time.\n" +
                           "    <br />\n" +
                           "    Each value is assigned a list with either a positive value or a negative value in it each positive value adds (in\n" +
                           "    minutes) that\n" +
                           "    value to the current time displayed in the main page and each negative value subtracts that amount (in minutes) from\n" +
                           "    the current time displayed.\n" +
                           "    </p>\n" +
                           "\n" +
                           "    <h1>Extras</h1>\n" +
                           "    <p>\n" +
                           "    <h3>More Adjustments</h3>\n" +
                           "    <p>Furthermore the values of high latitude rule, and madhab can also be changed.</p>\n" +
                           "\n" +
                           "    <h3>Alarm Troubleshooting</h3>\n" +
                           "    Sometimes the alarms may not work, this is due to Android restriction on alarm time execution.\n" +
                           "    <br />\n" +
                           "    This can also happen if you change a value near to an alarm time.\n" +
                           "    <br />\n" +
                           "    which is important to save battery life of your phone.\n" +
                           "    <p>If The alarms don't work do the following:\n" +
                           "    <ul>\n" +
                           "        <li>Check that the Mute button on main page is not set to silent</li>\n" +
                           "        <li>next check that the volume is not set to 0</li>\n" +
                           "        <li>check if alarms work on your device by setting the Test alarm in settings</li>\n" +
                           "        This can be found on the main page of settings. it will set an alarm in 10 seconds from when the tab is pressed\n" +
                           "        <li>next reset the alarms from the reset button on the main page</li>\n" +
                           "        <li>if it still does not work restart your phone <b>Nimaz</b> has a mechanism that will reset all alarms upon restart\n" +
                           "        </li>\n" +
                           "        <li>if it still does not work remove Nimaz app from battery Optimization list</li>\n" +
                           "        If you have already done this then Nimaz will not allow you to go there by displaying a message.\n" +
                           "           <br/> <br/>        " +
                           "This can be done using following steps:\n" +
                           "        <ol>\n" +
                           "            <li>Go to settings page of Nimaz app</li>\n" +
                           "            <li>Select Battery Optimization tab</li>\n" +
                           "            this will take you to your phone settings page where the Battery optimization list is located\n" +
                           "            <li>Change list to All apps</li>\n" +
                           "            <li>Find Nimaz in the list</li>\n" +
                           "            The list is sorted alphabetically from A to Z\n" +
                           "            <li>click Nimaz app Icon</li>\n" +
                           "            A dialog will open\n" +
                           "            <li>Select Don't Optimize from the Dialog and click ok</li>\n" +
                           "            now go back to Nimaz and reset alarms.\n" +
                           "        </ol>\n" +
                           "    </ul>\n" +
                           "    </p>\n" +
                           "    </p>\n" +
                           "\n" +
                           "\n" +
                           "    </p>"

        internet.text = Html.fromHtml(internettext , Html.FROM_HTML_MODE_LEGACY)

        return root
    }

}