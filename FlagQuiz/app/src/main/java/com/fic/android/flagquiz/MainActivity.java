package com.fic.android.flagquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends ActionBarActivity {
    // keys for reading data from SharedPreferences
    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regionsToInclude";
    public static final String RESETTER = "pref_checkToReset";
    public static final String DEFAULTER = "pref_checkToDefault";

    private boolean defaulter = true; //used to always load defaults on start
    private boolean resetter = true; // used to stop reset after change in settings
    private boolean phoneDevice = true; // used to force portrait mode
    private boolean preferencesChanged = true; // did preferences change?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set default values in the app's SharedPreferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // register listener for SharedPreferences changes
        PreferenceManager.getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(
                        preferenceChangeListener);

        // determine screen size
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        // if device is a tablet, set phoneDevice to false
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            phoneDevice = false; // not a phone-sized device

        // if running on phone-sized device, allow only portrait orientation
        if (phoneDevice)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    } // end method onCreate

    // called after onCreate completes execution
    @Override
    protected void onStart() {
        super.onStart();

        if(defaulter) {
            // set default values in the app's SharedPreferences


        }



        if (preferencesChanged) {
            // now that the default preferences have been set,
            // initialize QuizFragment and start the quiz
            QuizFragment quizFragment = (QuizFragment) getFragmentManager().findFragmentById(R.id.quizFragment);
            quizFragment.updateGuessRows(PreferenceManager.getDefaultSharedPreferences(this));
            quizFragment.updateRegions(PreferenceManager.getDefaultSharedPreferences(this));
            if (resetter) {
                quizFragment.resetQuiz();
            }
            else {
                quizFragment.resetQuizQuestion();
            }
            preferencesChanged = false;
        }
    } // end method onStart

    // show menu if app is running on a phone or a portrait-oriented tablet
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // get the default Display object representing the screen
        boolean ret = false;
        int orientation = getResources().getConfiguration().orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                getMenuInflater().inflate(R.menu.menu_main, menu); // inflate the menu
                ret = true;
                break;
        }
        return ret;
    } // end method onCreateOptionsMenu

    // displays SettingsActivity when running on a phone
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);
    }

    // listener for changes to the app's SharedPreferences
    private OnSharedPreferenceChangeListener preferenceChangeListener =
            new OnSharedPreferenceChangeListener() {
                // called when the user changes the app's preferences
                @Override
                public void onSharedPreferenceChanged(
                        SharedPreferences sharedPreferences, String key) {
                    preferencesChanged = true; // user changed app settings

                    QuizFragment quizFragment = (QuizFragment) getFragmentManager().findFragmentById(R.id.quizFragment);

                    if (key.equals(RESETTER)) {
                        if (resetter) {
                            resetter = false;
                        } else {
                            resetter = true;
                        }
                    }
                    /*if (key.equals(DEFAULTER)) {
                        if (defaulter) {
                            defaulter = false;
                        } else {
                            defaulter = true;
                            static void setDefaultValues(Context context, int resId, boolean readAgain);
                        }
                    }*/

                    if (key.equals(CHOICES)) // # of choices to display changed
                    {
                        quizFragment.updateGuessRows(sharedPreferences);
                        if (resetter) {
                            quizFragment.resetQuiz();
                        }
                        else quizFragment.resetQuizQuestion();
                    } else if (key.equals(REGIONS)) // regions to include changed
                    {
                        Set<String> regions = sharedPreferences.getStringSet(REGIONS, null);

                        if (regions != null && regions.size() > 0) {
                            quizFragment.updateRegions(sharedPreferences);
                            if (resetter) {
                                quizFragment.resetQuiz();
                            }
                            else quizFragment.resetQuizQuestion();
                        } else // must select one region--set North America as default
                        {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            regions.add(getResources().getString(R.string.default_region));
                            editor.putStringSet(REGIONS, regions);
                            editor.commit();
                            Toast.makeText(MainActivity.this, R.string.default_region_message, Toast.LENGTH_SHORT).show();
                        }

                    }
                    if (resetter) {
                        Toast.makeText(MainActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT).show();
                    }
                } // end method onSharedPreferenceChanged
            }; // end anonymous inner class
} // end class MainActivity
