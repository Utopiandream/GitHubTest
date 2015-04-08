package com.example.apothecaryrdy.project1_instagood.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.apothecaryrdy.project1_instagood.R;
import com.example.apothecaryrdy.project1_instagood.app.AppController;
import com.example.apothecaryrdy.project1_instagood.extensions.MyJsonObjectRequest;
import com.example.apothecaryrdy.project1_instagood.util.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RequestJsonActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_json);
        findViewById(R.id.btnRequestJsonObject).setOnClickListener(onClickListener);
        findViewById(R.id.btnRequestJsonArray).setOnClickListener(onClickListener);
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Request request = null;
            switch (v.getId()) {
                case R.id.btnRequestJsonObject:
                    request = new MyJsonObjectRequest(
                            AppConstants.URL_JSON_OBJECT, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    ((TextView) findViewById(R.id.textViewResponse))
                                            .setText(response.toString());
                                }
                            }, errorListener
                    );
                    break;
                case R.id.btnRequestJsonArray:
                    request = new JsonArrayRequest(
                            AppConstants.URL_JSON_ARRAY,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    ((TextView) findViewById(R.id.textViewResponse)).setText(response.toString());

                                }
                            }, errorListener){

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> pHeaders = super.getHeaders();
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("api-key", "API_KEY_GOES_HERE");
                            headers.put("User-Agent", "MY_USER_AGENT");
					//        if (headers != null) //TODO copy all headers from pHeaders to headers
                            return headers;
                        }
                    };
                    break;
            }
            AppController.addRequestToQueue(request);
        }
    };

}
