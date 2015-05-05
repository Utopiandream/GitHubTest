package com.dmst3b.projects.project2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CannonGameFragment extends Fragment {
    private CannonView cannonView; // custom view to display the game
    private CannonThread data;

     // used to stop reset after change in settings

    // called when Fragment's view needs to be created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreateView(inflater, container, savedInstanceState);
        View view =
                inflater.inflate(R.layout.fragment_cannon_game, container, false);

        // get the CannonView
        cannonView = (CannonView) view.findViewById(R.id.cannonView);
        return view;

    }
    public void setData(CannonThread data) {
        this.data = data;
    }

    public CannonThread getData() {
        return data;
    }
    // set up volume control once Activity is created
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // allow volume keys to set game volume
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        aboutGame();
    }


    final DialogFragment introduction1 =
            new DialogFragment() {
                // create an AlertDialog and return it
                @Override
                public Dialog onCreateDialog(Bundle bundle) {
                    // create dialog displaying String resource for messageId
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.hello_world);

                    //Hope all of this works


                    builder.setMessage("There are 3 Levels.\n" +
                            "The object of the game is to hit all of the\n" +
                            " yellow and blue targets before time runs out!\n" +
                            "Highscores will be tracked and saved. \n You may turn this off in settings." +
                            "Green bar is slomo Powerup!. \n You can turn off this message by, \n" +
                            " unchecking the About Game option in settings." +
                            "\n if you would like to clear highscores, \n you can do so at top right while viewing scores.");


                    builder.setPositiveButton(R.string.okay,
                            new DialogInterface.OnClickListener() {
                                // called when "Next Level" Button is pressed
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cannonView.dialogIsDisplayed = false;

                                    cannonView.newGame();
                                }
                            } // end anonymous inner class
                    ); // end call to setPositiveButton

                    return builder.create(); // return the AlertDialog
                }
            };




    // when MainActivity is paused, CannonGameFragment terminates the game
    @Override
    public void onPause() {
        super.onPause();
        cannonView.stopGame(); // terminates the game

    }

    // when MainActivity is paused, CannonGameFragment releases resources
    @Override
    public void onDestroy() {
        super.onDestroy();
        cannonView.releaseResources();
    }

    public void aboutGame(){

        // use FragmentManager to display the confirmDelete DialogFragment
        if (cannonView.defaulter){
            //cannonView.dialogIsDisplayed = true;
            introduction1.show(getFragmentManager(), "introduction");
        }

    }

} // end class CannonGameFragment