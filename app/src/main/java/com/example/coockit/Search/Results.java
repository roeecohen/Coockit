package com.example.coockit.Search;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.coockit.Profile.UploadRecipe;
import com.example.coockit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Results {

    private RecyclerView recyclerView;
    private static ArrayList<String> mTitles;
    private static ArrayList<String> mUrls;
    private static ArrayList<String> mClickUrls;
    private static ArrayList<String> mIngredients;
    private static ArrayList<String> mPictures;
    private RequestQueue mQueue;
    private Context mContext;
    private Activity mActivity;
    private String mSearchInput;

    public interface VolleyCallBack {
        void onSuccess();
    }

    public Results(Activity act, Context con, View view, String searchIn) {
        mActivity = act;
        mContext = con;
        mSearchInput = searchIn;
        mTitles = new ArrayList<String>();
        mUrls = new ArrayList<String>();
        mClickUrls = new ArrayList<String>();
        mIngredients = new ArrayList<String>();
        mPictures = new ArrayList<String>();
        recyclerView =(RecyclerView)view.findViewById(R.id.new_recycler);
        recyclerView.setHasFixedSize(true);
        mQueue = Volley.newRequestQueue(mContext);

        jsonParse(new Results.VolleyCallBack() {

            @Override
            public void onSuccess() {
                setAdapter();
            }
        });
    }

    private void setAdapter()
    {
        SearchAdapter myAdapter = new SearchAdapter(mContext,mActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(myAdapter);
    }
    public void jsonParse(final Results.VolleyCallBack callBack)
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

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject recipe = jsonArray.getJSONObject(i);
                                    if(!isBlackList(recipe.getString("href"))) {
                                        mTitles.add(recipe.getString("title"));
                                        addWebsiteName(recipe.getString("href"));
                                        mClickUrls.add(recipe.getString("href"));
                                        mIngredients.add(recipe.getString("ingredients"));
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

    private void addWebsiteName(String url) {
        mUrls.add(cutUrl(url));
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
}
