package com.example.apothecaryrdy.project1_instagood.extensions;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 3/10/2015.
 */
public class MyJsonObjectRequest extends JsonObjectRequest {
    public MyJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public MyJsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> pHeaders = super.getHeaders();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("api-key", "API_KEY_GOES_HERE");
        headers.put("User-Agent", "MY_USER_AGENT");
//        if (headers != null) //TODO copy all headers from pHeaders to headers
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> pParams = super.getParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("api-key", "API_KEY_GOES_HERE");
        params.put("User-Agent", "MY_USER_AGENT");
//        if (params != null) //TODO copy all params from pParams to params
        return params;
    }
}
