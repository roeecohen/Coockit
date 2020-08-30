package com.example.coockit.Search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coockit.R;

/**
 * in the web results tab in the app all the recipes from the internet we be shown in the tab
 */
public class InternetResultsTab extends Fragment {
    private static RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_internet_results_tab, container, false);
        mRecyclerView =(RecyclerView)view.findViewById(R.id.search_results_recycler);
        mRecyclerView.setHasFixedSize(true);
        return view;
    }

    public static RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

}
