package com.example.apothecaryrdy.project1_instagood.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.apothecaryrdy.project1_instagood.app.AppController;
import com.example.apothecaryrdy.project1_instagood.util.AppConstants;
import com.example.apothecaryrdy.project1_instagood.R;


public class RequestImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_image);
        findViewById(R.id.btnGoBack).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Retrieves an image specified by the URL, displays it in the UI.
            ImageRequest request = new ImageRequest(AppConstants.URL_IMAGE,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            ((ImageView) findViewById(R.id.imageViewer)).setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
//                            ((ImageView)findViewById(R.id.imageView)).setImageResource(R.drawable.);
                        }
                    });
            AppController.addRequestToQueue(request);
        }

    };

}
