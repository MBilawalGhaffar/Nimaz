package com.arshadshah.nimaz.prayerTimeApi.internals

import com.arshadshah.nimaz.prayerTimeApi.Coordinates
import com.arshadshah.nimaz.prayerTimeApi.internals.DoubleUtil.unwindAngle
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan


object QiblaUtil {
    private val MAKKAH = Coordinates(21.4225241, 39.8261818)
    fun calculateQiblaDirection(coordinates: Coordinates): Double {
        // Equation from "Spherical Trigonometry For the use of colleges and schools" page 50
        val longitudeDelta =
            Math.toRadians(MAKKAH.longitude) - Math.toRadians(coordinates.longitude)
        val latitudeRadians = Math.toRadians(coordinates.latitude)
        val term1 = sin(longitudeDelta)
        val term2 = cos(latitudeRadians) * tan(Math.toRadians(MAKKAH.latitude))
        val term3 = sin(latitudeRadians) * cos(longitudeDelta)
        val angle = atan2(term1, term2 - term3)
        return unwindAngle(Math.toDegrees(angle))
    }
}