package com.arshadshah.nimaz.helperClasses.utils.prayerTimesUtils

import com.arshadshah.nimaz.helperClasses.utils.prayerTimesUtils.data.CalendarUtil.add
import com.arshadshah.nimaz.helperClasses.utils.prayerTimesUtils.data.CalendarUtil.isLeapYear
import com.arshadshah.nimaz.helperClasses.utils.prayerTimesUtils.data.CalendarUtil.resolveTime
import com.arshadshah.nimaz.helperClasses.utils.prayerTimesUtils.data.CalendarUtil.roundedMinute
import com.arshadshah.nimaz.helperClasses.utils.prayerTimesUtils.data.DateComponents
import com.arshadshah.nimaz.helperClasses.utils.prayerTimesUtils.data.DateComponents.Companion.fromUTC
import com.arshadshah.nimaz.helperClasses.utils.prayerTimesUtils.data.TimeComponents.Companion.fromDouble
import com.arshadshah.nimaz.helperClasses.utils.prayerTimesUtils.internals.SolarTime
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

class PrayerTimes(
    val coordinates: Coordinates,
    dateComponents: DateComponents,
    calculationParameters: CalculationParameters
) {

    var fajr: Date? = null
    var sunrise: Date? = null
    var dhuhr: Date? = null
    var asr: Date? = null
    var maghrib: Date? = null
    var isha: Date? = null

    @JvmOverloads
    fun currentPrayer(time: Date = Date()): Prayer {
        val fajrTommorow = fajr!!.time + 86400000
        val `when` = time.time
        return when {
            isha!!.time - `when` <= 0 -> {
                Prayer.ISHA
            }

            maghrib!!.time - `when` <= 0 -> {
                Prayer.MAGHRIB
            }

            asr!!.time - `when` <= 0 -> {
                Prayer.ASR
            }

            dhuhr!!.time - `when` <= 0 -> {
                Prayer.DHUHR
            }

            sunrise!!.time - `when` <= 0 -> {
                Prayer.SUNRISE
            }

            fajr!!.time - `when` <= 0 -> {
                Prayer.FAJR
            }
            `when` in isha!!.time..fajrTommorow -> {
                Prayer.ISHA
            }
            `when` < fajr!!.time -> {
                Prayer.ISHA
            }
            else -> {
                Prayer.NONE
            }
        }
    }

    @JvmOverloads
    fun nextPrayer(time: Date = Date()): Prayer {

        val `when` = time.time
        return when {
            isha!!.time - `when` <= 0 -> {
                Prayer.FAJR
            }

            maghrib!!.time - `when` <= 0 -> {
                Prayer.ISHA
            }

            asr!!.time - `when` <= 0 -> {
                Prayer.MAGHRIB
            }

            dhuhr!!.time - `when` <= 0 -> {
                Prayer.ASR
            }

            sunrise!!.time - `when` <= 0 -> {
                Prayer.DHUHR
            }

            fajr!!.time - `when` <= 0 -> {
                Prayer.SUNRISE
            }
            else -> {
                Prayer.FAJR
            }
        }
    }

    fun timeForPrayer(prayer: Prayer?): Date? {
        return when (prayer) {
            Prayer.FAJR -> fajr
            Prayer.SUNRISE -> sunrise
            Prayer.DHUHR -> dhuhr
            Prayer.ASR -> asr
            Prayer.MAGHRIB -> maghrib
            Prayer.ISHA -> isha
            Prayer.NONE -> null
            else -> null
        }
    }

    companion object {

        private fun seasonAdjustedMorningTwilight(
            latitude: Double,
            day: Int,
            year: Int,
            sunrise: Date?
        ): Date {
            val a = 75 + 28.65 / 55.0 * abs(latitude)
            val b = 75 + 19.44 / 55.0 * abs(latitude)
            val c = 75 + 32.74 / 55.0 * abs(latitude)
            val d = 75 + 48.10 / 55.0 * abs(latitude)
            val adjustment: Double
            val dyy = daysSinceSolstice(day, year, latitude)
            adjustment = when {
                dyy < 91 -> {
                    a + (b - a) / 91.0 * dyy
                }

                dyy < 137 -> {
                    b + (c - b) / 46.0 * (dyy - 91)
                }

                dyy < 183 -> {
                    c + (d - c) / 46.0 * (dyy - 137)
                }

                dyy < 229 -> {
                    d + (c - d) / 46.0 * (dyy - 183)
                }

                dyy < 275 -> {
                    c + (b - c) / 46.0 * (dyy - 229)
                }

                else -> {
                    b + (a - b) / 91.0 * (dyy - 275)
                }
            }
            return add(sunrise, (-(adjustment * 60.0).roundToInt()), Calendar.SECOND)
        }

        private fun seasonAdjustedEveningTwilight(
            latitude: Double,
            day: Int,
            year: Int,
            sunset: Date?
        ): Date {
            val a = 75 + 25.60 / 55.0 * abs(latitude)
            val b = 75 + 2.050 / 55.0 * abs(latitude)
            val c = 75 - 9.210 / 55.0 * abs(latitude)
            val d = 75 + 6.140 / 55.0 * abs(latitude)
            val adjustment: Double
            val dyy = daysSinceSolstice(day, year, latitude)
            adjustment = when {
                dyy < 91 -> {
                    a + (b - a) / 91.0 * dyy
                }

                dyy < 137 -> {
                    b + (c - b) / 46.0 * (dyy - 91)
                }

                dyy < 183 -> {
                    c + (d - c) / 46.0 * (dyy - 137)
                }

                dyy < 229 -> {
                    d + (c - d) / 46.0 * (dyy - 183)
                }

                dyy < 275 -> {
                    c + (b - c) / 46.0 * (dyy - 229)
                }

                else -> {
                    b + (a - b) / 91.0 * (dyy - 275)
                }
            }
            return add(sunset, (adjustment * 60.0).roundToInt(), Calendar.SECOND)
        }

        private fun daysSinceSolstice(dayOfYear: Int, year: Int, latitude: Double): Int {
            var daysSinceSolistice: Int
            val northernOffset = 10
            val isLeapYear = isLeapYear(year)
            val southernOffset = if (isLeapYear) 173 else 172
            val daysInYear = if (isLeapYear) 366 else 365
            if (latitude >= 0) {
                daysSinceSolistice = dayOfYear + northernOffset
                if (daysSinceSolistice >= daysInYear) {
                    daysSinceSolistice -= daysInYear
                }
            } else {
                daysSinceSolistice = dayOfYear - southernOffset
                if (daysSinceSolistice < 0) {
                    daysSinceSolistice += daysInYear
                }
            }
            return daysSinceSolistice
        }
    }

    /**
     * Calculate PrayerTimes
     *
     * @param coordinates the coordinates of the location
     * @param date        the date components for that location
     * @param parameters  the parameters for the calculation
     */
    init {
        var tempFajr: Date? = null
        var tempSunrise: Date? = null
        var tempDhuhr: Date? = null
        var tempAsr: Date? = null
        var tempMaghrib: Date? = null
        var tempIsha: Date? = null
        val prayerDate = resolveTime(dateComponents)
        val calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = prayerDate
        val dayOfYear = calendar[Calendar.DAY_OF_YEAR]
        val tomorrowDate = add(prayerDate, 1, Calendar.DATE)
        val tomorrow = fromUTC(tomorrowDate)
        val solarTime = SolarTime(dateComponents, coordinates)
        var timeComponents = fromDouble(solarTime.transit)
        val transit = timeComponents?.dateComponents(
            dateComponents
        )
        timeComponents = fromDouble(solarTime.sunrise)
        val sunriseComponents = timeComponents?.dateComponents(
            dateComponents
        )
        timeComponents = fromDouble(solarTime.sunset)
        val sunsetComponents = timeComponents?.dateComponents(
            dateComponents
        )
        val tomorrowSolarTime = SolarTime(tomorrow, coordinates)
        val tomorrowSunriseComponents = fromDouble(tomorrowSolarTime.sunrise)
        val error =
            transit == null || sunriseComponents == null || sunsetComponents == null || tomorrowSunriseComponents == null
        if (!error) {
            tempDhuhr = transit
            tempSunrise = sunriseComponents
            tempMaghrib = sunsetComponents
            timeComponents =
                fromDouble(solarTime.afternoon(calculationParameters.madhab.shadowLength))
            if (timeComponents != null) {
                tempAsr = timeComponents.dateComponents(dateComponents)
            }

            // get night length
            val tomorrowSunrise = tomorrowSunriseComponents!!.dateComponents(tomorrow)
            val night = tomorrowSunrise.time - sunsetComponents!!.time
            timeComponents =
                fromDouble(solarTime.hourAngle(-calculationParameters.fajrAngle, false))
            if (timeComponents != null) {
                tempFajr = timeComponents.dateComponents(dateComponents)
            }
            if (calculationParameters.method === CalculationMethod.MOON_SIGHTING_COMMITTEE && coordinates.latitude >= 55) {
                tempFajr = add(sunriseComponents, -1 * (night / 7000).toInt(), Calendar.SECOND)
            }
            val nightPortions = calculationParameters.nightPortions()
            val safeFajr: Date =
                if (calculationParameters.method === CalculationMethod.MOON_SIGHTING_COMMITTEE) {
                    seasonAdjustedMorningTwilight(
                        coordinates.latitude,
                        dayOfYear,
                        dateComponents.year,
                        sunriseComponents
                    )
                } else {
                    val portion = nightPortions.fajr
                    val nightFraction = (portion * night / 1000).toLong()
                    add(sunriseComponents, -1 * nightFraction.toInt(), Calendar.SECOND)
                }
            if (tempFajr == null || tempFajr.before(safeFajr)) {
                tempFajr = safeFajr
            }

            // Isha calculation with check against safe value
            if (calculationParameters.ishaInterval > 0) {
                tempIsha =
                    add(tempMaghrib, calculationParameters.ishaInterval * 60, Calendar.SECOND)
            } else {
                timeComponents =
                    fromDouble(solarTime.hourAngle(-calculationParameters.ishaAngle, true))
                if (timeComponents != null) {
                    tempIsha = timeComponents.dateComponents(dateComponents)
                }
                if (calculationParameters.method === CalculationMethod.MOON_SIGHTING_COMMITTEE && coordinates.latitude >= 55) {
                    val nightFraction = night / 7000
                    tempIsha = add(sunsetComponents, nightFraction.toInt(), Calendar.SECOND)
                }
                val safeIsha: Date =
                    if (calculationParameters.method === CalculationMethod.MOON_SIGHTING_COMMITTEE) {
                        seasonAdjustedEveningTwilight(
                            coordinates.latitude, dayOfYear, dateComponents.year,
                            sunsetComponents
                        )
                    } else {
                        val portion = nightPortions.isha
                        val nightFraction = (portion * night / 1000).toLong()
                        add(sunsetComponents, nightFraction.toInt(), Calendar.SECOND)
                    }
                if (tempIsha == null || tempIsha.after(safeIsha)) {
                    tempIsha = safeIsha
                }
            }
        }
        if (error || tempAsr == null) {
            // if we don't have all prayer times then initialization failed
            fajr = null
            sunrise = null
            dhuhr = null
            asr = null
            maghrib = null
            isha = null
        } else {
            // Assign final times to public struct members with all offsets
            fajr = roundedMinute(
                add(
                    add(tempFajr, calculationParameters.adjustments.fajr, Calendar.MINUTE),
                    calculationParameters.methodAdjustments.fajr, Calendar.MINUTE
                )
            )
            sunrise = roundedMinute(
                add(
                    add(tempSunrise, calculationParameters.adjustments.sunrise, Calendar.MINUTE),
                    calculationParameters.methodAdjustments.sunrise, Calendar.MINUTE
                )
            )
            dhuhr = roundedMinute(
                add(
                    add(tempDhuhr, calculationParameters.adjustments.dhuhr, Calendar.MINUTE),
                    calculationParameters.methodAdjustments.dhuhr, Calendar.MINUTE
                )
            )
            asr = roundedMinute(
                add(
                    add(tempAsr, calculationParameters.adjustments.asr, Calendar.MINUTE),
                    calculationParameters.methodAdjustments.asr, Calendar.MINUTE
                )
            )
            maghrib = roundedMinute(
                add(
                    add(tempMaghrib, calculationParameters.adjustments.maghrib, Calendar.MINUTE),
                    calculationParameters.methodAdjustments.maghrib, Calendar.MINUTE
                )
            )
            isha = roundedMinute(
                add(
                    add(tempIsha, calculationParameters.adjustments.isha, Calendar.MINUTE),
                    calculationParameters.methodAdjustments.isha, Calendar.MINUTE
                )
            )
        }
    }
}