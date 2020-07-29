package com.example.coockit.Search;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.coockit.Main.MainActivity;
import com.example.coockit.R;
import com.example.coockit.SMS.SendMessage;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {

    private View mView;
    private ListView mListView;
    private Button mSearchBtn;
    private static String mSearchInput;
    private static CardView mOptions;
    private static Button mCheckAroundBtn;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public SearchFragment() { }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search, container, false);

        mViewPager = (ViewPager) mView.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) mView.findViewById(R.id.result_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        SearchAutoComplete autoComplete = new SearchAutoComplete();
        autoComplete.listners();

        mTabLayout = (TabLayout) mView.findViewById(R.id.result_tabs);
        mSearchBtn = (Button) mView.findViewById(R.id.search_btn);
        mListView = (ListView) mView.findViewById(R.id.search_item_list);
        mOptions = (CardView) mView.findViewById(R.id.check_box_card);

        mCheckAroundBtn = (Button)mView.findViewById(R.id.check_around_btn);
        mCheckAroundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = (MainActivity) getActivity();
                SendMessage sendMessage = new SendMessage();
                sendMessage.onSend(v, mCheckAroundBtn ,main);
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibilties();
                new InternetResults(getActivity(),getActivity().getApplicationContext(),mView,mSearchInput);
                setupViewPager();

            }
        });
        return mView;
    }

    // Add Fragments to Tabs
    private void setupViewPager() {
        Adapter adapter = new Adapter(getChildFragmentManager(),1);
        adapter.addFragment(new InternetResultsTab(), "Web Results");
        adapter.addFragment(new FirebaseResultsTab(), "App Results");
        mViewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
                    setVisibilties();
                    new InternetResults(getActivity(),getActivity().getApplicationContext(),mView,mSearchInput);
                    setupViewPager();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String str) {
                    mListView.setVisibility(View.VISIBLE);
                    mViewPager.setVisibility(View.GONE);
                    mOptions.setVisibility(View.GONE);
                    mTabLayout.setVisibility(View.GONE);

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
    private void setVisibilties() {
        mListView.setVisibility(View.GONE);
        mCheckAroundBtn.setVisibility(View.GONE);
        mViewPager.setVisibility(View.VISIBLE);
        mTabLayout.setVisibility(View.VISIBLE);
    }
    public static Button getmCheckAroundBtn() {
        return mCheckAroundBtn;
    }
    public static CardView getmOptions() {
        return mOptions;
    }
    public static String getmSearchInput() {
        return mSearchInput;
    }
}
