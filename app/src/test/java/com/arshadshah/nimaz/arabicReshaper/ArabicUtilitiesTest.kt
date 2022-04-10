package com.arshadshah.nimaz.arabicReshaper

import junit.framework.TestCase

class ArabicUtilitiesTest : TestCase() {

    //setup
    private val arabicUtilities = ArabicUtilities

    //test od get word function that separates a string into words by spaces
    fun testGetWords() {
        val input = "الحمد لله"
        val expected = arrayOf("الحمد", "لله")
        val actual = arabicUtilities.getWords(input)
        assertEquals(expected[0], actual[0])
        assertEquals(expected[1], actual[1])
    }

    fun testHasArabicLetters() {
        val input = "الحمد لله"
        val expected = true
        val actual = arabicUtilities.hasArabicLetters(input)
        assertEquals(expected, actual)
    }

    fun testIsArabicWord() {
        val input = "الحمد"
        val expected = true
        val actual = arabicUtilities.isArabicWord(input)
        assertEquals(expected, actual)
    }

    fun testReshape() {
        val input = "الحمد لله"
        val expected = "اﻟﺤﻤﺪ   ﻟﻠﻪ"
        val actual = arabicUtilities.reshape(input)
        assertEquals(expected, actual)
    }

    fun testReshapeSentence() {
        val input = "الحمد لله"
        val expected = "اﻟﺤﻤﺪ   ﻟﻠﻪ"
        val actual = arabicUtilities.reshapeSentence(input)
        assertEquals(expected, actual)
    }
}