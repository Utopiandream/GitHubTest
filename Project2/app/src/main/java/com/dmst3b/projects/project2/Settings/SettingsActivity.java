// SettingsActivity.java
// Activity to display SettingsFragment on a phone
package com.dmst3b.projects.project2.Settings;

import android.app.Activity;
import android.os.Bundle;

import com.dmst3b.projects.project2.R;

public class SettingsActivity extends Activity
{
   // use FragmentManager to display SettingsFragment
   @Override
   protected void onCreate(Bundle savedInstanceState) 
   {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.fragment_settings);
   }
} // end class SettingsActivity