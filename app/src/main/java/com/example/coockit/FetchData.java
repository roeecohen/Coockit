//package com.example.coockit;
//
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.JsonObjectRequest;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class FetchData {
//    JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
//            null, new Response.Listener<JSONObject>() {
//
//        @Override
//        public void onResponse(JSONObject response) {
//            Log.d(tag, response.toString());
//            activity.hideDialog();
//            try {
//                activity.onRequestServed(response, code);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            VolleyLog.d(tag, "Error: " + error.getMessage());
//            Log.e(tag, "Site Info Error: " + error.getMessage());
//            Toast.makeText(activity.getApplicationContext(),
//                    error.getMessage(), Toast.LENGTH_SHORT).show();
//            activity.hideDialog();
//            try {
//                activity.onRequestServed(null, code);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }) {
//
//        /**
//         * Passing some request headers
//         */
//        @Override
//        public Map<String, String> getHeaders() throws AuthFailureError {
//            HashMap<String, String> headers = new HashMap<String, String>();
//            //headers.put("Content-Type", "application/json");
//            headers.put("key", "Value");
//            return headers;
//        }
//    };
//}
