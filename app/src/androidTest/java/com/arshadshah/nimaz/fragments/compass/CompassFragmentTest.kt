package com.arshadshah.nimaz.fragments.compass

import android.hardware.Sensor
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arshadshah.nimaz.prayerTimeApi.Coordinates
import com.arshadshah.nimaz.prayerTimeApi.Qibla
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.ceil

@RunWith(AndroidJUnit4::class)
class CompassFragmentTest {
    @Test
    fun test_Compass_Fragment_To_Have_Latitude_Longitude() {

        val scenario = launchFragment<CompassFragment>()

        //move state to started
        scenario.moveToState(Lifecycle.State.STARTED)

        //check that the longitude and latitude are not null
        scenario.onFragment { fragment ->
            TestCase.assertNotNull(fragment.longitude)
            TestCase.assertNotNull(fragment.latitude)
        }
    }

    @Test
    fun test_Compass_Fragment_To_Have_Qibla_Direction() {

        val scenario = launchFragment<CompassFragment>()

        //move state to started
        scenario.moveToState(Lifecycle.State.STARTED)

        //check that the qibla direction is 113
        scenario.onFragment { fragment ->
            val expected = 113.0
            val coordinates = Coordinates(fragment.latitude, fragment.longitude)
            val qiblaDirection =  Qibla(coordinates)
            TestCase.assertEquals(expected, ceil(qiblaDirection.direction))
        }
    }

    @Test
    fun test_Compass_Fragment_To_Register_Accelarometer_Magnotometer() {

        val scenario = launchFragment<CompassFragment>()

        //move state to started
        scenario.moveToState(Lifecycle.State.STARTED)

        //check that the sensor manager is not null
        scenario.onFragment { fragment ->
            TestCase.assertNotNull(fragment.sensorManager)
        }

                //unregister the accelerometer sensor and magnetometer sensor
        scenario.onFragment { fragment ->

            //get the accelerometer sensor from the fragment
            val accelerometer = fragment.accelerometer
            val magnetometer = fragment.magnetometer

            //check that the accelerometer and magnetometer are not null
            TestCase.assertNotNull(accelerometer)
            TestCase.assertNotNull(magnetometer)
        }
    }
}
