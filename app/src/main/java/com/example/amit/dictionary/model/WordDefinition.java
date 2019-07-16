package com.example.amit.dictionary.model;
/**
 * Created by AMIT YADAV
 * 13/10/18
 */


import java.util.List;

public class WordDefinition {
    //id is same as word id
    public String id;
    public String word;
    public String language;
    public String pronunciationUrl;
    public String pronunciationText;
    public String single_definition;
    public boolean isFav;
    public List<WordLexicalEntry> lexicalEntries;

}
