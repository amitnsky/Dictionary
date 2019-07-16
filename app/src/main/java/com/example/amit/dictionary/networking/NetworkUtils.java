package com.example.amit.dictionary.networking;
/**
 * Created by AMIT YADAV
 * 11/10/18
 */

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.amit.dictionary.DictionaryApplication;
import com.example.amit.dictionary.R;
import com.example.amit.dictionary.model.SearchResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetworkUtils {


    public static void queryWord(String mSearchKey, Callback<SearchResult> resultCallback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OxfordApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OxfordApi api = retrofit.create(OxfordApi.class);

        Log.v("Base Url ", retrofit.baseUrl().toString());
        Call<SearchResult> call = api.getSearchResults(mSearchKey.toLowerCase());

        call.enqueue(resultCallback);
    }

    public static void queryDefinitions(String mWordId, Callback<String> resultCallback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OxfordApi.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        OxfordApi api = retrofit.create(OxfordApi.class);

        Call<String> call = api.getDefinitions(mWordId);

        call.enqueue(resultCallback);
    }

    public static void queryWordOfDay(Uri url, Callback<String> resultCallback) {
        Log.d("Base url: ","Scheme: "+ url.getScheme() + " Author: " + url.getAuthority());
        Log.d("Path url: ",url.getPath());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        OxfordApi api = retrofit.create(OxfordApi.class);
        Call<String> call = api.getWordOfDay(url.toString());
        call.enqueue(resultCallback);
    }

    //helper method to monitor network status
    public static boolean checkConnectivity(ConnectivityManager mConnectivityManager) {
        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
