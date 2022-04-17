package com.arshadshah.nimaz.arabicReshaper

import android.graphics.Typeface


/**
 * This class is the main class that is responsible for Reshaping Arabic Sentences and Text
 * Utilities Class to make it easier to deal with Arabic Reshaper Class
 * Wrapper for Arabic Reshaper Class
 * @author Amr Gawish
 */
object ArabicUtilities {
    /**
     * the path of teh fonts file must be under assets folder
     */
    private const val FONTS_LOCATION_PATH = "fonts/quran_font.ttf"
    var face: Typeface? = null

    /**
     * Helper function is to check if the character passed, is Arabic
     * @param target The Character to check Against
     * @return true if the Character is Arabic letter, otherwise returns false
     */
    private fun isArabicCharacter(target: Char): Boolean {

        //Iterate over the 36 Characters in ARABIC_GLPHIES Matrix
        for (element in ArabicReshaper.ARABIC_GLPHIES) {
            //Check if the target Character exist in ARABIC_GLPHIES Matrix
            if (element[0] == target) return true
        }
        for (element in ArabicReshaper.HARAKATE) {
            //Check if the target Character exist in ARABIC_GLPHIES Matrix
            if (element == target) return true
        }
        return false
    }

    /**
     * Helper function to split Sentence By Space
     * @param sentence the Sentence to Split into Array of Words
     * @return Array Of words
     */
    fun getWords(sentence: String?): Array<String?> {
        return sentence?.split("\\s".toRegex())?.toTypedArray() ?: arrayOfNulls(0)
    }

    /**
     * Helper function to check if the word has Arabic Letters
     * @param word The to check Against
     * @return true if the word has Arabic letters, false otherwise
     */
    fun hasArabicLetters(word: String?): Boolean {

        //Iterate over the word to check all the word's letters
        for (element in word!!) {
            if (isArabicCharacter(element)) return true
        }
        return false
    }

    /**
     * Helper function to check if the word is all Arabic Word
     * @param word The word to check against
     * @return true if the word is Arabic Word, false otherwise
     */
    fun isArabicWord(word: String?): Boolean {
        //Iterate over the Word
        for (i in 0 until word!!.length) {
            if (!isArabicCharacter(word[i])) return false
        }
        return true
    }

    /**
     * Helper function to split the Mixed Word into words with only Arabic, and English Words
     * @param word The Mixed Word
     * @return The Array of the Words of each Word may exist inside that word
     */
    private fun getWordsFromMixedWord(word: String?): Array<String?> {

        //The return result of words
        val finalWords: MutableList<String> = ArrayList()

        //Temp word to hold the current word
        var tempWord = ""

        //Iterate over the Word Length
        for (i in 0 until word!!.length) {

            //Check if the Character is Arabic Character
            if (isArabicCharacter(word[i])) {

                //Check if the tempWord is not empty, and what left in tempWord is not Arabic Word
                if (tempWord != "" && !isArabicWord(tempWord)) {

                    //add the Word into the Array
                    finalWords.add(tempWord)

                    //initiate the tempWord again
                    tempWord = "" + word[i]
                } else {

                    //Not to add the tempWord, but to add the character to the rest of the characters
                    tempWord += word[i]
                }
            } else {

                //Check if the tempWord is not empty, and what left in tempWord is Arabic Word
                if (tempWord != "" && isArabicWord(tempWord)) {

                    //add the Word into the Array
                    finalWords.add(tempWord)

                    //initiate the tempWord again
                    tempWord = "" + word[i]
                } else {

                    //Not to add the tempWord, but to add the character to the rest of the characters
                    tempWord += word[i]
                }
            }
        }

        // add remaining tempWord to finalWords
        if (tempWord != "") {
            finalWords.add(tempWord)
        }
        return finalWords.toTypedArray()
    }

    fun reshape(allText: String?): String? {
        return if (allText != null) {
            val result = StringBuffer()
            val sentences = allText.split("\n".toRegex()).toTypedArray()
            for (i in sentences.indices) {
                result.append(reshapeSentence(sentences[i]))
                // don't append the line feed to the final sentence
                if (i < sentences.size - 1) {
                    result.append("\n")
                }
            }
            result.toString()
        } else {
            null
        }
    }

    /**
     * The Main Reshaping Function to be Used in Android Program
     * @param sentence The text to be Reshaped
     * @return the Reshaped Text
     */
    fun reshapeSentence(sentence: String?): String {
        //get the Words from the Text
        val words = getWords(sentence)

        //prepare the Reshaped Text
        val reshapedText = StringBuffer("")

        //Iterate over the Words
        for (i in words.indices) {

            //Check if the Word has Arabic Letters
            if (hasArabicLetters(words[i])) {

                //Check if the Whole word is Arabic
                if (isArabicWord(words[i])) {

                    //Initiate the ArabicReshaper functionality
                    val arabicReshaper = ArabicReshaper(words[i]!!)


                    //Append the Reshaped Arabic Word to the Reshaped Whole Text
                    reshapedText.append(arabicReshaper.reshapedWord)
                } else { //The word has Arabic Letters, but its not an Arabic Word, its a mixed word

                    //Extract words from the words (split Arabic, and English)
                    val mixedWords = getWordsFromMixedWord(words[i])

                    //iterate over mixed Words
                    for (j in mixedWords.indices) {

                        //Initiate the ArabicReshaper functionality
                        val arabicReshaper = ArabicReshaper(mixedWords[j]!!)

                        //Append the Reshaped Arabic Word to the Reshaped Whole Text
                        reshapedText.append(arabicReshaper.reshapedWord)
                    }
                }
            } else { //The word doesn't have any Arabic Letters

                //Just append the word to the whole reshaped Text
                reshapedText.append(words[i])
            }

            if (i < words.size - 1) {
                //Append the space to separate between words
                reshapedText.append("   ")
            }
        }

        //return the final reshaped whole text
        return reshapedText.toString()
    }
}