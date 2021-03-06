package com.example.amit.dictionary;

/**
 * Created by AMIT YADAV
 * 11/10/18
 */


import android.app.Activity;
import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.amit.dictionary.model.SearchResult;
import com.example.amit.dictionary.networking.OxfordApi;
import com.example.amit.dictionary.utilities.WodWidget;
import com.google.android.gms.ads.MobileAds;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DictionaryApplication extends Application {

    public static final String TAG = DictionaryApplication.class
            .getSimpleName();

    private static DictionaryApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        MobileAds.initialize(this, BuildConfig.APP_AD_KEY);
    }

    public static synchronized DictionaryApplication getInstance() {
        return mInstance;
    }

    /**
     * @param activity The activity in which we have to show currently data is loading
     *                 Activity should must have included layout loading.xml in its layout.
     */
    public void showLoadingStarted(Activity activity) {
        activity.findViewById(R.id.content_view).setVisibility(View.GONE);
        activity.findViewById(R.id.loading_fail_ll).setVisibility(View.GONE);
        activity.findViewById(R.id.loading_empty_ll).setVisibility(View.GONE);
        activity.findViewById(R.id.loading_ll).setVisibility(View.VISIBLE);
    }

    /**
     * @param activity The activity in which we have to show data has loaded
     *                 Activity should must have included layout loading.xml in its layout.
     */
    public void showLoadingStopped(Activity activity) {
        activity.findViewById(R.id.loading_fail_ll).setVisibility(View.GONE);
        activity.findViewById(R.id.loading_empty_ll).setVisibility(View.GONE);
        activity.findViewById(R.id.loading_ll).setVisibility(View.GONE);
        activity.findViewById(R.id.content_view).setVisibility(View.VISIBLE);
    }

    /**
     * @param activity The activity in which we have to show currently data failed to load
     *                 Activity should must have included layout loading.xml in its layout.
     */
    public void showLoadFailed(Activity activity) {
        activity.findViewById(R.id.content_view).setVisibility(View.GONE);
        activity.findViewById(R.id.loading_empty_ll).setVisibility(View.GONE);
        activity.findViewById(R.id.loading_ll).setVisibility(View.GONE);
        activity.findViewById(R.id.loading_fail_ll).setVisibility(View.VISIBLE);
    }

    public void showLoadedEmpty(Activity activity) {
        activity.findViewById(R.id.content_view).setVisibility(View.GONE);
        activity.findViewById(R.id.loading_fail_ll).setVisibility(View.GONE);
        activity.findViewById(R.id.loading_ll).setVisibility(View.GONE);
        activity.findViewById(R.id.loading_empty_ll).setVisibility(View.VISIBLE);

    }

    public void setState(LOADING_STATE state, Activity activity) {
        switch (state) {
            case LOAD_STARTED:
                showLoadingStarted(activity);
                break;
            case LOAD_STOPPED:
                showLoadingStopped(activity);
                break;
            case LOAD_FAILED:
                showLoadFailed(activity);
                break;
            case LOAD_EMPTY:
                showLoadedEmpty(activity);
                break;
        }
    }

    public static final String LOADING_STATE_KEY = "LOADING_STATE_KEY";

    public enum LOADING_STATE {
        LOAD_STARTED, LOAD_STOPPED, LOAD_FAILED, LOAD_EMPTY,
    }

    public static void upDateWidgetsIfAny(@NonNull Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] widgetIds = manager.getAppWidgetIds(new ComponentName(context, WodWidget.class));
        if (widgetIds == null || widgetIds.length <= 0)
            return;
        for (int widgetId : widgetIds)
            WodWidget.updateWodWidget(context, manager, widgetId);
    }
}
