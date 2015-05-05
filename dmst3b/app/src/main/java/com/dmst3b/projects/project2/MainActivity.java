package com.dmst3b.projects.project2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dmst3b.projects.project2.Database.ScoreListActivity;
import com.dmst3b.projects.project2.Database.ScoreListFragment;
import com.dmst3b.projects.project2.Settings.SettingsActivity;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.preference.PreferenceManager.setDefaultValues;



public class MainActivity extends ActionBarActivity
{

    //prefence strings
    public static final String RESETTER = "pref_checkToReset";
    public static final String DEFAULTER = "pref_checkToDefault";
    private boolean defaulter = true; //used to always load defaults on start
    public boolean resetter = true; // used to stop reset after change in settings
    private boolean preferencesChanged = true; // did preferences change?
    private CannonView cannonView;

//keys for storing name ID in Bundle passed to a fragment
public static final String LEVEL_ID = "level_id";
ScoreListFragment scoreListFragment;
CannonGameFragment cannonGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDefaultValues(this, R.xml.preferences, false);

        // register listener for SharedPreferences changes
        getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(
                        preferenceChangeListener);


        scoreListFragment = new ScoreListFragment();






    }

   /* public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
        case R.id.action_settings:{
            Intent preferencesIntent = new Intent(this, SettingsActivity.class);
            startActivity(preferencesIntent);
              break;}
        case R.id.score_list: {
            Intent scoreIntent = new Intent(this, ScoreListActivity.class);
            startActivity(scoreIntent);
            break;         }

        }
        return super.onOptionsItemSelected(item);
    }



    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                // called when the user changes the app's preferences
                @Override
                public void onSharedPreferenceChanged(
                        SharedPreferences sharedPreferences, String key) {
                    preferencesChanged = true; // user changed app settings

                     //scoreList = (ScoreListFragment) getFragmentManager().findFragmentById(R.id.scoreListFragment);

                    //sets the boolean equal to the current state of the check box in settings
                    resetter = sharedPreferences.getBoolean(RESETTER, false);


                    defaulter = sharedPreferences.getBoolean(DEFAULTER, false);




                    if (resetter || defaulter) {
                        Toast.makeText(MainActivity.this, R.string.confirm_title, Toast.LENGTH_SHORT).show();
                    }
                } // end method onSharedPreferenceChanged
            };
}// End Main activity

