package com.arshadshah.nimaz.helperClasses.quran
/**
 * bookmark object that is used in bookmark database
* @param ayaNumber the aya number of the bookmark
* @param ayaText the aya text of the bookmark
* @param arabicText the arabic text of the bookmark
* @param Number the juz number of the bookmark
 */
data class Bookmark(val ayaNumber: String, val ayaText: String, val arabicText: String, val typeNumber: String)