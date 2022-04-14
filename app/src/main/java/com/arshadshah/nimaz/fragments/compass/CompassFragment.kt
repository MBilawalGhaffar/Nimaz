package com.arshadshah.nimaz.fragments.compass

import android.content.Context.SENSOR_SERVICE
import android.content.pm.PackageManager
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.utils.NetworkChecker
import com.arshadshah.nimaz.helperClasses.utils.locationFinder
import com.arshadshah.nimaz.prayerTimeApi.Coordinates
import com.arshadshah.nimaz.prayerTimeApi.Qibla
import com.google.android.material.snackbar.Snackbar
import kotlin.math.ceil
import kotlin.properties.Delegates


open class CompassFragment : Fragment(), SensorEventListener {

    var latitude by Delegates.notNull<Double>()
    var longitude by Delegates.notNull<Double>()

    // variables
    lateinit var sensorManager: SensorManager
    private lateinit var image: ImageView
    lateinit var accelerometer: Sensor
    lateinit var magnetometer: Sensor

    private var currentDegree = 0.0f
    private var lastAccelerometer = FloatArray(3)
    private var lastMagnetometer = FloatArray(3)
    private var lastAccelerometerSet = false
    private var lastMagnetometerSet = false


    private lateinit var compassLayout: ConstraintLayout

    private lateinit var packageManager: PackageManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for requireContext() fragment
        val root = inflater.inflate(R.layout.fragment_compass, container, false)

        packageManager = requireContext().packageManager
        // initialize variables
        image = root.findViewById(R.id.compass)
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)) {
            Snackbar.make(
                compassLayout,
                "Your Device does not have a compass!",
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            sensorManager = requireContext().getSystemService(SENSOR_SERVICE) as SensorManager
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        }

        val location: TextView = root.findViewById(R.id.location)
        val degrees: TextView = root.findViewById(R.id.degree)

        // get values from settings
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val name = sharedPreferences.getString("location_input", "Portlaoise")
        val locationTypeValue = sharedPreferences.getBoolean("locationType", true)
        val isNetworkAvailable = NetworkChecker().networkCheck(requireContext())
        if (isNetworkAvailable) {
            if (!locationTypeValue) {
                //location finder class
                val lonAndLat = locationFinder()
                lonAndLat.findLongAndLan(requireContext(), name!!)
            }
            location.text = sharedPreferences.getString("location_input", "Portlaoise").toString()
            latitude = sharedPreferences.getString("latitude", "0.0")!!.toDouble()
            longitude = sharedPreferences.getString("longitude", "0.0")!!.toDouble()
        } else {
            with(sharedPreferences.edit()) {
                putString("location_input", "No Network")
                apply()
            }
            latitude = sharedPreferences.getString("latitude", "0.0")!!.toDouble()
            longitude = sharedPreferences.getString("longitude", "0.0")!!.toDouble()
        }

        val coordinates = Coordinates(latitude, longitude)
        val qibla = Qibla(coordinates)
        val directionToQibla = qibla.direction
        val res: Resources = resources
        val text = res.getString(R.string.degrees_label, (ceil(directionToQibla)).toString())
        degrees.text = text

        compassLayout = root.findViewById(R.id.compassLayout)

        return root
    }

    override fun onResume() {
        super.onResume()
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)) {
            Log.e("Compass error", "Device does not have compass feature")
            Toast.makeText(
                requireContext(),
                "Device does not have compass feature",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME)
        }
    } // end of onResume

    override fun onPause() {
        super.onPause()
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)) {
            Log.e("Compass error", "Device does not have compass feature")
            Toast.makeText(
                requireContext(),
                "Device does not have compass feature",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            sensorManager.unregisterListener(this, accelerometer)
            sensorManager.unregisterListener(this, magnetometer)
        }
    } // end of onpause

    // Onsensor changed function
    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor === accelerometer) {
            lowPass(event!!.values, lastAccelerometer)
            lastAccelerometerSet = true
        } else if (event!!.sensor === magnetometer) {
            lowPass(event!!.values, lastMagnetometer)
            lastMagnetometerSet = true
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            val r = FloatArray(9)

            if (SensorManager.getRotationMatrix(r, null, lastAccelerometer, lastMagnetometer)) {
                val orientation = FloatArray(3)

                SensorManager.getOrientation(r, orientation)

                var degree = (Math.toDegrees(orientation[0].toDouble()) + 360).toFloat() % 360

                val sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(requireContext())

                // bearing
                val latitude = sharedPreferences.getString("latitude", "0.0")!!.toDouble()
                val longitude = sharedPreferences.getString("longitude", "0.0")!!.toDouble()

                val coordinates = Coordinates(latitude, longitude)
                val qibla = Qibla(coordinates)
                degree -= qibla.direction.toFloat()
                val rotateAnimation =
                    RotateAnimation(
                        currentDegree,
                        -degree,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f
                    )
                rotateAnimation.duration = 500
                rotateAnimation.fillAfter = true

                image.startAnimation(rotateAnimation)
                currentDegree = -degree

            } // inner if
        } // outerif
    } // end of onsensor changed

    private fun lowPass(input: FloatArray, output: FloatArray) {
        val alpha = 0.05f

        for (i in input.indices) {
            output[i] = output[i] + alpha * (input[i] - output[i])
        }
    } // end of lowPass

    // onAcurracyChanged function
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}