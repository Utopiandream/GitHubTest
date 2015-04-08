package com.example.apothecaryrdy.project1_instagood.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apothecaryrdy.project1_instagood.MainActivity;
import com.example.apothecaryrdy.project1_instagood.PhotoListAdapter;
import com.example.apothecaryrdy.project1_instagood.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramRequest;
import net.londatiga.android.instagram.InstagramSession;
import net.londatiga.android.instagram.InstagramUser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class RequestHotImageActivity extends MainActivity {

    private InstagramSession mInstagramSession;
    private Instagram mInstagram;
    protected GridView mGridViewr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_image);
        findViewById(R.id.btnGoBack).setOnClickListener(onClickListener);
        mInstagram = new Instagram(this, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
        mInstagramSession = mInstagram.getSession();
        mGridViewr   = (GridView) findViewById(R.id.mGridView);
        if (mInstagramSession.isActive()) {
            InstagramUser instagramUser = mInstagramSession.getUser();
            //this is to get image
            ImageView userIv = (ImageView) findViewById(R.id.imageViewer);

            DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_user)
                    .showImageForEmptyUri(R.drawable.ic_user)
                    .showImageOnFail(R.drawable.ic_user)
                    .considerExifParams(true)
                    .build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                    .writeDebugLogs()
                    .defaultDisplayImageOptions(displayOptions)
                    .build();

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);

            AnimateFirstDisplayListener animate = new AnimateFirstDisplayListener();

            imageLoader.displayImage(instagramUser.profilPicture, userIv, animate);
            new DownloadTask().execute();

        }


    } //End of On Create

    public class DownloadTask extends AsyncTask<URL, Integer, Long> {
        ArrayList<String> photoList;

        protected void onCancelled() {

        }

        protected void onPreExecute() {

        }

        //This method gets the feed
        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>(1);
                InstagramUser instagramUser = mInstagramSession.getUser();
                params.add(new BasicNameValuePair("count", "10"));

                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
                String response			 = request.createRequest("GET", "/users/" + instagramUser.id + "/media/recent/", params);
                //https://api.instagram.com/v1/users/{user-id}/media/recent/?access_token=ACCESS-TOKEN
                //String response	= request.createRequest("GET", "/users/self/feed", params);
                if (!response.equals("")) {
                    JSONObject jsonObj  = (JSONObject) new JSONTokener(response).nextValue();
                    JSONArray jsonData	= jsonObj.getJSONArray("data");

                    int length = jsonData.length();

                    if (length > 0) {
                        photoList = new ArrayList<String>();

                        for (int i = 0; i < length; i++) {
                            JSONObject jsonPhoto = jsonData.getJSONObject(i).getJSONObject("images").getJSONObject("low_resolution");

                            photoList.add(jsonPhoto.getString("url"));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            //mLoadingPb.setVisibility(View.GONE);

            if (photoList == null) {
                Toast.makeText(getApplicationContext(), "No Photos Available", Toast.LENGTH_LONG).show();
            } else {
                DisplayMetrics dm = new DisplayMetrics();

                getWindowManager().getDefaultDisplay().getMetrics(dm);

                int width 	= (int) Math.ceil((double) dm.widthPixels / 2);
                width=width-50;
                int height	= width;

                PhotoListAdapter adapter = new PhotoListAdapter(RequestHotImageActivity.this);

                adapter.setData(photoList);
                adapter.setLayoutParam(width, height);

                mGridViewr.setAdapter(adapter);
            }
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(RequestHotImageActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };





}
