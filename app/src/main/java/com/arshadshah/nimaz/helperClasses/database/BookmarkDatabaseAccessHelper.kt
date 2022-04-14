package com.arshadshah.nimaz.helperClasses.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.arshadshah.nimaz.helperClasses.quran.Bookmark


/**
 * This class is used to store and retrieve the bookmarks from the database.
 * 
 */
class BookmarkDatabaseAccessHelper(context: Context) {
    
    //get the BookmarkDatabaseHelper instance
    private val bookmarkDatabaseHelper = BookmarkDatabaseHelper(context)

    //open the database
    fun open(): SQLiteDatabase? {
        return bookmarkDatabaseHelper.writableDatabase
    }

    //close the database
    fun close() {
        bookmarkDatabaseHelper.close()
    }


    /**
     * This method is used to check if the bookmark exists in the database.
     * isAyaBookmarked is used to check if the aya is bookmarked or not in Surah Table
     * @param ayahNumber the ayah number of the bookmark
     * @param ayaText the aya text of the bookmark
     * @param arabicText the arabic text of the bookmark
     * @return true if the bookmark exists, false otherwise
     *
     */
    fun isAyaBookmarkedSurah(ayahNumber: String, ayaText: String, arabicText: String): Boolean {
        val database = open() ?: return false

        val whereClause = "(${BookmarkDatabaseHelper.COLUMN_AYA_NUMBER_BOOKMARK} = ? OR ${BookmarkDatabaseHelper.COLUMN_AYA_TEXT_BOOKMARK} = ?) AND ${BookmarkDatabaseHelper.COLUMN_AYA_ARABIC_BOOKMARK} = ?"

        val whereArgs = arrayOf(ayahNumber, ayaText, arabicText)

        //check in surah table
        val cursorSurah = database.query(BookmarkDatabaseHelper.TABLE_BOOKMARK_SURAH, null, whereClause, whereArgs, null, null, null)

        //check if the bookmark exists
        val isBookmarked = cursorSurah.count > 0

        cursorSurah.close()

        close()

        return isBookmarked
    }

     /**
     * This method is used to check if the bookmark exists in the database.
     * isAyaBookmarked is used to check if the aya is bookmarked or not in Juz Table
     * @param ayahNumber the ayah number of the bookmark
     * @param ayaText the aya text of the bookmark
     * @param arabicText the arabic text of the bookmark
     * @return true if the bookmark exists, false otherwise
     *
     */
    fun isAyaBookmarkedJuz(ayahNumber: String, ayaText: String, arabicText: String): Boolean {
        val database = open() ?: return false

        val whereClause = "(${BookmarkDatabaseHelper.COLUMN_AYA_NUMBER_BOOKMARK_JUZ} = ? OR ${BookmarkDatabaseHelper.COLUMN_AYA_TEXT_BOOKMARK_JUZ} = ?) AND ${BookmarkDatabaseHelper.COLUMN_AYA_ARABIC_BOOKMARK_JUZ} = ?"

        val whereArgs = arrayOf(ayahNumber, ayaText, arabicText)

        //check in surah table
        val cursorJuz = database.query(BookmarkDatabaseHelper.TABLE_BOOKMARK_JUZ, null, whereClause, whereArgs, null, null, null)

        //check if the bookmark exists
        val isBookmarked = cursorJuz.count > 0

        cursorJuz.close()

        close()

        return isBookmarked
    }
    

    /**
     * This method is used to add a bookmark to the database.
     * for a surah
     * @param ayaNumber the aya number of the bookmark
     * @param ayaText the aya text of the bookmark
     * @param arabicText the arabic text of the bookmark
     * @param surahNumber the surah number of the bookmark
     * 
     * @return true if the bookmark is added successfully, false otherwise
     *
     */
    fun addBookmarkSurah(ayaNumber: String, ayaText: String, arabicText: String, surahNumber: String): Boolean {
        //get the database
        val database = open() ?: return false

        //check if the database is null

        //create the values
        val values = ContentValues()
        values.put(BookmarkDatabaseHelper.COLUMN_AYA_NUMBER_BOOKMARK, ayaNumber)
        values.put(BookmarkDatabaseHelper.COLUMN_AYA_TEXT_BOOKMARK, ayaText)
        values.put(BookmarkDatabaseHelper.COLUMN_AYA_ARABIC_BOOKMARK, arabicText)
        values.put(BookmarkDatabaseHelper.COLUMN_SURAH_NUMBER, surahNumber)

        //insert the values
        val rowId = database.insert(BookmarkDatabaseHelper.TABLE_BOOKMARK_SURAH, null, values)

        //check if the row id is -1
        if (rowId == -1L) {
            return false
        }

        //close the database
        close()

        //return true
        return true

    }

    /**
     * This method is used to delete a bookmark from the database.
     * for a juz
     * @param ayaNumber the aya number of the bookmark
     * @param ayaText the aya text of the bookmark
     * @param arabicText the arabic text of the bookmark
     * @param juzNumber the juz number of the bookmark
     * 
     * @return true if the bookmark is added successfully, false otherwise
     *
     */
    fun addBookmarkJuz(ayaNumber: String, ayaText: String, arabicText: String, juzNumber:String): Boolean {
        //get the database
        val database = open() ?: return false

        //check if the database is null

        //create the values
        val values = ContentValues()
        values.put(BookmarkDatabaseHelper.COLUMN_AYA_NUMBER_BOOKMARK_JUZ, ayaNumber)
        values.put(BookmarkDatabaseHelper.COLUMN_AYA_TEXT_BOOKMARK_JUZ, ayaText)
        values.put(BookmarkDatabaseHelper.COLUMN_AYA_ARABIC_BOOKMARK_JUZ, arabicText)
        values.put(BookmarkDatabaseHelper.COLUMN_JUZ_NUMBER, juzNumber)

        //insert the values
        val rowId = database.insert(BookmarkDatabaseHelper.TABLE_BOOKMARK_JUZ, null, values)

        //check if the row id is -1
        if (rowId == -1L) {
            return false
        }

        //close the database
        close()

        //return true
        return true

    }

    /**
     * This method is used to delete a bookmark from the database.
     * 
     * @param ayaNumber the aya number of the bookmark
     * @param surahNumber the surah number of the bookmark
     * 
     * @return true if the bookmark is deleted successfully, false otherwise
     *
     */
    fun deleteBookmarkSurah(ayaNumber: String, surahNumber: String): Boolean {
        //get the database
        val database = open() ?: return false

        //check if the database is null

        //create the where clause
        val whereClause = "${BookmarkDatabaseHelper.COLUMN_AYA_NUMBER_BOOKMARK} = ? AND ${BookmarkDatabaseHelper.COLUMN_SURAH_NUMBER} = ?"

        //create the where arguments
        val whereArgs = arrayOf(ayaNumber.toString(), surahNumber.toString())

        //delete the bookmark
        val rowId = database.delete(BookmarkDatabaseHelper.TABLE_BOOKMARK_SURAH, whereClause, whereArgs)

        //check if the row id is -1
        if (rowId == -1) {
            return false
        }

        //close the database
        close()

        //return true
        return true
    }

    /**
     * This method is used to delete a bookmark from the database.
     * 
     * @param ayaNumber the aya number of the bookmark
     * @param juzNumber the juz number of the bookmark
     * 
     * @return true if the bookmark is deleted successfully, false otherwise
     *
     */
    fun deleteBookmarkJuz(ayaNumber: String, juzNumber:String): Boolean {
        //get the database
        val database = open() ?: return false

        //check if the database is null

        //create the where clause
        val whereClause = "${BookmarkDatabaseHelper.COLUMN_AYA_NUMBER_BOOKMARK_JUZ} = ? AND ${BookmarkDatabaseHelper.COLUMN_JUZ_NUMBER} = ?"

        //create the where arguments
        val whereArgs = arrayOf(ayaNumber.toString(), juzNumber.toString())

        //delete the bookmark
        val rowId = database.delete(BookmarkDatabaseHelper.TABLE_BOOKMARK_JUZ, whereClause, whereArgs)

        //check if the row id is -1
        if (rowId == -1) {
            return false
        }

        //close the database
        close()

        //return true
        return true
    }

    /**
     * This method is used to retrieve the bookmarks from the database.
     * for a given surah number.
     * @param surahNumber the surah number of the bookmarks
     * @return the bookmarks
     *
     */
    @SuppressLint("Range")
    fun getBookmarksForSurah(surahNumber: String): ArrayList<Bookmark> {
        //get the database
        val database = open() ?: return ArrayList()

        //check if the database is null

        //create the where clause
        val whereClause = "${BookmarkDatabaseHelper.COLUMN_SURAH_NUMBER} = ?"

        //create the where arguments
        val whereArgs = arrayOf(surahNumber.toString())

        //create the cursor
        val cursor = database.query(BookmarkDatabaseHelper.TABLE_BOOKMARK_SURAH, null, whereClause, whereArgs, null, null, null)

        //create the bookmarks array list
        val bookmarks = ArrayList<Bookmark>()

        //check if the cursor is null
        if (cursor == null) {
            return bookmarks
        }

        //check if the cursor is empty
        if (cursor.count == 0) {
            return bookmarks
        }

        //move to the first row
        cursor.moveToFirst()

        //loop through the cursor
        do {
            //create the bookmark
            val bookmark = Bookmark(
                cursor.getString(cursor.getColumnIndex(BookmarkDatabaseHelper.COLUMN_AYA_NUMBER_BOOKMARK)),
                cursor.getString(cursor.getColumnIndex(BookmarkDatabaseHelper.COLUMN_AYA_TEXT_BOOKMARK)),
                cursor.getString(cursor.getColumnIndex(BookmarkDatabaseHelper.COLUMN_AYA_ARABIC_BOOKMARK)),
                cursor.getString(cursor.getColumnIndex(BookmarkDatabaseHelper.COLUMN_SURAH_NUMBER))
            )

            //add the bookmark to the bookmarks array list
            bookmarks.add(bookmark)

        } while (cursor.moveToNext())

        //close the cursor
        cursor.close()

        //close the database
        close()

        //return the bookmarks
        return bookmarks
    }


    /**
     * This method is used to retrieve the bookmarks from the database.
     * for a given juz number.
     * @param juzNumber the juz number of the bookmarks
     * @return the bookmarks
     */
    @SuppressLint("Range")
    fun getBookmarksForJuz(juzNumber:String): ArrayList<Bookmark> {
        //get the database
        val database = open() ?: return ArrayList()

        //check if the database is null

        //create the where clause
        val whereClause = "${BookmarkDatabaseHelper.COLUMN_SURAH_NUMBER} = ?"

        //create the where arguments
        val whereArgs = arrayOf(juzNumber.toString())

        //create the cursor
        val cursor = database.query(BookmarkDatabaseHelper.TABLE_BOOKMARK_JUZ, null, whereClause, whereArgs, null, null, null)

        //create the bookmarks array list
        val bookmarks = ArrayList<Bookmark>()

        //check if the cursor is null
        if (cursor == null) {
            return bookmarks
        }

        //check if the cursor is empty
        if (cursor.count == 0) {
            return bookmarks
        }

        //move to the first row
        cursor.moveToFirst()

        //loop through the cursor
        do {
            //create the bookmark
            val bookmark = Bookmark(
                cursor.getString(cursor.getColumnIndex(BookmarkDatabaseHelper.COLUMN_AYA_NUMBER_BOOKMARK_JUZ)),
                cursor.getString(cursor.getColumnIndex(BookmarkDatabaseHelper.COLUMN_AYA_TEXT_BOOKMARK_JUZ)),
                cursor.getString(cursor.getColumnIndex(BookmarkDatabaseHelper.COLUMN_AYA_ARABIC_BOOKMARK_JUZ)),
                cursor.getString(cursor.getColumnIndex(BookmarkDatabaseHelper.COLUMN_SURAH_NUMBER))
            )

            //add the bookmark to the bookmarks array list
            bookmarks.add(bookmark)

        } while (cursor.moveToNext())

        //close the cursor
        cursor.close()

        //close the database
        close()

        //return the bookmarks
        return bookmarks
    }

}