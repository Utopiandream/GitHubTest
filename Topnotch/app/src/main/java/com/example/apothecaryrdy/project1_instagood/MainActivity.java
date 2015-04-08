package com.example.apothecaryrdy.project1_instagood;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apothecaryrdy.project1_instagood.activities.RequestHotImageActivity;
import com.example.apothecaryrdy.project1_instagood.activities.RequestNotImageActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {
    // name of SharedPreferences XML file that stores the saved searches
    private static final String SEARCHES = "searches";
    private static final NumberFormat intformat = NumberFormat.getIntegerInstance();

    private EditText queryEditText; // EditText where user enters a query
    private EditText tagEditText; // EditText where user tags a query
    private SharedPreferences savedSearches; // user's favorite searches
    private ArrayList<String> tags; // list of tags for saved searches
    private ArrayAdapter adapter; // binds tags to ListView

    private InstagramSession mInstagramSession;
    private Instagram mInstagram;

    protected ProgressBar mLoadingPb;
    protected GridView mGridView;

    protected static final String CLIENT_ID = "994ee47a2ec54f049b58bf7d9a3bb602";
    protected static final String CLIENT_SECRET = "c1b5194be4ec46eca8706b15dc33321a";
    protected static final String REDIRECT_URI = "topnotchcustomscheme://oauth/callback/instagram/";

//add things for seekbar
    private int customWeek = 20; //initial week age of pictures
    private SeekBar.OnSeekBarChangeListener customSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            customWeek = progress;
            updateCustom();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void updateCustom() {
        weekCustomTextView.setText("Weeks To Look Back " + intformat.format(customWeek));
    }

    private TextView weekCustomTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the SharedPreferences containing the user's saved searches //TODO
        savedSearches = getSharedPreferences(SEARCHES, MODE_PRIVATE);
        tags = new ArrayList<String>(savedSearches.getAll().keySet());
        Collections.sort(tags, String.CASE_INSENSITIVE_ORDER);
        ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item, tags);

        ((Button) findViewById(R.id.btn_connect)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mInstagram.authorize(mAuthListener);
            }
        });

        mInstagram = new Instagram(this, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
        mInstagramSession = mInstagram.getSession();

       if (mInstagramSession.isActive()) {
           setContentView(R.layout.activity_user);
           InstagramUser instagramUser = mInstagramSession.getUser();

           findViewById(R.id.hotImageButton).setOnClickListener(onClickListener);
           findViewById(R.id.notImageButton).setOnClickListener(onClickListener);

                                 //Grab Name, Username, and Profile Pic
           ((TextView) findViewById(R.id.tv_name)).setText(instagramUser.fullName);
           ((TextView) findViewById(R.id.tv_username)).setText(instagramUser.username);
           ImageView userIv = (ImageView) findViewById(R.id.iv_user);

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

           //for seekerbar
           weekCustomTextView = (TextView) findViewById(R.id.weekCustomTextView);
           updateCustom();

           //set customWeekSeekBar OnSeekBarChangeListener
           SeekBar customWeekSeekBar = (SeekBar) findViewById(R.id.customWeekSeekBar);
           customWeekSeekBar.setOnSeekBarChangeListener(customSeekBarListener);


           ((Button) findViewById(R.id.btn_logout)).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View arg0) {
                   mInstagramSession.reset();

                   startActivity(new Intent(MainActivity.this, MainActivity.class));

                   finish();
               }
           });

       }   else {
           setContentView(R.layout.activity_main);

           ((Button) findViewById(R.id.btn_connect)).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View arg0) {
                   mInstagram.authorize(mAuthListener);
               }
           });
       }

    }//end method on create

    //Listeners for Hot Or Not Buttons
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.notImageButton:
                    intent = new Intent(MainActivity.this, RequestNotImageActivity.class);
                    break;
                case R.id.hotImageButton:
                    intent = new Intent(MainActivity.this, RequestHotImageActivity.class);
                    break;
            }
            startActivity(intent);
        }
    };


    //Bunch of required Instagram Methods

    private void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private Instagram.InstagramAuthListener mAuthListener = new Instagram.InstagramAuthListener() {
        @Override
        public void onSuccess(InstagramUser user) {
            finish();

            startActivity(new Intent(MainActivity.this, MainActivity.class));
        }

        @Override
        public void onError(String error) {
            showToast(error);
        }

        @Override
        public void onCancel() {
            showToast("OK. Maybe later?");

        }
    };

    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

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

                params.add(new BasicNameValuePair("count", "10"));

                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
                String response			 = request.createRequest("GET", "/users/self/feed", params);

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
            mLoadingPb.setVisibility(View.GONE);

            if (photoList == null) {
                Toast.makeText(getApplicationContext(), "No Photos Available", Toast.LENGTH_LONG).show();
            } else {
                DisplayMetrics dm = new DisplayMetrics();

                getWindowManager().getDefaultDisplay().getMetrics(dm);

                int width 	= (int) Math.ceil((double) dm.widthPixels / 2);
                width=width-50;
                int height	= width;

                PhotoListAdapter adapter = new PhotoListAdapter(MainActivity.this);

                adapter.setData(photoList);
                adapter.setLayoutParam(width, height);

                mGridView.setAdapter(adapter);
            }
        }
    }



}

