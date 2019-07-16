package com.example.amit.dictionary.networking;

/**
 * Created by AMIT YADAV
 * 11/10/18
 */


import com.example.amit.dictionary.BuildConfig;
import com.example.amit.dictionary.model.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface OxfordApi {

    String APP_KEY = BuildConfig.APP_KEY;
    String APP_KEY_NAME = "app_key";
    String APP_ID = BuildConfig.APP_ID;
    String APP_ID_NAME = "app_id";

    static final String BASE_URL = "https://od-api.oxforddictionaries.com/api/v2/";

    /**
     * for a word match query the url should be like
     * ENTRY_BASE_URL + language + "/" + word_id;
     */
    static final String ENDPOINT_ENTRIES = "entries";

    /**
     * for a word match query the url should be like
     * SEARCH_BASE_URL + language + "?q=" + word_id;
     */
    static final String ENDPOINT_SEARCH = "search";

    static String DEFAULT_LANG = "en-us";

    static final String OXFORD_HEADER_ACCEPT = "Accept:application/json";

    static final String OXFORD_HEADER_KEY = OxfordApi.APP_KEY_NAME + ":" + OxfordApi.APP_KEY;

    static final String OXFORD_HEADER_ID = OxfordApi.APP_ID_NAME + ":" + OxfordApi.APP_ID;

    //method for matching words query from given language
    @Headers({
            OXFORD_HEADER_ACCEPT, OXFORD_HEADER_ID, OXFORD_HEADER_KEY
    })
    @GET(ENDPOINT_SEARCH + "/{lang}")
    Call<SearchResult> getSearchResults(@Path("lang") String lang, @Query("q") String word);

    //method for matching words query from default language
    @Headers({
            OXFORD_HEADER_ACCEPT, OXFORD_HEADER_ID, OXFORD_HEADER_KEY
    })
    @GET(ENDPOINT_SEARCH + "/" + DEFAULT_LANG)
    Call<SearchResult> getSearchResults(@Query("q") String word);

    //"https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    //method for matching words query from given language
    @Headers({
            OXFORD_HEADER_ACCEPT, OXFORD_HEADER_ID, OXFORD_HEADER_KEY
    })
    @GET(ENDPOINT_ENTRIES + "/{lang}/{keyword}")
    Call<String> getDefinitions(@Path("lang") String lang, @Path("keyword") String word_id);

    //method for matching words query from default language
    @Headers({
            OXFORD_HEADER_ACCEPT, OXFORD_HEADER_ID, OXFORD_HEADER_KEY
    })
    @GET(ENDPOINT_ENTRIES + "/" + DEFAULT_LANG + "/{keyword}")
    Call<String> getDefinitions(@Path("keyword") String word_id);


    //method for getting word of day
    @GET
    Call<String> getWordOfDay(@Url String url);

}
