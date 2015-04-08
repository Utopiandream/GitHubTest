package com.example.apothecaryrdy.project1_instagood.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.apothecaryrdy.project1_instagood.MainActivity;
import com.example.apothecaryrdy.project1_instagood.R;


public class RequestHotImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_image);
        findViewById(R.id.btnGoBack).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(RequestHotImageActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };


}
