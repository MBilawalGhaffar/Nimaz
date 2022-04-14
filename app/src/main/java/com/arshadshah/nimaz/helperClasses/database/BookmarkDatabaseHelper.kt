package com.arshadshah.nimaz.helperClasses.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * a class that creates the bookmark database
 * with the required tables and columns
 * table: bookmarkSurah
 * columns: ayaNumberBookmark: String, ayaTextBookmark: String, ayaArabicBookmark: String, surahNumber
 * table: bookmarkJuz
 * columns: ayaNumberBookmark: String, ayaTextBookmark: String, ayaArabicBookmark: String, juzNumber
 * it extends SQLiteOpenHelper
 * @author Arshad Shah
 */
class BookmarkDatabaseHelper
    (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    //companion object to create the database
    companion object {
        //database name
        private const val DATABASE_NAME = "bookmarkDatabase"

        //database version
        private const val DATABASE_VERSION = 1

        //table name
        const val TABLE_BOOKMARK_SURAH = "bookmarkSurah"
        const val TABLE_BOOKMARK_JUZ = "bookmarkJuz"

        //columns
        const val COLUMN_AYA_NUMBER_BOOKMARK_ID = "ayaBookmarkId"
        const val COLUMN_AYA_NUMBER_BOOKMARK = "ayaNumberBookmark"
        const val COLUMN_AYA_TEXT_BOOKMARK = "ayaTextBookmark"
        const val COLUMN_AYA_ARABIC_BOOKMARK = "ayaArabicBookmark"
        const val COLUMN_SURAH_NUMBER = "surahNumber"

        const val COLUMN_AYA_NUMBER_BOOKMARK_ID_JUZ = "ayaBookmarkIdJuz"
        const val COLUMN_AYA_NUMBER_BOOKMARK_JUZ = "ayaNumberBookmark"
        const val COLUMN_AYA_TEXT_BOOKMARK_JUZ = "ayaTextBookmark"
        const val COLUMN_AYA_ARABIC_BOOKMARK_JUZ = "ayaArabicBookmark"
        const val COLUMN_JUZ_NUMBER = "juzNumber"
    }

    //create table sql
    private val CREATE_TABLE_BOOKMARK_SURAH = ("CREATE TABLE $TABLE_BOOKMARK_SURAH (" +
            "$COLUMN_AYA_NUMBER_BOOKMARK_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COLUMN_AYA_NUMBER_BOOKMARK TEXT, " +
            "$COLUMN_AYA_TEXT_BOOKMARK TEXT, " +
            "$COLUMN_AYA_ARABIC_BOOKMARK TEXT, " +
            "$COLUMN_SURAH_NUMBER INTEGER)")

    private val CREATE_TABLE_BOOKMARK_JUZ = ("CREATE TABLE $TABLE_BOOKMARK_JUZ (" +
            "$COLUMN_AYA_NUMBER_BOOKMARK_ID_JUZ INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COLUMN_AYA_NUMBER_BOOKMARK_JUZ TEXT, " +
            "$COLUMN_AYA_TEXT_BOOKMARK_JUZ TEXT, " +
            "$COLUMN_AYA_ARABIC_BOOKMARK_JUZ TEXT, " +
            "$COLUMN_JUZ_NUMBER INTEGER)")


    //drop table sql
    private val DROP_TABLE_BOOKMARK_SURAH = "DROP TABLE IF EXISTS $TABLE_BOOKMARK_SURAH"
    private val DROP_TABLE_BOOKMARK_JUZ = "DROP TABLE IF EXISTS $TABLE_BOOKMARK_JUZ"

    //database instance
    private var database: SQLiteDatabase? = null


    //function onCreate
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_BOOKMARK_SURAH)
        db.execSQL(CREATE_TABLE_BOOKMARK_JUZ)
    }

    //function onUpgrade
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_TABLE_BOOKMARK_SURAH)
        db.execSQL(DROP_TABLE_BOOKMARK_JUZ)
        onCreate(db)
    }

    init {
        //create or open the database in assets folder
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null)
    }
}