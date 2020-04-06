//package com.example.coockit.Search;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.example.coockit.R;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class SearchResults extends AppCompatActivity {
//
//    private  RecyclerView recyclerView;
//    private static ArrayList<String> mTitles;
//    private static ArrayList<String> mUrls;
//    private static ArrayList<String> mClickUrls;
//    private static ArrayList<String> mIngredients;
//    private static ArrayList<String> mPictures;
//    private RequestQueue mQueue;
//
//    public interface VolleyCallBack {
//        void onSuccess();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_results);
//
//        mTitles = new ArrayList<String>();
//        mUrls = new ArrayList<String>();
//        mClickUrls = new ArrayList<String>();
//        mIngredients = new ArrayList<String>();
//        mPictures = new ArrayList<String>();
//        recyclerView =(RecyclerView)findViewById(R.id.new_recycler);
//        recyclerView.setHasFixedSize(true);
//        mQueue = Volley.newRequestQueue(this);
//
//        jsonParse(new VolleyCallBack() {
//
//            @Override
//            public void onSuccess() {
//                setAdapter();
//
//            }
//        });
//    }
//
//    private void setAdapter()
//    {
//        SearchAdapter myAdapter = new SearchAdapter(getIntent(),this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        recyclerView.setAdapter(myAdapter);
//    }
//    public void jsonParse(final VolleyCallBack callBack)
//    {
//
//        try {
//            //try catch for this exception
//            String ingredients = URLDecoder.decode(getIntent().getStringExtra("search_input"), "UTF-8");
//
//            String url = "https://recipe-puppy.p.rapidapi.com/?i="+ingredients;
//
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                JSONArray jsonArray = response.getJSONArray("results");
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject recipe = jsonArray.getJSONObject(i);
//
//                                    mTitles.add(recipe.getString("title"));
//                                    addWebsiteName(recipe.getString("href"));
//                                    mClickUrls.add(recipe.getString("href"));
//                                    mIngredients.add(recipe.getString("ingredients"));
//                                    mPictures.add(recipe.getString("thumbnail"));
//                                    Log.d("Debug2", mTitles.get(i));
//                                    callBack.onSuccess();
//                                    // mTitle.append(title + "\n");
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    error.printStackTrace();
//                }
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("x-rapidapi-host", "recipe-puppy.p.rapidapi.com");
//                    params.put("x-rapidapi-key", "a5e0c90d95msh61c8593b87e07bfp1130c5jsn287715fa66bc");
//
//                    return params;
//                }
//            };
//            mQueue.add(request);
//
//        } catch (UnsupportedEncodingException e) {
//            throw new AssertionError("UTF-8 is unknown");
//        }
//    }
//
//    private void addWebsiteName(String url) {
//        int count=0;
//        char urlArr[] = url.toCharArray();
//        String result="";
//
//        for (int i=0; i<url.length();i++)
//        {
//            if(urlArr[i]=='/')
//                count++;
//
//            if(count==3)
//                break;
//
//            result+=urlArr[i];
//        }
//        mUrls.add(result);
//
//    }
//
//    public static ArrayList<String> getmTitles() {
//        return mTitles;
//    }
//    public static ArrayList<String> getmUrls() {
//        return mUrls;
//    }
//    public static ArrayList<String> getmClickUrls() {
//        return mClickUrls;
//    }
//    public static ArrayList<String> getmIngredients() {
//        return mIngredients;
//    }
//    public static ArrayList<String> getmPictures() {
//        return mPictures;
//    }
//}
