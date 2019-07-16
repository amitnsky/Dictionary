package com.example.amit.dictionary.activities;

/**
 * Created by AMIT YADAV
 * 14/10/18
 */

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.amit.dictionary.DictionaryApplication;
import com.example.amit.dictionary.adapters.CursorRecyclerAdapter;
import com.example.amit.dictionary.data.DictContract;
import com.example.amit.dictionary.data.DictContract.DictEntry;
import com.example.amit.dictionary.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>, CursorRecyclerAdapter.OnListItemClickListener {
    private final String LOG_TAG = HistoryActivity.class.getSimpleName();
    CursorRecyclerAdapter mAdapter;

    @BindView(R.id.history_recycler_view)
    RecyclerView mRecyclerView;

    final String[] PROJECTION = new String[]{DictEntry._ID, DictContract.DictEntry.WORD};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ButterKnife.bind(this);

        mAdapter = new CursorRecyclerAdapter(this, R.drawable.ic_history_black_24dp, R.drawable.ic_delete_sweep_black_24dp);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button button = findViewById(R.id.search_something_bv);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this, DrawerListener.class);
                startActivity(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                getContentResolver().delete(DictEntry.HISTORY_CONTENT_URI,
                        DictEntry._ID + " =?", new String[]{String.valueOf(viewHolder.itemView.getTag())});
            }
        }).attachToRecyclerView(mRecyclerView);
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
        DictionaryApplication.getInstance().setState(DictionaryApplication.LOADING_STATE.LOAD_STARTED, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return mAdapter.getItemCount() > 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history_menu_delete_all:
                showDeleteDialog();
                return true;
            case R.id.history_menu_sort_by:
                showSortPopUp();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //show popup delete diolog for delete all menu
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_all);
        builder.setMessage(R.string.clear_history_msg);
        builder.setPositiveButton(R.string.clear, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getContentResolver().delete(DictEntry.HISTORY_CONTENT_URI, null, null);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //show a pop up for sort by menu
    private void showSortPopUp() {
        SharedPreferences preference = this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preference.edit();
        int position = preference.getInt(getString(R.string.history_sort_key), 0);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.sort_order).setSingleChoiceItems(R.array.history_sort_by_label,
                position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        editor.putInt(getString(R.string.history_sort_key), position);
                        editor.apply();
                    }
                }
        );
        builder.setPositiveButton(getString(R.string.yes_do), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DictionaryApplication.getInstance().setState(DictionaryApplication.LOADING_STATE.LOAD_STARTED, HistoryActivity.this);
                getSupportLoaderManager().restartLoader(0, null, HistoryActivity.this);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    @NonNull
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = DictContract.DictEntry._ID + " DESC";
        return new android.support.v4.content.CursorLoader(this, DictContract.DictEntry.HISTORY_CONTENT_URI,
                PROJECTION, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        if (!cursor.moveToFirst()) {
            DictionaryApplication.getInstance().setState(DictionaryApplication.LOADING_STATE.LOAD_EMPTY, this);
            return;
        }
        DictionaryApplication.getInstance().setState(DictionaryApplication.LOADING_STATE.LOAD_STOPPED, this);
        mAdapter.swapCursor(cursor);
        invalidateOptionsMenu();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        mAdapter = null;
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void onClick(String text) {
        Intent intent = new Intent(this, WordActivity.class);
        intent.putExtra(DictEntry.WORD_ID, text);
        startActivity(intent);
    }
}
