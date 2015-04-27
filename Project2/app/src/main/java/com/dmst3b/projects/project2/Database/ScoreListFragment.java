//
// Displays the list of scores
package com.dmst3b.projects.project2.Database;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.DialogInterface;
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


       public void onClearScore();


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

public void onClearScore(){

    // use FragmentManager to display the confirmDelete DialogFragment
    confirmDelete.show(getFragmentManager(), "confirm delete");

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
      String[] from = new String[] { "name"};
      int[] to = new int[] { android.R.id.text1};
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

          case R.id.action_clear:
            onClearScore();

      }
      
      return super.onOptionsItemSelected(item); // call super's method
   }
   
   // update data set
   public void updateScoreList()
   {
      new GetScoresTask().execute((Object[]) null);
   }

    DialogFragment confirmDelete =
            new DialogFragment()
            {
                // create an AlertDialog and return it
                @Override
                public Dialog onCreateDialog(Bundle bundle)
                {
                    // create a new AlertDialog Builder
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(getActivity());

                    builder.setTitle(R.string.confirm_title);
                    builder.setMessage(R.string.confirm_message);

                    // provide an OK button that simply dismisses the dialog
                    builder.setPositiveButton(R.string.button_delete,
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(
                                        DialogInterface dialog, int button)
                                {
                                    final DatabaseConnector databaseConnector =
                                            new DatabaseConnector(getActivity());

                                    // AsyncTask deletes score and notifies listener
                                    AsyncTask<Long, Object, Object> deleteTask =
                                            new AsyncTask<Long, Object, Object>()
                                            {
                                                @Override
                                                protected Object doInBackground(Long... params)
                                                {
                                                    databaseConnector.clearScore();
                                                    return null;
                                                }

                                                @Override
                                                protected void onPostExecute(Object result)
                                                {
                                                    listener.onClearScore();
                                                    // set text to display when there are no Score

                                                }
                                            }; // end new AsyncTask

                                    // execute the AsyncTask to delete score at rowID
                                    deleteTask.execute();
                                } // end method onClick
                            } // end anonymous inner class
                    ); // end call to method setPositiveButton

                    builder.setNegativeButton(R.string.button_cancel, null);
                    return builder.create(); // return the AlertDialog

                }
            };
} // end class ScoreListFragment
