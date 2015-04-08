package com.example.apothecaryrdy.project1_instagood.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.apothecaryrdy.project1_instagood.R;
import com.example.apothecaryrdy.project1_instagood.app.AppController;
import com.example.apothecaryrdy.project1_instagood.util.AppConstants;

import java.util.HashMap;
import java.util.Map;


public class RequestStringActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_string);
        findViewById(R.id.btnRequestString).setOnClickListener(onClickListner);
        findViewById(R.id.btnRequestPostString).setOnClickListener(onClickListner);
    }

    View.OnClickListener onClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int type = Request.Method.GET;
            switch (v.getId()) {
                case R.id.btnRequestString:
                    type = Request.Method.GET;
                    break;
                case R.id.btnRequestPostString:
                    type = Request.Method.POST;
                    break;
            }
            StringRequest stringRequest = new StringRequest(
                    type,
                    AppConstants.URL_STRING,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ((TextView) findViewById(R.id.textViewResponse)).setText(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ((TextView) findViewById(R.id.textViewResponse)).setText(error.getMessage());
                        }
                    }
            ){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> pParams = super.getParams();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api-key", "API_KEY_GOES_HERE");
                    params.put("User-Agent", "MY_USER_AGENT");
//        if (params != null) //TODO copy all params from pParams to params
                    return params;
                }
            };
            AppController.addRequestToQueue(stringRequest);
        }
    };

}
