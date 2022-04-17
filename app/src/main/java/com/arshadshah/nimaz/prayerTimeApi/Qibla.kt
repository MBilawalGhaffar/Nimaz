package com.arshadshah.nimaz.prayerTimeApi

import com.arshadshah.nimaz.prayerTimeApi.internals.QiblaUtil.calculateQiblaDirection


class Qibla(coordinates: Coordinates?) {
    val direction: Double = calculateQiblaDirection(coordinates!!)

    companion object {
        private val MAKKAH = Coordinates(21.4225241, 39.8261818)
    }

}