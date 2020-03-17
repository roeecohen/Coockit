package com.example.coockit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchFragment extends Fragment {
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    SearchView mySearchView;
    ListView myListView;
    String searchInput;
    boolean isComma ;

  public SearchFragment(){}

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_search, container, false);
    searchInput = "";
    isComma = false;
    mySearchView = (SearchView)view.findViewById(R.id.searchView);

    myListView = (ListView)view.findViewById(R.id.search_item_list);
    list = new ArrayList<String>();
    list.addAll(Arrays.asList(getResources().getStringArray(R.array.ingredients)));

    adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);

    myListView.setAdapter(adapter);

    mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
          return false;
        }

        @Override
        public boolean onQueryTextChange(String str) {
            str = str.trim();
            searchInput=str;
            isComma = false;

            String[] myStrings = str.split(",");
            for (int i = 0; i < myStrings.length; i++)
                myStrings[i] = myStrings[i].trim();

            char[] chars = str.toCharArray();
            if(chars.length>0&&chars[chars.length-1]==',') {
                isComma = true;
            }

            if(isComma)
                adapter.getFilter().filter("");
            else if(myStrings.length>0)
                adapter.getFilter().filter(myStrings[myStrings.length-1]);
            else
                adapter.getFilter().filter(str);

            return false;
        }
      });

    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = myListView.getItemAtPosition(position).toString();

        int counter = lettersAfterComma();

        if(isComma)
          mySearchView.setQuery(searchInput+str, false);
        else if(counter>0&&str.length() > 0)
            mySearchView.setQuery(searchInput.substring(0, searchInput.length() - counter)+str, false);
        else
            mySearchView.setQuery(str, false);

      }
    });
    return view;
  }

    private int lettersAfterComma()
    {
        char[] chars = searchInput.toCharArray();
        int counter=0;
        boolean isComma=false;
        if(chars.length>0 && chars[chars.length-1]!=',')
            for(int i=chars.length-1;i>=0;i--) {
                if (chars[i] == ',')
                {
                    isComma=true;
                    break;
                }
                ++counter;
            }
        return isComma?counter:0;
    }
}
