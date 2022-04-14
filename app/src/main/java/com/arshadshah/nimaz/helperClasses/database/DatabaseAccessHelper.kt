package com.arshadshah.nimaz.helperClasses.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.helperClasses.quran.AyaObject

/**
 * A class that provides methods to access the database
 * it is used to query the database
 * Created by Arshad Shah
 */
class DatabaseAccessHelper(context: Context) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * The database helper
     */
    private var dbHelper: DatabaseHelper? = null

    /**
     * The database
     */
    private var db: SQLiteDatabase? = null

    /**
     * Initializes the database
     */
    init {
        dbHelper = DatabaseHelper(context)
    }

    /**
     * Opens the database
     */
    fun open() {
        db = dbHelper!!.readableDatabase
    }

    /**
     * Closes the database
     */
    fun close() {
        dbHelper!!.close()
    }

    fun getAllJuzs(): Cursor? {
        //the cursor
        val cursor = db!!.rawQuery("SELECT * FROM juzs WHERE juznumberdata", null)
        //returns the juz object
        return cursor
    }

    fun getAllSuras(): Cursor? {
        //the cursor
        val cursor = db!!.rawQuery("SELECT * FROM suras WHERE suranumberdata", null)
        //returns the juz object
        return cursor
    }

    //function that returns the juzStartAyaInQuran for a given juznumber
    fun getJuzStartAyaInQuran(juznumber: Int): Int {
        //if the juznumber is 31 then the juzStartAyaInQuran is 6236
        //return 6236
        val ayaNumber: Int
        if (juznumber == 31) {
            ayaNumber = 6236
        } else {
            //the cursor
            val cursor = db!!.rawQuery(
                "SELECT juzStartAyaInQuran FROM juzs WHERE juznumberdata = $juznumber",
                null
            )
            //returns the juz object
            cursor.moveToFirst()
            ayaNumber = cursor.getInt(0)
        }

        return ayaNumber
    }

    //function to get all the ayas from quran_text between two numbers and return them as a cursor
    fun getAyaFromQuranText(startAya: Int, endAya: Int): Cursor? {
        //the cursor
        val cursor1 = db!!.rawQuery(
            "SELECT * FROM quran_text WHERE ayaNumberInQuran BETWEEN $startAya AND $endAya",
            null
        )
        //returns the juz object
        return cursor1
    }

    //function to get all the ayas from quran_text between two numbers and return them as a cursor
    fun getAyaFromUrduText(startAya: Int, endAya: Int): Cursor? {
        //the cursor
        val cursor1 = db!!.rawQuery(
            "SELECT * FROM urdu_text WHERE ayaNumberInQuranUrdu BETWEEN $startAya AND $endAya",
            null
        )
        //returns the juz object
        return cursor1
    }

    //function to get all the ayas from en_sahih between two numbers and return them as a cursor
    fun getAyaFromEnSahih(startAya: Int, endAya: Int): Cursor? {
        //the cursor
        val cursor2 = db!!.rawQuery(
            "SELECT * FROM en_sahih WHERE ayaNumberInQuranEnglish BETWEEN $startAya AND $endAya",
            null
        )
        //returns the juz object
        return cursor2
    }

    //function that returns all the aya based on the juz number given to it
    fun getAllAyaForJuz(juzNumber: Int): ArrayList<AyaObject?> {
        val currentJuzStartAya = getJuzStartAyaInQuran(juzNumber)
        val nextJuzStartAya = getJuzStartAyaInQuran(juzNumber + 1)
        val isEnglish = sharedPreferences.getBoolean("isEnglish", true)
        var cursorOfAyasFromEnglish: Cursor? = null
        var cursorOfAyasFromUrdu: Cursor? = null
        if (isEnglish) {
            cursorOfAyasFromEnglish = getAyaFromEnSahih(currentJuzStartAya, nextJuzStartAya - 1)
        } else {
            cursorOfAyasFromUrdu = getAyaFromUrduText(currentJuzStartAya, nextJuzStartAya - 1)
        }
        val cursorOfAyasFromArabic = getAyaFromQuranText(currentJuzStartAya, nextJuzStartAya - 1)

        //arraylist of AyaObjects
        val ayas = ArrayList<AyaObject?>()

        //array for arabic text
        val arabicText = ArrayList<String?>()

        //array for english text
        val translationText = ArrayList<String?>()

        //array for ayaNumber
        val ayaNumber = ArrayList<String?>()

        //loop through the arabic text cursor and add the ayas to the arraylist
        if (cursorOfAyasFromArabic != null && cursorOfAyasFromArabic.moveToFirst()) {
            do {
                //add the arabic text at index 3 to the arabicText array
                arabicText.add(cursorOfAyasFromArabic.getString(3))
                ayaNumber.add(cursorOfAyasFromArabic.getString(2))
            } while (cursorOfAyasFromArabic.moveToNext())
        }

        if (isEnglish) {
            //loop through the english text cursor and add the ayas to the arraylist
            if (cursorOfAyasFromEnglish != null && cursorOfAyasFromEnglish.moveToFirst()) {
                do {
                    //add the english text at index 3 to the englishText array
                    translationText.add(cursorOfAyasFromEnglish.getString(3))
                } while (cursorOfAyasFromEnglish.moveToNext())
            }
        } else {
            //loop through the english text cursor and add the ayas to the arraylist
            if (cursorOfAyasFromUrdu != null && cursorOfAyasFromUrdu.moveToFirst()) {
                do {
                    //add the english text at index 3 to the englishText array
                    translationText.add(cursorOfAyasFromUrdu.getString(3))
                } while (cursorOfAyasFromUrdu.moveToNext())
            }
        }


        //combine the arrays into an arraylist of ayaObjects
        for (i in 0 until arabicText.size) {
            ayas.add(AyaObject(ayaNumber[i]!!, translationText[i]!!, arabicText[i]!!))
        }
        cursorOfAyasFromArabic!!.close()
        if (isEnglish) {
            cursorOfAyasFromEnglish!!.close()
        } else {
            cursorOfAyasFromUrdu!!.close()
        }
        return ayas
    }

    //function that returns all aya from quran_text for given surahNumberInQuran and returns it in an array
    private fun getAllAyaFromQuranTextForSurah(surahNumberInQuran: Int): ArrayList<String?> {
        //the cursor
        val cursor = db!!.rawQuery(
            "SELECT * FROM quran_text WHERE suraNumberInQuran = $surahNumberInQuran",
            null
        )
        //array for arabic text
        val arabicText = ArrayList<String?>()

        while (cursor.moveToNext()) {
            //add the arabic text at index 3 to the arabicText array
            arabicText.add(cursor.getString(3))
        }
        cursor.close()

        return arabicText
    }

    //function that returns all aya from en_sahih for given surahNumberInQuran and returns it in an array
    private fun getAllAyaFromEnSahihForSurah(surahNumberInQuran: Int): ArrayList<String?> {
        //the cursor
        val cursor = db!!.rawQuery(
            "SELECT * FROM en_sahih WHERE suraNumberInQuranEnglish = $surahNumberInQuran",
            null
        )
        //array for english text
        val englishText = ArrayList<String?>()

        while (cursor.moveToNext()) {
            //add the english text at index 3 to the englishText array
            englishText.add(cursor.getString(3))
        }
        cursor.close()

        return englishText
    }

    //function that returns all aya from en_sahih for given surahNumberInQuran and returns it in an array
    private fun getAllAyaFromUrdu_TextForSurah(surahNumberInQuran: Int): ArrayList<String?> {
        //the cursor
        val cursor = db!!.rawQuery(
            "SELECT * FROM urdu_text WHERE suraNumberInQuranUrdu = $surahNumberInQuran",
            null
        )
        //array for english text
        val urduText = ArrayList<String?>()

        while (cursor.moveToNext()) {
            //add the english text at index 3 to the englishText array
            urduText.add(cursor.getString(3))
        }
        cursor.close()

        return urduText
    }

    //function that returns all ayaNumberInSurah from quran_text for given surahNumberInQuran and returns it in an array
    private fun getAllAyaNumberInSurahFromQuranText(surahNumberInQuran: Int): ArrayList<String?> {
        //the cursor
        val cursor = db!!.rawQuery(
            "SELECT * FROM quran_text WHERE suraNumberInQuran = $surahNumberInQuran",
            null
        )
        //array for ayaNumber
        val ayaNumber = ArrayList<String?>()

        while (cursor.moveToNext()) {
            //add the ayaNumber at index 2 to the ayaNumber array
            ayaNumber.add(cursor.getString(2))
        }
        cursor.close()

        return ayaNumber
    }

    //function that returns all the aya based on the surah number given to it
    fun getAllAyaForSurah(surahNumber: Int): ArrayList<AyaObject?> {
        //the cursor
        val arrayOfArabicText = getAllAyaFromQuranTextForSurah(surahNumber)

        val isEnglish = sharedPreferences.getBoolean("isEnglish", true)

        val arrayOfTranslationtext: ArrayList<String?> = if (isEnglish) {
            getAllAyaFromEnSahihForSurah(surahNumber)
        } else {
            getAllAyaFromUrdu_TextForSurah(surahNumber)
        }

        val arrayOfAyaNumber = getAllAyaNumberInSurahFromQuranText(surahNumber)

        //arraylist of AyaObjects
        val ayas = ArrayList<AyaObject?>()

        //combine the arrays into an arraylist of ayaObjects
        for (i in 0 until arrayOfArabicText.size) {
            ayas.add(
                AyaObject(
                    arrayOfAyaNumber[i]!!,
                    arrayOfTranslationtext[i]!!,
                    arrayOfArabicText[i]!!
                )
            )
        }

        return ayas
    }

    /**
     * function that searches the database for the given query and returns the results in an arraylist of AyaObjects
     * @param query the query to search for
     * @return an arraylist of AyaObjects
     */
    fun searchForAya(query: String, tableName: String, columnName: String): ArrayList<AyaObject?> {
        //find the ayas that have the query in the table
        val cursor =
            db!!.rawQuery("SELECT * FROM $tableName WHERE $columnName LIKE '%$query%'", null)

        //arabic text array
        val arabicText = ArrayList<String?>()

        //english text array
        val translationText = ArrayList<String?>()

        //aya number array
        val ayaNumber = ArrayList<String?>()

        //loop through the cursor and add the ayanumbers for the ayas to the table quran_text
        while (cursor.moveToNext()) {
            //add the ayaNumber at index 0 to the ayaNumber array
            ayaNumber.add(cursor.getString(0))
        }

        cursor.close()


        //arraylist of AyaObjects
        val ayas = ArrayList<AyaObject?>()


        //initialize the cursors for the tables
        lateinit var cursorOfAyasFromArabic: Cursor
        lateinit var cursorOfAyasFromEnglish: Cursor
        lateinit var cursorOfAyasFromUrdu: Cursor

        //find the ayas that have the ayanumberiquran in the table using arrayList ayaNumber
        for (i in 0 until ayaNumber.size) {
            //get a cursor to the table quran_text
            cursorOfAyasFromArabic = db!!.rawQuery(
                "SELECT * FROM quran_text WHERE ayaNumberInQuran = ${ayaNumber[i]}",
                null
            )
            while (cursorOfAyasFromArabic.moveToNext()) {
                //add the arabic text at index 3 to the arabicText array
                arabicText.add(cursorOfAyasFromArabic.getString(3))
            }

            //if the user is in english
            if (sharedPreferences.getBoolean("isEnglish", true)) {
                cursorOfAyasFromEnglish = db!!.rawQuery(
                    "SELECT * FROM en_sahih WHERE ayaNumberInQuranEnglish = ${ayaNumber[i]}",
                    null
                )
                while (cursorOfAyasFromEnglish.moveToNext()) {
                    //add the english text at index 3 to the englishText array
                    translationText.add(cursorOfAyasFromEnglish.getString(3))
                }
            } else {
                cursorOfAyasFromUrdu = db!!.rawQuery(
                    "SELECT * FROM urdu_text WHERE ayaNumberInQuranUrdu = ${ayaNumber[i]}",
                    null
                )
                while (cursorOfAyasFromUrdu.moveToNext()) {
                    //add the english text at index 3 to the englishText array
                    translationText.add(cursorOfAyasFromUrdu.getString(3))
                }
            }

            ayas.add(AyaObject(ayaNumber[i]!!, translationText[i]!!, arabicText[i]!!))
        }

        if (sharedPreferences.getBoolean("isEnglish", true)) {
            cursorOfAyasFromEnglish.close()
        } else {
            cursorOfAyasFromUrdu.close()
        }
        cursorOfAyasFromArabic.close()


        return ayas
    }


    /**
     * function that searches the database for the given query and returns the results as the number of ayas found
     * @param query the query to search for
     * @return the number of ayas found
     */
    fun searchForAyaAmountFound(query: String, tableName: String, columnName: String): Int {
        //find the ayas that have the query in the table
        val cursor =
            db!!.rawQuery("SELECT * FROM $tableName WHERE $columnName LIKE '%$query%'", null)


        //number of ayas found
        val ayaAmountForQuery = cursor.count

        cursor.close()

        return ayaAmountForQuery
    }

    fun getNumberOfAyatJuz(number: Int): ArrayList<String?> {
        val currentJuzStartAya = getJuzStartAyaInQuran(number)
        val nextJuzStartAya = getJuzStartAyaInQuran(number + 1)
        val cursor = getAyaFromQuranText(currentJuzStartAya, nextJuzStartAya - 1)
        val ayaNumber = ArrayList<String?>()

        while (cursor!!.moveToNext()) {
            //add the ayaNumber at index 2 to the ayaNumber array
            ayaNumber.add(cursor.getString(2))
        }
        cursor.close()

        return ayaNumber
    }

    //function that returns all aya from quran_text for given surahNumberInQuran and returns it in an array
    fun getNumberOfAyatSurah(surahNumberInQuran: Int): ArrayList<String?> {
        //the cursor
        val cursor = db!!.rawQuery(
            "SELECT * FROM quran_text WHERE suraNumberInQuran = $surahNumberInQuran",
            null
        )
        //array for arabic text
        val arabicText = ArrayList<String?>()

        while (cursor.moveToNext()) {
            //add the arabic text at index 3 to the arabicText array
            arabicText.add(cursor.getString(2))
        }
        cursor.close()

        return arabicText
    }
}