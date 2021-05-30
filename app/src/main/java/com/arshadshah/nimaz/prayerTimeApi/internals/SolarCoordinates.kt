package com.arshadshah.nimaz.prayerTimeApi.internals

import com.arshadshah.nimaz.prayerTimeApi.internals.Astronomical.apparentObliquityOfTheEcliptic
import com.arshadshah.nimaz.prayerTimeApi.internals.Astronomical.apparentSolarLongitude
import com.arshadshah.nimaz.prayerTimeApi.internals.Astronomical.ascendingLunarNodeLongitude
import com.arshadshah.nimaz.prayerTimeApi.internals.Astronomical.meanLunarLongitude
import com.arshadshah.nimaz.prayerTimeApi.internals.Astronomical.meanObliquityOfTheEcliptic
import com.arshadshah.nimaz.prayerTimeApi.internals.Astronomical.meanSiderealTime
import com.arshadshah.nimaz.prayerTimeApi.internals.Astronomical.meanSolarLongitude
import com.arshadshah.nimaz.prayerTimeApi.internals.Astronomical.nutationInLongitude
import com.arshadshah.nimaz.prayerTimeApi.internals.Astronomical.nutationInObliquity
import com.arshadshah.nimaz.prayerTimeApi.internals.CalendricalHelper.julianCentury
import com.arshadshah.nimaz.prayerTimeApi.internals.DoubleUtil.unwindAngle
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

internal class SolarCoordinates(julianDay : Double)
{

    /**
     * The declination of the sun, the angle between the rays of the Sun and the
     * plane of the Earth's equator, in degrees.
     */
    @JvmField
    val declination : Double

    /**
     * Right ascension of the Sun, the angular distance on the celestial equator
     * from the vernal equinox to the hour circle, in degrees.
     */
    @JvmField
    val rightAscension : Double

    /**
     * Apparent sidereal time, the hour angle of the vernal equinox, in degrees.
     */
    @JvmField
    val apparentSiderealTime : Double

    init
    {
        val T = julianCentury(julianDay)
        val L0 = meanSolarLongitude( /* julianCentury */T)
        val Lp = meanLunarLongitude( /* julianCentury */T)
        val Ω = ascendingLunarNodeLongitude( /* julianCentury */T)
        val λ = Math.toRadians(
            apparentSolarLongitude( /* julianCentury */T ,  /* meanLongitude */L0)
                              )
        val θ0 = meanSiderealTime( /* julianCentury */T)
        val ΔΨ = nutationInLongitude( /* julianCentury */T ,  /* solarLongitude */
                                                         L0 ,  /* lunarLongitude */
                                                         Lp ,  /* ascendingNode */
                                                         Ω
                                    )
        val Δε = nutationInObliquity( /* julianCentury */T ,  /* solarLongitude */
                                                         L0 ,  /* lunarLongitude */
                                                         Lp ,  /* ascendingNode */
                                                         Ω
                                    )
        val ε0 = meanObliquityOfTheEcliptic( /* julianCentury */T)
        val εapp = Math.toRadians(
            apparentObliquityOfTheEcliptic( /* julianCentury */T ,  /* meanObliquityOfTheEcliptic */
                                                               ε0
                                          )
                                 )

        /* Equation from Astronomical Algorithms page 165 */
        declination = Math.toDegrees(
            asin(sin(εapp) * sin(λ))
                                    )

        /* Equation from Astronomical Algorithms page 165 */
        rightAscension = unwindAngle(
            Math.toDegrees(atan2(cos(εapp) * sin(λ) , cos(λ)))
                                    )

        /* Equation from Astronomical Algorithms page 88 */
        apparentSiderealTime =
            θ0 + ΔΨ * 3600 * cos(
                Math.toRadians(ε0 + Δε)
                                ) / 3600
    }
}