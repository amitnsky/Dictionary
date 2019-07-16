package com.example.amit.dictionary.fragments;
/**
 * Created by AMIT YADAV
 * 14/10/18
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.amit.dictionary.adapters.FavouriteCursorAdapter;
import com.example.amit.dictionary.activities.WordActivity;
import com.example.amit.dictionary.data.DictContract;
import com.example.amit.dictionary.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {
    private final String LOG_TAG = FavouriteFragment.class.getSimpleName();

    FavouriteCursorAdapter mAdapter;

    @BindView(R.id.history_list_view)
    ListView mListView;

    final String[] PROJECTION = new String[]{
            DictContract.DictEntry._ID,
            DictContract.DictEntry.WORD_ID,
            DictContract.DictEntry.WORD,
            DictContract.DictEntry.LANGUAGE,
            DictContract.DictEntry.PRONNUNCIATION_TEXT,
            DictContract.DictEntry.SINGLE_DEFINITION,
    };
    private static final int SORT_BY_SEARCH = 0;
    private static final int SORT_BY_WORD = 1;
    SharedPreferences mSharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fav, container, false);
        ButterKnife.bind(this, rootView);

        mAdapter = new FavouriteCursorAdapter(getContext(), null, 0, DictContract.DictEntry.FAVOURITE_CONTENT_URI);
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(2);
        mListView.setEmptyView(rootView.findViewById(R.id.history_empty_view));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), WordActivity.class);
                intent.putExtra(DictContract.DictEntry.WORD_ID, view.getTag().toString());
                startActivity(intent);
            }
        });

        Button button = rootView.findViewById(R.id.hitory_search_something);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchView searchView = getActivity().findViewById(R.id.search_view);
                searchView.requestFocus();
            }
        });
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        getLoaderManager().initLoader(0, null, this).forceLoad();
        return rootView;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortBy = mSharedPreferences.getString(getString(R.string.setting_fav_sort_key),
                getString(R.string.setting_fav_sort_default_val));
        int sortCode = 0;
        if (sortBy.equals(getString(R.string.setting_fav_sort_save_val)))
            sortCode = SORT_BY_SEARCH;
        else sortCode = SORT_BY_WORD;
        String sortOrder = null;
        switch (sortCode) {
            case SORT_BY_SEARCH:
                sortOrder = DictContract.DictEntry._ID + " COLLATE NOCASE" + " DESC";
                break;
            case SORT_BY_WORD:
                sortOrder = DictContract.DictEntry.WORD + " COLLATE NOCASE" + " ASC";
                break;
        }
        return new android.support.v4.content.CursorLoader(getActivity().getApplicationContext(), DictContract.DictEntry.FAVOURITE_CONTENT_URI,
                PROJECTION, null, null, sortOrder);

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        mAdapter = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSharedPreferences != null)
            mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.setting_fav_sort_key)))
            getLoaderManager().restartLoader(0, null, this);
    }

}
