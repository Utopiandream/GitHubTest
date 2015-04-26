//
// Displays the list of scores
package com.dmst3b.projects.project2.Database;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.dmst3b.projects.project2.R;

public class ScoreListFragment extends ListFragment
{
   // callback methods implemented by MainActivity  
   public interface ScoreListFragmentListener
   {
      // called when user selects a score
      public void onScoreSelected(long rowID);

      // called when user decides to add a score
      public void onAddScore();
   }
   
   private ScoreListFragmentListener listener;
   
   private ListView scoreListView; // the ListActivity's ListView
   private CursorAdapter scoreAdapter; // adapter for ListView
   
   // set ScoreListFragmentListener when fragment attached
   @Override
   public void onAttach(Activity activity)
   {
      super.onAttach(activity);
      listener = (ScoreListFragmentListener) activity;
   }

   // remove ScoreListFragmentListener when Fragment detached
   @Override
   public void onDetach()
   {
      super.onDetach();
      listener = null;
   }

   // called after View is created
   @Override
   public void onViewCreated(View view, Bundle savedInstanceState)
   {
      super.onViewCreated(view, savedInstanceState);
      setRetainInstance(true); // save fragment across config changes
      setHasOptionsMenu(true); // this fragment has menu items to display

      // set text to display when there are no Score
      setEmptyText(getResources().getString(R.string.no_score));

      // get ListView reference and configure ListView
      scoreListView = getListView();
      scoreListView.setOnItemClickListener(viewScoreListener);
      scoreListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      
      // map each score's name to a TextView in the ListView layout
      String[] from = new String[] { "name", "age" };
      int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
      scoreAdapter = new SimpleCursorAdapter(getActivity(),
         android.R.layout.simple_list_item_2, null, from, to, 0);
      setListAdapter(scoreAdapter); // set adapter that supplies data
   }

   // responds to the user touching a score's name in the ListView
   OnItemClickListener viewScoreListener = new OnItemClickListener()
   {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, 
         int position, long id) 
      {
         listener.onScoreSelected(id); // pass selection to MainActivity
      } 
   }; // end viewScoreListener

   // when fragment resumes, use a GetScoresTask to load scores
   @Override
   public void onResume() 
   {
      super.onResume(); 
      new GetScoresTask().execute((Object[]) null);
   }

   // performs database query outside GUI thread
   private class GetScoresTask extends AsyncTask<Object, Object, Cursor>
   {
      DatabaseConnector databaseConnector = 
         new DatabaseConnector(getActivity());

      // open database and return Cursor for all scores
      @Override
      protected Cursor doInBackground(Object... params)
      {
         databaseConnector.open();
         return databaseConnector.getAllScores();
      } 

      // use the Cursor returned from the doInBackground method
      @Override
      protected void onPostExecute(Cursor result)
      {
         scoreAdapter.changeCursor(result); // set the adapter's Cursor
         databaseConnector.close();
      } 
   } // end class GetScoresTask

   // when fragment stops, close Cursor and remove from scoreAdapter
   @Override
   public void onStop() 
   {
      Cursor cursor = scoreAdapter.getCursor(); // get current Cursor
      scoreAdapter.changeCursor(null); // adapter now has no Cursor
      
      if (cursor != null) 
         cursor.close(); // release the Cursor's resources
      
      super.onStop();
   } 

   // display this fragment's menu items
   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
   {
      super.onCreateOptionsMenu(menu, inflater);
      inflater.inflate(R.menu.fragment_score_list_menu, menu);
   }

   // handle choice from options menu
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      switch (item.getItemId())
      {
         case R.id.action_add:
            listener.onAddScore();
            return true;
      }
      
      return super.onOptionsItemSelected(item); // call super's method
   }
   
   // update data set
   public void updateScoreList()
   {
      new GetScoresTask().execute((Object[]) null);
   }
} // end class ScoreListFragment
