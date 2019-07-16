package com.example.amit.dictionary.model;
/**
 * Created by AMIT YADAV
 * 13/10/18
 */




import java.util.List;

/**
 * DO NOT DELETE THIS FILE
 * A search query results in form of this model
 * when ../search?=word query made
 */

public class SearchResult {
    public List<QueryResult> results;
    public class QueryResult {
        public String matchType;
        public String word;
        public String id;
    }
}
