package com.arshadshah.nimaz.helperClasses.utils.sunMoonUtils

/**
 * Represents the moon position
 * @property altitude Moon altitude above the horizon in radians
 * @property azimuth Moon azimuth in radians
 * @property distanceKm Distance to moon in kilometers
 * @property parallacticAngle Parallactic angle of the moon in radians
 */
data class MoonPosition(
    val altitude: Double,
    val azimuth: Double,
    val distanceKm: Double,
    val parallacticAngle: Double
)