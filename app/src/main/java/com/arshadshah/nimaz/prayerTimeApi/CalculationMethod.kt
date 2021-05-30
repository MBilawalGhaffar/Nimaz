package com.arshadshah.nimaz.prayerTimeApi

/**
 * Standard calculation methods for calculating prayer times
 */
enum class CalculationMethod
{

    /**
     * Muslim World League Uses Fajr angle of 18 and an Isha angle of 17
     */
    MUSLIM_WORLD_LEAGUE ,

    /**
     * Egyptian General Authority of Survey Uses Fajr angle of 19.5 and an Isha
     * angle of 17.5
     */
    EGYPTIAN ,

    /**
     * University of Islamic Sciences, Karachi Uses Fajr angle of 18 and an Isha
     * angle of 18
     */
    KARACHI ,

    /**
     * Umm al-Qura University, Makkah Uses a Fajr angle of 18.5 and an Isha angle of
     * 90. Note: You should add a +30 minute custom adjustment of Isha during
     * Ramadan.
     */
    UMM_AL_QURA ,

    /**
     * The Gulf Region Uses Fajr and Isha angles of 18.5 degrees.
     */
    DUBAI ,

    /**
     * Moonsighting Committee Uses a Fajr angle of 18 and an Isha angle of 18. Also
     * uses seasonal adjustment values.
     */
    MOON_SIGHTING_COMMITTEE ,

    /**
     * Referred to as the ISNA method This method is included for completeness, but
     * is not recommended. Uses a Fajr angle of 15 and an Isha angle of 15.
     */
    NORTH_AMERICA ,

    /**
     * Kuwait Uses a Fajr angle of 18 and an Isha angle of 17.5
     */
    KUWAIT ,

    /**
     * Qatar Modified version of Umm al-Qura that uses a Fajr angle of 18.
     */
    QATAR ,

    /**
     * Singapore Uses a Fajr angle of 20 and an Isha angle of 18
     */
    SINGAPORE ,

    /**
     * Union Organization Islamic de France uses fajr and isha angle of 12
     */
    FRANCE ,

    /**
     * Spiritual Administration of Muslims of Russia uses fajr of 16 and isha of 15
     */
    RUSSIA ,

    /**
     * IRELAND and NORTH WEST EUROPE INCLUDING ENGLAND Uses a Fajr angle of 16 and
     * an Isha angle of 14
     */
    IRELAND ,

    /**
     * The default value for [CalculationParameters.method] when initializing
     * a [CalculationParameters] object. Sets a Fajr angle of 0 and an Isha
     * angle of 0.
     */
    OTHER;

    /**
     * Return the CalculationParameters for the given method
     *
     * @return CalculationParameters for the given Calculation method
     */
    val parameters : CalculationParameters
        get() = when (this)
        {
            MUSLIM_WORLD_LEAGUE ->
            {
                CalculationParameters(18.0 , 17.0 , this)
            }

            EGYPTIAN ->
            {
                CalculationParameters(19.5 , 17.5 , this)
            }

            KARACHI ->
            {
                CalculationParameters(18.0 , 18.0 , this)
            }

            UMM_AL_QURA ->
            {
                CalculationParameters(18.5 , 90 , this)
            }

            DUBAI ->
            {
                CalculationParameters(18.2 , 18.2 , this)
            }

            MOON_SIGHTING_COMMITTEE ->
            {
                CalculationParameters(18.0 , 18.0 , this)
            }

            NORTH_AMERICA ->
            {
                CalculationParameters(15.0 , 15.0 , this)
            }

            KUWAIT ->
            {
                CalculationParameters(18.0 , 17.5 , this)
            }

            QATAR ->
            {
                CalculationParameters(18.0 , 90 , this)
            }

            SINGAPORE ->
            {
                CalculationParameters(20.0 , 18.0 , this)
            }

            FRANCE ->
            {
                CalculationParameters(12.0 , 12.0 , this)
            }

            RUSSIA ->
            {
                CalculationParameters(16.0 , 15.0 , this)
            }

            IRELAND ->
            {
                CalculationParameters(
                    14.0 ,
                    13.0 ,
                    this
                                     ).withMethodAdjustments(
                    PrayerAdjustments(
                        1 ,
                        - 2 ,
                        2 ,
                        0 ,
                        0 ,
                        - 2
                                     )
                                                            )
            }

            OTHER ->
            {
                CalculationParameters(0.0 , 0.0 , this)
            }

            else ->
            {
                throw IllegalArgumentException("Invalid CalculationMethod")
            }
        }
}