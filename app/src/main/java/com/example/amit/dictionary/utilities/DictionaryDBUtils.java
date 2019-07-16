package com.example.amit.dictionary.utilities;
/**
 * Created by AMIT YADAV
 * 17/10/18
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.amit.dictionary.data.DictContract.DictEntry;
import com.example.amit.dictionary.model.WordDefinition;

import java.util.ArrayList;
import java.util.Arrays;


public class DictionaryDBUtils {

    public static final String[] WORD_COMPLETE_PROJECTION =
            new String[]{
                    DictEntry.WORD_ID,
                    DictEntry.WORD,
                    DictEntry.LANGUAGE,
                    DictEntry.SINGLE_DEFINITION,
                    DictEntry.PRONNUNCIATION_URL,
                    DictEntry.PRONNUNCIATION_TEXT,
                    DictEntry.LEXICAL_ENTRIES,
            };

    public static WordDefinition getWordFromCursor(Cursor cursor) {
        WordDefinition word = null;
        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            word = new WordDefinition();
            word.id = cursor.getString(cursor.getColumnIndex(DictEntry.WORD_ID));

            if ((cursor.getColumnIndex(DictEntry.WORD) != -1))
                word.word = cursor.getString(cursor.getColumnIndex(DictEntry.WORD));

            if ((cursor.getColumnIndex(DictEntry.LANGUAGE) != -1))
                word.language = cursor.getString(cursor.getColumnIndex(DictEntry.LANGUAGE));

            if ((cursor.getColumnIndex(DictEntry.PRONNUNCIATION_URL) != -1))
                word.pronunciationUrl = cursor.getString(cursor.getColumnIndex(DictEntry.PRONNUNCIATION_URL));

            if ((cursor.getColumnIndex(DictEntry.PRONNUNCIATION_TEXT) != -1))
                word.pronunciationText = cursor.getString(cursor.getColumnIndex(DictEntry.PRONNUNCIATION_TEXT));

            if ((cursor.getColumnIndex(DictEntry.LEXICAL_ENTRIES) != -1))
                word.lexicalEntries = DictionaryJsonUtils.getLexList(cursor.getString(cursor.getColumnIndex(DictEntry.LEXICAL_ENTRIES)));

            if ((cursor.getColumnIndex(DictEntry.SINGLE_DEFINITION) != -1))
                word.single_definition = cursor.getString(cursor.getColumnIndex(DictEntry.SINGLE_DEFINITION));


            cursor.close();
            word.isFav = true;
        }
        return word;
    }

    public static void insertWordIntoDatabase(Context context, Uri uri, WordDefinition word) {
        ContentValues cv = new ContentValues();
        cv.put(DictEntry.WORD_ID, word.id);
        cv.put(DictEntry.WORD, word.word);
        cv.put(DictEntry.LANGUAGE, word.language);
        cv.put(DictEntry.SINGLE_DEFINITION, word.single_definition);
        cv.put(DictEntry.PRONNUNCIATION_TEXT, word.pronunciationText);
        cv.put(DictEntry.PRONNUNCIATION_URL, word.pronunciationUrl);
        cv.put(DictEntry.LEXICAL_ENTRIES, DictionaryJsonUtils.getLexJsonString(word));
        context.getContentResolver().insert(uri, cv);
    }

}
