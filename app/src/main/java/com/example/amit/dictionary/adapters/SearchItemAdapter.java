package com.example.amit.dictionary.adapters;

/**
 * Created by AMIT YADAV
 * 8/10/18
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amit.dictionary.model.SearchResult.QueryResult;
import com.example.amit.dictionary.R;

import java.util.ArrayList;

public class SearchItemAdapter extends ArrayAdapter<QueryResult> {

    public SearchItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<QueryResult> objects) {
        super(context, resource, objects);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rootView=convertView;
        if (convertView==null){
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.search_items,parent,false);
        }
        TextView wordTextView = rootView.findViewById(R.id.search_items_word);
        wordTextView.setText(getItem(position).word);
        ImageView delView = rootView.findViewById(R.id.search_items_action);
        delView.setVisibility(View.GONE);
        return rootView;
    }
}
