package com.example.apothecaryrdy.project1_instagood.app;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by User on 3/5/2015.
 */
public class AppController extends Application {
    private static AppController instance;
    private static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AppController i() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        return requestQueue;
    }

    public static void addRequestToQueue(Request request) {
        i().getRequestQueue().add(request);
    }
}
