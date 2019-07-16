package com.example.amit.dictionary.fragments;
/**
 * Created by AMIT YADAV
 * 14/10/18
 */
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.amit.dictionary.DictionaryApplication;
import com.example.amit.dictionary.activities.SearchActivity;
import com.example.amit.dictionary.activities.WordActivity;
import com.example.amit.dictionary.adapters.CursorRecyclerAdapter;
import com.example.amit.dictionary.data.DictContract;
import com.example.amit.dictionary.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DictionaryFragment extends Fragment
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>,
        CursorRecyclerAdapter.OnListItemClickListener {


    @BindView(R.id.history_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.content_view)
    ViewGroup contentView;
    @BindView(R.id.loading_fail_ll)
    ViewGroup failedLL;
    @BindView(R.id.loading_empty_ll)
    ViewGroup emptyLL;
    @BindView(R.id.loading_ll)
    ViewGroup loadingLL;

    CursorRecyclerAdapter mAdapter;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_history, container, false);
        ButterKnife.bind(this, rootView);

        mAdapter = new CursorRecyclerAdapter(this, R.drawable.ic_search_black_24dp, -1);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setState(DictionaryApplication.LOADING_STATE.LOAD_STARTED);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.getLoaderManager().initLoader(124, null, this).forceLoad();
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new android.support.v4.content.CursorLoader(getContext(), DictContract.DictEntry.SEARCH_CONTENT_URI,
                new String[]{DictContract.DictEntry._ID,
                        DictContract.DictEntry.WORD_ID,
                        DictContract.DictEntry.WORD},
                null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        // Toast.makeText(getContext(), "Loaded " + cursor.getCount(), Toast.LENGTH_SHORT).show();
        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            setState(DictionaryApplication.LOADING_STATE.LOAD_STOPPED);
            mAdapter.swapCursor(cursor);
        } else {
            setState(DictionaryApplication.LOADING_STATE.LOAD_EMPTY);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void onClick(String wordId) {
        Intent intent = new Intent(getContext(), WordActivity.class);
        intent.putExtra(DictContract.DictEntry.WORD_ID, wordId);
        startActivity(intent);
    }

    void setState(DictionaryApplication.LOADING_STATE state) {
        switch (state) {
            case LOAD_EMPTY:
                contentView.setVisibility(View.GONE);
                failedLL.setVisibility(View.GONE);
                loadingLL.setVisibility(View.GONE);
                emptyLL.setVisibility(View.VISIBLE);
                break;
            case LOAD_STARTED:
                contentView.setVisibility(View.GONE);
                failedLL.setVisibility(View.GONE);
                emptyLL.setVisibility(View.GONE);
                loadingLL.setVisibility(View.VISIBLE);
                break;
            case LOAD_STOPPED:
                failedLL.setVisibility(View.GONE);
                emptyLL.setVisibility(View.GONE);
                loadingLL.setVisibility(View.GONE);
                contentView.setVisibility(View.VISIBLE);
                break;
        }
    }
}
