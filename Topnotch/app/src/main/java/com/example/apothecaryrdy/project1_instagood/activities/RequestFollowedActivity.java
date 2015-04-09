package com.example.apothecaryrdy.project1_instagood.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apothecaryrdy.project1_instagood.PhotoListAdapter;
import com.example.apothecaryrdy.project1_instagood.R;

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


public class RequestFollowedActivity extends ListActivity {

    private EditText tagEditText; // EditText where user Enters a name
    private SharedPreferences savedSearches; // user's favorite searches
    private ArrayList<String> followerlist; // list of tags for saved searches
    private ArrayList<String> profilenames;
    private ArrayAdapter adapter; // binds tags to ListView


    protected static final String CLIENT_ID = "994ee47a2ec54f049b58bf7d9a3bb602";
    protected static final String CLIENT_SECRET = "c1b5194be4ec46eca8706b15dc33321a";
    protected static final String REDIRECT_URI = "topnotchcustomscheme://oauth/callback/instagram/";
    private InstagramSession mInstagramSession;
    private Instagram mInstagram;
    protected GridView mGridViewr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_follower);
        mGridViewr   = (GridView) findViewById(R.id.mGridView1);
        mInstagram = new Instagram(this, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
        mInstagramSession = mInstagram.getSession();

        if (mInstagramSession.isActive()) {
            InstagramUser instagramUser = mInstagramSession.getUser();
            //this is to get image

            // get references to the EditTexts //TODO
            tagEditText = (EditText) findViewById(R.id.tagEditText);


            adapter = new ArrayAdapter<String>(this, R.layout.list_item, followerlist);

            // register listener that searches Instagram when user touches a tag //TODO
            getListView().setOnItemClickListener(itemClickListener);

            // set listener that allows user to delete or edit a search //TODO
            getListView().setOnItemLongClickListener(itemLongClickListener);

            new DownloadTask().execute();
        }
    } // end method onCreate


    // itemClickListener launches a web browser to display search results
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String url = "http://instagram.com/" + profilenames.get(position);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        }
    };

    //Long Click Listener gives two options for name selected
    AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tagView = (TextView) view;
            final String tag = tagView.getText().toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(RequestFollowedActivity.this);
            builder.setTitle("Please choose your action for " + tag);
            String items[] = new String[]{"Edit", "Delete"};
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int index) {
                    switch (index) {
                        case 0: //Edit
                            tagEditText.setText(tag);
                            break;
                        case 1: //Delete
                            deleteSearch(tag);
                            break;

                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create().show();
            return true;
        }
    };//TODO



    // deletes a search after the user confirms the delete operation
    private void deleteSearch(final String tag) {
        // create a new AlertDialog
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);//TODO
        confirmBuilder.setMessage("Are you sure?");
        // set the AlertDialog's message //TODO

        // set the AlertDialog's negative Button //TODO
        confirmBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // set the AlertDialog's positive Button //TODO
        confirmBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                followerlist.remove(tag);
                adapter.notifyDataSetChanged();
            }
        });
        // display AlertDialog //TODO
        confirmBuilder.create().show();
    } // end method deleteSearch


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
                String mscope = "relationships";
                params.add(new BasicNameValuePair("scope", "mscope"));

                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
                String response			 = request.createRequest("GET", "/users/" + instagramUser.id + "/followed-by", params);
                //https://api.instagram.com/v1/users/{user-id}/media/recent/?access_token=ACCESS-TOKEN
                //String response	= request.createRequest("GET", "/users/self/feed", params);
                if (!response.equals("")) {
                    JSONObject jsonObj  = (JSONObject) new JSONTokener(response).nextValue();
                    JSONArray jsonData	= jsonObj.getJSONArray("data");

                    int length = jsonData.length();

                    if (length > 0) {
                        photoList = new ArrayList<String>();
                        followerlist = new ArrayList<String>();
                        profilenames = new ArrayList<String>();
                        for (int i = 0; i < length; i++) {
                            JSONObject jsonPhoto = jsonData.getJSONObject(i);
                            photoList.add(jsonPhoto.getString("profile_picture"));
                            followerlist.add(jsonPhoto.getString("full_name"));
                            profilenames.add(jsonPhoto.getString("username"));
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
                int width=200;
                int height	= width;

                PhotoListAdapter adapter = new PhotoListAdapter(RequestFollowedActivity.this);

                adapter.setData(photoList);
                adapter.setLayoutParam(width, height);

                mGridViewr.setAdapter(adapter);
            }

        if (followerlist == null) {
            Toast.makeText(getApplicationContext(), "No Names Available", Toast.LENGTH_LONG).show();
        } else {
            adapter = new ArrayAdapter<String>(RequestFollowedActivity.this, R.layout.list_item, followerlist);
            setListAdapter(adapter);

        }
    }
    }







} // end class MainActivity
