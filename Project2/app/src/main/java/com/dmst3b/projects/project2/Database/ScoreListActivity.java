// SettingsActivity.java
// Activity to display SettingsFragment on a phone
package com.dmst3b.projects.project2.Database;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.dmst3b.projects.project2.R;

public class ScoreListActivity extends ActionBarActivity implements DetailsFragment.DetailsFragmentListener, ScoreListFragment.ScoreListFragmentListener
{
   // use FragmentManager to display SettingsFragment
   ScoreListFragment scoreListFragment;
    private ScoreListFragment.ScoreListFragmentListener listener;
    public static final String LEVEL_ID = "level_id";
   @Override
   protected void onCreate(Bundle savedInstanceState) 
   {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main2);

       if (findViewById(R.id.fragmentContainer) != null)
       {


           scoreListFragment = new ScoreListFragment();
           // add the fragment to the FrameLayout
           FragmentTransaction transaction =
                   getFragmentManager().beginTransaction();
           transaction.add(R.id.fragmentContainer, scoreListFragment);
           transaction.commit(); // causes ScoreListFragment to display
       }
   }


    private void displayScore(long levelID, int viewID)
    {
        DetailsFragment detailsFragment = new DetailsFragment();

        // specify levelID as an argument to the DetailsFragment
        Bundle arguments = new Bundle();
        arguments.putLong(LEVEL_ID, levelID);
        detailsFragment.setArguments(arguments);

        // use a FragmentTransaction to display the DetailsFragment
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        transaction.replace(viewID, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes DetailsFragment to display
    }

    @Override
    public void onScoreSelected(long levelID) {
        if (findViewById(R.id.fragmentContainer) != null) // phone
            displayScore(levelID, R.id.fragmentContainer);
    }




    public void onClearScore() {
        getFragmentManager().popBackStack(); // removes top of back stack

        scoreListFragment.updateScoreList();

    }




    @Override
    public void onScoreDeleted() {
        getFragmentManager().popBackStack(); // removes top of back stack

        if (findViewById(R.id.fragmentContainer) == null) // tablet
            scoreListFragment.updateScoreList();
    }



} // end class ScoreListActivity