package com.example.coockit.Search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.coockit.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * in this class we get all recipes that match the ingredients that the user inserted the app
 * the recipes are from the website recipe puppy
 */

public class InternetResults {

    private static ArrayList<String> mTitles;
    private static ArrayList<String> mUrls;
    private static ArrayList<String> mClickUrls;
    private static ArrayList<String> mIngredients;
    private static ArrayList<String> mPictures;

    private static Set<String> mCheckBoxOptions;

    private RequestQueue mQueue;
    private Context mContext;
    private Activity mActivity;
    private String mSearchInput;
    private IngredientsOptionsAdapter mIngOptionsAdapter;
    private RecyclerView mIngOptionsRecycler;

    public interface VolleyCallBack {
        void onSuccess();
    }

    public InternetResults(Activity act, Context con, final View view, String searchIn) {
        mActivity = act;
        mContext = con;
        mSearchInput = searchIn;
        mTitles = new ArrayList<String>();
        mUrls = new ArrayList<String>();
        mClickUrls = new ArrayList<String>();
        mIngredients = new ArrayList<String>();
        mPictures = new ArrayList<String>();

        mCheckBoxOptions = new HashSet<String>();
        mIngOptionsRecycler = view.findViewById(R.id.ingred_options_recycler);

        mQueue = Volley.newRequestQueue(mContext);

        jsonParse(new InternetResults.VolleyCallBack() {

            @Override
            public void onSuccess() {
                checkForMissingIngredients();

                mIngOptionsAdapter = new IngredientsOptionsAdapter(mContext,view);

                mIngOptionsRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                mIngOptionsRecycler.setAdapter(mIngOptionsAdapter);
                setAdapter();
            }
        });
    }

    private void setAdapter()
    {
        SearchAdapter myAdapter = new SearchAdapter();
        InternetResultsTab.getmRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        InternetResultsTab.getmRecyclerView().setAdapter(myAdapter);
    }
    public void jsonParse(final InternetResults.VolleyCallBack callBack)
    {
        try {
            //try catch for this exception
            String ingredients = URLDecoder.decode(mSearchInput, "UTF-8");
            String url = "http://www.recipepuppy.com/api/?i="+ingredients;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("results");

                                if(jsonArray.length()==0)
                                    Toast.makeText(mContext,"No recipes found! please try different ingredients",Toast.LENGTH_SHORT).show();
                                else
                                {
                                    SearchFragment.getmOptions().setVisibility(View.VISIBLE);
                                }

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject recipe = jsonArray.getJSONObject(i);
                                    if(!isBlackList(recipe.getString("href"))) {
                                        mTitles.add(recipe.getString("title").trim());
                                        mUrls.add(cutUrl(recipe.getString("href").trim()));
                                        mClickUrls.add(recipe.getString("href").trim());
                                        mIngredients.add(recipe.getString("ingredients").trim());
                                        mPictures.add(recipe.getString("thumbnail"));
                                        callBack.onSuccess();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 is unknown");
        }
    }

    private boolean isBlackList(String href) {
        String result = cutUrl(href);
        if(result.equals("http://cookeatshare.com"))
            return true;
        return false;
    }

    private String cutUrl(String url) {
        int count=0;
        String result="";
        char urlArr[] = url.toCharArray();

        for (int i=0; i<url.length();i++)
        {
            if(urlArr[i]=='/')
                count++;

            if(count==3)
                break;

            result+=urlArr[i];
        }
        return result;
    }

    public static ArrayList<String> getmTitles() {
        return mTitles;
    }
    public static ArrayList<String> getmUrls() {
        return mUrls;
    }
    public static ArrayList<String> getmClickUrls() {
        return mClickUrls;
    }
    public static ArrayList<String> getmIngredients() {
        return mIngredients;
    }
    public static ArrayList<String> getmPictures() {
        return mPictures;
    }

    private void checkForMissingIngredients()
    {
        for (int i = 0; i < InternetResults.getmIngredients().size(); i++) {
            ArrayList<String> arrayIngredientsSearchResult = new ArrayList<>(Arrays.asList(InternetResults.getmIngredients().get(i).toLowerCase().trim().split(",")));
            ArrayList<String> arrayInput = new ArrayList<>(Arrays.asList(mSearchInput.toLowerCase().trim().split(",")));

            List<String> temp = new ArrayList<String>(arrayIngredientsSearchResult);
            temp.removeAll( arrayInput );
            mCheckBoxOptions.addAll(temp);
        }
    }
    public static Set<String> getmCheckBoxOptions() {
        return mCheckBoxOptions;
    }

    public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
        public SearchAdapter() { }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.search_result_web_item, parent, false);
            return new SearchViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {
            holder.title.setText((String) InternetResults.getmTitles().get(position));
            holder.url.setText((String) InternetResults.getmUrls().get(position));
            holder.ingredients.setText((String) InternetResults.getmIngredients().get(position));

            if(!InternetResults.getmPictures().get(position).isEmpty())
                Picasso.get().load((String) InternetResults.getmPictures().get(position)).into(holder.img);

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse((String) InternetResults.getmClickUrls().get(position)) );
                    mActivity.startActivity( browse );
                }
            });
        }

        @Override
        public int getItemCount() {
            return InternetResults.getmTitles().size();
        }

        public class SearchViewHolder extends RecyclerView.ViewHolder {
            private TextView title;
            private TextView url;
            private TextView ingredients;
            private ImageView img;
            private CardView card;

            public SearchViewHolder(@NonNull View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title2);
                url = (TextView) itemView.findViewById(R.id.url2);
                ingredients = (TextView) itemView.findViewById(R.id.ingredients2);
                img = (ImageView) itemView.findViewById(R.id.img2);
                card= (CardView) itemView.findViewById(R.id.cardView3);
            }
        }
    }
}
