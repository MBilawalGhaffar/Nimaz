package com.arshadshah.nimaz.helperClasses.utils

//a class that contains a function to check a string and return constructed sqlite query
class SearchQueryFunctions {

    //function to check the context of the search query and return the constructed sqlite query for quran database
    //return a query that checks if the keywords given in the search query are present in the quran database in any context
    //in any of the rows in text
    fun checkContext(searchQuery: String): String {
        var query = ""
        if (searchQuery.contains(" ")) {
            val splitQuery = searchQuery.split(" ")
            for (i in splitQuery.indices) {
                if (i == 0) {
                    query += "WHERE "
                }
                query += if (i == splitQuery.size - 1) {
                    "text LIKE ${splitQuery[i]}"
                } else {
                    "text LIKE ${splitQuery[i]} OR "
                }
            }
        } else {
            query = "WHERE text LIKE $searchQuery"
        }

        //add the start and end of the query
        query = "SELECT * FROM en_sahih $query"
        return query
    }
}