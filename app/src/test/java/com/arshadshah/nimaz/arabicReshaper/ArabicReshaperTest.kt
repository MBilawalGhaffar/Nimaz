package com.arshadshah.nimaz.arabicReshaper

import junit.framework.TestCase

class ArabicReshaperTest : TestCase() {

    //tests
    fun testArabicReshaper() {
        val input = "أحب اللغة العربية"
        val expected = "ﺃﺣﺒﺎﻟﻠﻐﺔاﻟﻌﺮﺑﻴﺔ"
        val actual = ArabicReshaper(input)
        assertEquals(expected, actual.reshapedWord)
    }
}