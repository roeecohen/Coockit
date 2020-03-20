package com.example.coockit.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.coockit.Home.HomeSingleRecipe;
import com.example.coockit.Profile.ProfileRecipes;
import com.example.coockit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {

    private View mView;

    private Button mSearchBtn;
    private String mSearchInput;

    public SearchFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);

        SearchAutoComplete autoComplete = new SearchAutoComplete();
        autoComplete.listners();

        mSearchBtn = (Button) mView.findViewById(R.id.search_btn);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchResults.class);
                intent.putExtra("search_input", mSearchInput);
                startActivity(intent);
            }
        });

        return mView;
    }

    public class SearchAutoComplete {

        private ArrayList<String> mList;
        private ArrayAdapter<String> mAdapter;
        private SearchView mySearchView;
        private ListView myListView;
        private boolean mIsComma;

        public SearchAutoComplete() {
            mSearchInput = "";
            mIsComma = false;
            mySearchView = (SearchView) mView.findViewById(R.id.searchView);

            myListView = (ListView) mView.findViewById(R.id.search_item_list);
            mList = new ArrayList<String>();
            mList.addAll(Arrays.asList(getResources().getStringArray(R.array.ingredients)));

            mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mList);

            myListView.setAdapter(mAdapter);
        }

        public void listners() {
            mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String str) {
                    showFilters(str);
                    return false;
                }
            });

            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String str = myListView.getItemAtPosition(position).toString();

                    int counter = lettersAfterComma();

                    if (mIsComma)
                        mySearchView.setQuery(mSearchInput + str, false);
                    else if (counter > 0 && str.length() > 0)
                        mySearchView.setQuery(mSearchInput.substring(0, mSearchInput.length() - counter) + str, false);
                    else
                        mySearchView.setQuery(str, false);

                }
            });
        }

        private void showFilters(String str)
        {
            str = str.trim();
            mSearchInput = str;
            mIsComma = false;

            String[] myStrings = str.split(",");
            for (int i = 0; i < myStrings.length; i++)
                myStrings[i] = myStrings[i].trim();

            char[] chars = str.toCharArray();
            if (chars.length > 0 && chars[chars.length - 1] == ',') {
                mIsComma = true;
            }
            if (mIsComma)
                mAdapter.getFilter().filter("");
            else if (myStrings.length > 0)
                mAdapter.getFilter().filter(myStrings[myStrings.length - 1]);
            else
                mAdapter.getFilter().filter(str);
        }

        private int lettersAfterComma() {
            char[] chars = mSearchInput.toCharArray();
            int counter = 0;
            boolean isComma = false;
            if (chars.length > 0 && chars[chars.length - 1] != ',')
                for (int i = chars.length - 1; i >= 0; i--) {
                    if (chars[i] == ',') {
                        isComma = true;
                        break;
                    }
                    ++counter;
                }
            return isComma ? counter : 0;

        }
    }
}
