
package com.dmst3b.projects.project2.Database;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.dmst3b.projects.project2.MainActivity;
import com.dmst3b.projects.project2.R;

public class AddEditFragment extends Fragment
{
   // callback method implemented by MainActivity  
   public interface AddEditFragmentListener
   {
      // called after edit completed so score can be redisplayed
      public void onAddEditCompleted(long levelID);
   }
   
   private AddEditFragmentListener listener; 
   
   private long levelID; // database level ID of the score
   private Bundle scoreInfoBundle; // arguments for editing a score

   // EditTexts for score information
   private EditText nameEditText;



   // set AddEditFragmentListener when Fragment attached   
   @Override
   public void onAttach(Activity activity)
   {
      super.onAttach(activity);
      listener = (AddEditFragmentListener) activity; 
   }

   // remove AddEditFragmentListener when Fragment detached
   @Override
   public void onDetach()
   {
      super.onDetach();
      listener = null; 
   }
   
   // called when Fragment's view needs to be created
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState)
   {
      super.onCreateView(inflater, container, savedInstanceState);    
      setRetainInstance(true); // save fragment across config changes
      setHasOptionsMenu(true); // fragment has menu items to display
      
      // inflate GUI and get references to EditTexts
      View view = 
         inflater.inflate(R.layout.fragment_add_edit, container, false);
      nameEditText = (EditText) view.findViewById(R.id.nameEditText);


      scoreInfoBundle = getArguments(); // null if creating new score

      if (scoreInfoBundle != null)
      {
         levelID = scoreInfoBundle.getLong(MainActivity.LEVEL_ID);
         nameEditText.setText(scoreInfoBundle.getString("name"));

      } 
      
      // set Save Score Button's event listener
      Button saveScoreButton =
         (Button) view.findViewById(R.id.saveScoreButton);
      saveScoreButton.setOnClickListener(saveScoreButtonClicked);
      return view;
   }

   // responds to event generated when user saves a score
   OnClickListener saveScoreButtonClicked = new OnClickListener()
   {
      @Override
      public void onClick(View v) 
      {
         if (nameEditText.getText().toString().trim().length() != 0)
         {
            // AsyncTask to save score, then notify listener
            AsyncTask<Object, Object, Object> saveScoreTask =
               new AsyncTask<Object, Object, Object>() 
               {
                  @Override
                  protected Object doInBackground(Object... params) 
                  {
                     saveScore(); // save score to the database
                     return null;
                  } 
      
                  @Override
                  protected void onPostExecute(Object result) 
                  {
                     // hide soft keyboard
                     InputMethodManager imm = (InputMethodManager) 
                        getActivity().getSystemService(
                           Context.INPUT_METHOD_SERVICE);
                     imm.hideSoftInputFromWindow(
                        getView().getWindowToken(), 0);

                     listener.onAddEditCompleted(levelID);
                  } 
               }; // end AsyncTask
               
            // save the score to the database using a separate thread
            saveScoreTask.execute((Object[]) null);
         } 
         else // required score name is blank, so display error dialog
         {
            DialogFragment errorSaving = 
               new DialogFragment()
               {
                  @Override
                  public Dialog onCreateDialog(Bundle savedInstanceState)
                  {
                     AlertDialog.Builder builder = 
                        new AlertDialog.Builder(getActivity());
                     builder.setMessage(R.string.error_message);
                     builder.setPositiveButton(R.string.ok, null);                     
                     return builder.create();
                  }               
               };
            
            errorSaving.show(getFragmentManager(), "error saving score");
         } 
      } // end method onClick
   }; // end OnClickListener saveScoreButtonClicked

   // saves score information to the database
   private void saveScore()
   {
      // get DatabaseConnector to interact with the SQLite database
      DatabaseConnector databaseConnector = 
         new DatabaseConnector(getActivity());

      if (scoreInfoBundle == null)
      {
         // insert the contact information into the database
         levelID = databaseConnector.insertScore(
            nameEditText.getText().toString());

      } 
      else
      {
         databaseConnector.updateScore(levelID,
                 nameEditText.getText().toString());
      }
   } // end method saveContact
} // end class AddEditFragment
