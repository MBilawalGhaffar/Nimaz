package com.arshadshah.nimaz.arabicReshaper

class ArabicReshaper(unshapedWord: String) {
    /**
     * The Reshaped Word
     * @return reshaped Word
     */
    /**
     * The reshaped Word String
     */
    var reshapedWord = ""

    /**
     * Searching for the letter and Get the right shape for the character depends on the location specified
     * @param target The character that needs to get its form
     * @param location The location of the Form letter
     * @return The letter with its right shape
     */
    private fun getReshapedGlphy(target: Char, location: Int): Char {
        //Iterate over the 36 characters in the GLPHIES Matrix
        for (n in ARABIC_GLPHIES.indices) {
            //Check if the character equals the target character
            if (ARABIC_GLPHIES[n][0] == target) {
                //Get the right shape for the character, depends on the location
                return ARABIC_GLPHIES[n][location]
            }
        }
        //get the same character, If not found in the GLPHIES Matrix
        return target
    }

    /**
     * Define which Character Type is This, that has 2,3 or 4 Forms variation?
     * @param target The character, that needed
     * @return the integer number indicated the Number of forms the Character has, return 2 otherwise
     */
    private fun getGlphyType(target: Char): Int {
        //Iterate over the 36 characters in the GLPHIES Matrix
        for (n in ARABIC_GLPHIES.indices) {
            //Check if the character equals the target character
            if (ARABIC_GLPHIES[n][0] == target) //Get the number of Forms that the character has
                return ARABIC_GLPHIES[n][5].toInt()
        }
        //Return the number 2 Otherwise
        return 2
    }

    /**
     * returns true if the target character is a haraka
     * @param target
     * @return
     */
    private fun isHaraka(target: Char): Boolean {
        for (n in HARAKATE.indices) {
            //Check if the character equals the target character
            if (HARAKATE[n] == target) //Get the number of Forms that the character has
                return true
        }
        return false
    }

    private fun replaceLamAlef(unshapedWord: String): String {
        var unshapedWord = unshapedWord
        val wordLength = unshapedWord.length
        val wordLetters = CharArray(wordLength)
        unshapedWord.toCharArray(wordLetters, 0, 0, wordLength)
        var letterBefore = 0.toChar()
        for (index in 0 until wordLetters.size - 1) {
            if (!isHaraka(wordLetters[index]) && DEFINED_CHARACTERS_ORGINAL_LAM.toInt() != wordLetters[index]
                    .toInt()
            ) {
                letterBefore = wordLetters[index]
            }
            if (DEFINED_CHARACTERS_ORGINAL_LAM.toInt() == wordLetters[index]
                    .toInt()
            ) {
                val candidateLam = wordLetters[index]
                var harakaPosition = index + 1
                while (harakaPosition < wordLetters.size && isHaraka(wordLetters[harakaPosition])) {
                    harakaPosition++
                }
                if (harakaPosition < wordLetters.size) {
                    var lamAlef = 0.toChar()
                    lamAlef = if (index > 0 && getGlphyType(letterBefore) > 2) getLamAlef(
                        wordLetters[harakaPosition], candidateLam, false
                    ) else {
                        getLamAlef(wordLetters[harakaPosition], candidateLam, true)
                    }
                    if (lamAlef != 0.toChar()) {
                        wordLetters[index] = lamAlef
                        wordLetters[harakaPosition] = ' '
                    }
                }
            }
        }
        unshapedWord = String(wordLetters)
        unshapedWord = unshapedWord.replace(" ".toRegex(), "")
        return unshapedWord.trim { it <= ' ' }
    }

    /**
     * Get LamAlef right Character Presentation of the character
     * @param candidateAlef The letter that is supposed to Alef
     * @param candidateLam The letter that is supposed to Lam
     * @param isEndOfWord Is those characters at the end of the Word, to get its right form
     * @return Reshaped character of the LamAlef
     */
    private fun getLamAlef(candidateAlef: Char, candidateLam: Char, isEndOfWord: Boolean): Char {
        //The shift rate, depends if the the end of the word or not!
        var shiftRate = 1

        //The reshaped Lam Alef
        var reshapedLamAlef = 0.toChar()

        //Check if at the end of the word
        if (isEndOfWord) shiftRate++

        //check if the Lam is matching the candidate Lam
        if (DEFINED_CHARACTERS_ORGINAL_LAM.toInt() == candidateLam.toInt()) {

            //Check which Alef is matching after the Lam and get Its form
            if (candidateAlef.toInt() == DEFINED_CHARACTERS_ORGINAL_ALF_UPPER_MDD.toInt()) {
                reshapedLamAlef = LAM_ALEF_GLPHIES[0][shiftRate]
            }
            if (candidateAlef.toInt() == DEFINED_CHARACTERS_ORGINAL_ALF_UPPER_HAMAZA.toInt()) {
                reshapedLamAlef = LAM_ALEF_GLPHIES[1][shiftRate]
            }
            if (candidateAlef.toInt() == DEFINED_CHARACTERS_ORGINAL_ALF_LOWER_HAMAZA.toInt()) {
                reshapedLamAlef = LAM_ALEF_GLPHIES[3][shiftRate]
            }
            if (candidateAlef.toInt() == DEFINED_CHARACTERS_ORGINAL_ALF.toInt()) {
                reshapedLamAlef = LAM_ALEF_GLPHIES[2][shiftRate]
            }
        }
        //return the ReshapedLamAlef
        return reshapedLamAlef
    }

    /**
     * Decompose the word into two parts:
     * - simple letters with their positions
     * - Tashkil alone with their position
     *
     */
    internal inner class DecomposedWord(unshapedWord: String) {
        var stripedHarakates: CharArray
        var harakatesPositions: IntArray
        var stripedRegularLetters: CharArray
        var lettersPositions: IntArray

        /**
         * reconstruct the word when the reshaping ahs been done
         * @param reshapedWord
         * @return
         */
        fun reconstructWord(reshapedWord: String): String {
            var wordWithHarakates: CharArray? = null
            wordWithHarakates = CharArray(reshapedWord.length + stripedHarakates.size)
            for (index in lettersPositions.indices) {
                wordWithHarakates[lettersPositions[index]] = reshapedWord[index]
            }
            for (index in harakatesPositions.indices) {
                wordWithHarakates[harakatesPositions[index]] = stripedHarakates[index]
            }
            return String(wordWithHarakates)
        }

        /**
         * decompose the word
         * @param unshapedWord
         */
        init {
            val wordLength = unshapedWord.length
            var harakatesCount = 0
            for (index in 0 until wordLength) {
                if (isHaraka(unshapedWord[index])) {
                    harakatesCount++
                }
            }
            harakatesPositions = IntArray(harakatesCount)
            stripedHarakates = CharArray(harakatesCount)
            lettersPositions = IntArray(wordLength - harakatesCount)
            stripedRegularLetters = CharArray(wordLength - harakatesCount)
            harakatesCount = 0
            var letterCount = 0
            for (index in 0 until unshapedWord.length) {
                if (isHaraka(unshapedWord[index])) {
                    harakatesPositions[harakatesCount] = index
                    stripedHarakates[harakatesCount] = unshapedWord[index]
                    harakatesCount++
                } else {
                    lettersPositions[letterCount] = index
                    stripedRegularLetters[letterCount] = unshapedWord[index]
                    letterCount++
                }
            }
        }
    }

    /**
     * Main Reshaping function, Doesn't Support LamAlef
     * @param unshapedWord The unReshaped Word to Reshape
     * @return The Reshaped Word without the LamAlef Support
     */
    fun reshapeIt(unshapedWord: String): String {

        //The reshaped Word to Return
        val reshapedWord = StringBuffer("")
        val wordLength = unshapedWord.length

        //The Word Letters
        val wordLetters = CharArray(wordLength)

        //Copy the unreshapedWord to the WordLetters Character Array
        unshapedWord.toCharArray(wordLetters, 0, 0, wordLength)


        //for the first letter
        reshapedWord.append(
            getReshapedGlphy(
                wordLetters[0],
                2
            )
        ) //2 is the Form when the Letter is at the start of the word


        //iteration from the second till the second to last
        for (i in 1 until wordLength - 1) {
            val beforeLast = i - 1
            //Check if the Letter Before Last has only 2 Forms, for the current Letter to be as a start for a new Word!
            if (getGlphyType(wordLetters[beforeLast]) == 2) { //checking if it's only has 2 shapes
                //If the letter has only 2 shapes, then it doesnt matter which position it is, It'll be always the second form
                reshapedWord.append(getReshapedGlphy(wordLetters[i], 2))
            } else {
                //Then it should be in the middle which should be placed in its right form [3]
                reshapedWord.append(getReshapedGlphy(wordLetters[i], 3))
            }
        }

        //check for the last letter Before last has 2 forms, that means that the last Letter will be alone.
        if (wordLength >= 2) {
            if (getGlphyType(wordLetters[wordLength - 2]) == 2) {
                //If the letter has only 2 shapes, then it doesnt matter which position it is, It'll be always the second form
                reshapedWord.append(getReshapedGlphy(wordLetters[wordLength - 1], 1))
            } else {
                //Put the right form of the character, 4 for the last letter in the word
                reshapedWord.append(getReshapedGlphy(wordLetters[wordLength - 1], 4))
            }
        }
        //Return the ReshapedWord
        return reshapedWord.toString()
    }

    companion object {
        var DEFINED_CHARACTERS_ORGINAL_ALF_UPPER_MDD = 0x0622.toChar()
        var DEFINED_CHARACTERS_ORGINAL_ALF_UPPER_HAMAZA = 0x0623.toChar()
        var DEFINED_CHARACTERS_ORGINAL_ALF_LOWER_HAMAZA = 0x0625.toChar()
        var DEFINED_CHARACTERS_ORGINAL_ALF = 0x0627.toChar()
        var DEFINED_CHARACTERS_ORGINAL_LAM = 0x0644.toChar()
        var LAM_ALEF_GLPHIES = arrayOf(
            charArrayOf(15270.toChar(), 65270.toChar(), 65269.toChar()),
            charArrayOf(15271.toChar(), 65272.toChar(), 65271.toChar()),
            charArrayOf(1575.toChar(), 65276.toChar(), 65275.toChar()),
            charArrayOf(1573.toChar(), 65274.toChar(), 65273.toChar())
        )
        var HARAKATE = charArrayOf(
            '\u0600',
            '\u0601',
            '\u0602',
            '\u0603',
            '\u0606',
            '\u0607',
            '\u0608',
            '\u0609',
            '\u060A',
            '\u060B',
            '\u060D',
            '\u060E',
            '\u0610',
            '\u0611',
            '\u0612',
            '\u0613',
            '\u0614',
            '\u0615',
            '\u0616',
            '\u0617',
            '\u0618',
            '\u0619',
            '\u061A',
            '\u061B',
            '\u061E',
            '\u061F',
            '\u0621',
            '\u063B',
            '\u063C',
            '\u063D',
            '\u063E',
            '\u063F',
            '\u0640',
            '\u064B',
            '\u064C',
            '\u064D',
            '\u064E',
            '\u064F',
            '\u0650',
            '\u0651',
            '\u0652',
            '\u0653',
            '\u0654',
            '\u0655',
            '\u0656',
            '\u0657',
            '\u0658',
            '\u0659',
            '\u065A',
            '\u065B',
            '\u065C',
            '\u065D',
            '\u065E',
            '\u0660',
            '\u066A',
            '\u066B',
            '\u066C',
            '\u066F',
            '\u0670',
            '\u0672',
            '\u06D4',
            '\u06D5',
            '\u06D6',
            '\u06D7',
            '\u06D8',
            '\u06D9',
            '\u06DA',
            '\u06DB',
            '\u06DC',
            '\u06DF',
            '\u06E0',
            '\u06E1',
            '\u06E2',
            '\u06E3',
            '\u06E4',
            '\u06E5',
            '\u06E6',
            '\u06E7',
            '\u06E8',
            '\u06E9',
            '\u06EA',
            '\u06EB',
            '\u06EC',
            '\u06ED',
            '\u06EE',
            '\u06EF',
            '\u06D6',
            '\u06D7',
            '\u06D8',
            '\u06D9',
            '\u06DA',
            '\u06DB',
            '\u06DC',
            '\u06DD',
            '\u06DE',
            '\u06DF',
            '\u06F0',
            '\u06FD',
            '\uFE70',
            '\uFE71',
            '\uFE72',
            '\uFE73',
            '\uFE74',
            '\uFE75',
            '\uFE76',
            '\uFE77',
            '\uFE78',
            '\uFE79',
            '\uFE7A',
            '\uFE7B',
            '\uFE7C',
            '\uFE7D',
            '\uFE7E',
            '\uFE7F',
            '\uFC5E',
            '\uFC5F',
            '\uFC60',
            '\uFC61',
            '\uFC62',
            '\uFC63'
        )
        var ARABIC_GLPHIES = arrayOf(
            charArrayOf('\u0622', '\uFE81', '\uFE81', '\uFE82', '\uFE82', 2.toChar()),
            charArrayOf('\u0623', '\uFE83', '\uFE83', '\uFE84', '\uFE84', 2.toChar()),
            charArrayOf('\u0624', '\uFE85', '\uFE85', '\uFE86', '\uFE86', 2.toChar()),
            charArrayOf('\u0625', '\uFE87', '\uFE87', '\uFE88', '\uFE88', 2.toChar()),
            charArrayOf('\u0626', '\uFE89', '\uFE8B', '\uFE8C', '\uFE8A', 4.toChar()),
            charArrayOf('\u0627', '\u0627', '\u0627', '\uFE8E', '\uFE8E', 2.toChar()),
            charArrayOf('\u0628', '\uFE8F', '\uFE91', '\uFE92', '\uFE90', 4.toChar()),
            charArrayOf('\u0629', '\uFE93', '\uFE93', '\uFE94', '\uFE94', 2.toChar()),
            charArrayOf('\u062A', '\uFE95', '\uFE97', '\uFE98', '\uFE96', 4.toChar()),
            charArrayOf('\u062B', '\uFE99', '\uFE9B', '\uFE9C', '\uFE9A', 4.toChar()),
            charArrayOf('\u062C', '\uFE9D', '\uFE9F', '\uFEA0', '\uFE9E', 4.toChar()),
            charArrayOf('\u062D', '\uFEA1', '\uFEA3', '\uFEA4', '\uFEA2', 4.toChar()),
            charArrayOf('\u062E', '\uFEA5', '\uFEA7', '\uFEA8', '\uFEA6', 4.toChar()),
            charArrayOf('\u062F', '\uFEA9', '\uFEA9', '\uFEAA', '\uFEAA', 2.toChar()),
            charArrayOf('\u0630', '\uFEAB', '\uFEAB', '\uFEAC', '\uFEAC', 2.toChar()),
            charArrayOf('\u0631', '\uFEAD', '\uFEAD', '\uFEAE', '\uFEAE', 2.toChar()),
            charArrayOf('\u0632', '\uFEAF', '\uFEAF', '\uFEB0', '\uFEB0', 2.toChar()),
            charArrayOf('\u0633', '\uFEB1', '\uFEB3', '\uFEB4', '\uFEB2', 4.toChar()),
            charArrayOf('\u0634', '\uFEB5', '\uFEB7', '\uFEB8', '\uFEB6', 4.toChar()),
            charArrayOf('\u0635', '\uFEB9', '\uFEBB', '\uFEBC', '\uFEBA', 4.toChar()),
            charArrayOf('\u0636', '\uFEBD', '\uFEBF', '\uFEC0', '\uFEBE', 4.toChar()),
            charArrayOf('\u0637', '\uFEC1', '\uFEC3', '\uFEC4', '\uFEC2', 4.toChar()),
            charArrayOf('\u0638', '\uFEC5', '\uFEC7', '\uFEC8', '\uFEC6', 4.toChar()),
            charArrayOf('\u0639', '\uFEC9', '\uFECB', '\uFECC', '\uFECA', 4.toChar()),
            charArrayOf('\u063A', '\uFECD', '\uFECF', '\uFED0', '\uFECE', 4.toChar()),
            charArrayOf('\u0641', '\uFED1', '\uFED3', '\uFED4', '\uFED2', 4.toChar()),
            charArrayOf('\u0642', '\uFED5', '\uFED7', '\uFED8', '\uFED6', 4.toChar()),
            charArrayOf('\u0643', '\uFED9', '\uFEDB', '\uFEDC', '\uFEDA', 4.toChar()),
            charArrayOf('\u0644', '\uFEDD', '\uFEDF', '\uFEE0', '\uFEDE', 4.toChar()),
            charArrayOf('\u0645', '\uFEE1', '\uFEE3', '\uFEE4', '\uFEE2', 4.toChar()),
            charArrayOf('\u0646', '\uFEE5', '\uFEE7', '\uFEE8', '\uFEE6', 4.toChar()),
            charArrayOf('\u0647', '\uFEE9', '\uFEEB', '\uFEEC', '\uFEEA', 4.toChar()),
            charArrayOf('\u0648', '\uFEED', '\uFEED', '\uFEEE', '\uFEEE', 2.toChar()),
            charArrayOf('\u0649', '\uFEEF', '\uFEEF', '\uFEF0', '\uFEF0', 2.toChar()),
            charArrayOf('\u0671', '\u0671', '\u0671', '\uFB51', '\uFB51', 2.toChar()),
            charArrayOf('\u064A', '\uFEF1', '\uFEF3', '\uFEF4', '\uFEF2', 4.toChar()),
            charArrayOf('\u066E', '\uFBE4', '\uFBE8', '\uFBE9', '\uFBE5', 4.toChar()),
            charArrayOf('\u0671', '\u0671', '\u0671', '\uFB51', '\uFB51', 2.toChar()),
            charArrayOf('\u06AA', '\uFB8E', '\uFB90', '\uFB91', '\uFB8F', 4.toChar()),
            charArrayOf('\u06C1', '\uFBA6', '\uFBA8', '\uFBA9', '\uFBA7', 4.toChar()),
            charArrayOf('\u06E4', '\u06E4', '\u06E4', '\u06E4', '\uFEEE', 2.toChar())
        )
    }

    /**
     * Constructor of the Class
     * @param unshapedWord The unShaped Word
     */
    init {
        var unshapedWord = unshapedWord
        unshapedWord = replaceLamAlef(unshapedWord)
        val decomposedWord = DecomposedWord(unshapedWord)
        if (decomposedWord.stripedRegularLetters.isNotEmpty()) {
            reshapedWord = reshapeIt(String(decomposedWord.stripedRegularLetters))
        }
        reshapedWord = decomposedWord.reconstructWord(reshapedWord)
    }
}