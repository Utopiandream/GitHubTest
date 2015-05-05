package com.dmst3b.projects.project2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dmst3b.projects.project2.Database.DatabaseConnector;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.preference.PreferenceManager.setDefaultValues;

public class CannonView extends SurfaceView implements SurfaceHolder.Callback {
    // constants for game play
    public static int TARGET_PIECES = 7; // sections in the target
    public static double MISS_PENALTY = 1; // seconds deducted on a miss
    public static int HIT_REWARD = 3; // seconds added on a hit
    private static final String TAG = "CannonView"; // for logging errors
    public static final String LEVEL_ID = "level_id";
    private boolean preferencesChanged = true; // did preferences change?
    public static final String RESETTER = "pref_checkToReset";
    public static final String DEFAULTER = "pref_checkToDefault";
    public boolean defaulter; //used to always load defaults on start
    public boolean resetter;

    // constants and variables for managing sounds
    private static final int TARGET_SOUND_ID = 0;
    private static final int CANNON_SOUND_ID = 1;
    private static final int BLOCKER_SOUND_ID = 2;
    private static final int POWERUP_SOUND_ID = 3;
    private static final int LEVEL_SOUND_ID = 4;
    private static final int WINNER_SOUND_ID = 5;
    private static final int EXPLODE_SOUND_ID = 6;

    protected double totalElapsedTime; // elapsed seconds
    private CannonThread cannonThread; // controls the game loop
    private Activity activity; // to display Game Over dialog in GUI thread
    boolean dialogIsDisplayed = false;
    // variables for the game loop and tracking statistics
    private boolean gameOver; // is the game over?
    private boolean nextLevel; //go to next level?
    public int level;
    private double timeLeft; // time remaining in seconds
    private int shotsFired; // shots the user has fired
    // variables for the blocker and target
    private Line blocker; // start and end points of the blocker
    private int blockerDistance; // blocker distance from left
    private int blockerBeginning; // blocker top-edge distance from top
    private int blockerEnd; // blocker bottom-edge distance from top
    private int initialBlockerVelocity; // initial blocker speed multiplier
    private float blockerVelocity; // blocker speed multiplier during game
    // variables for the blocker and target
    private Line blocker2; // start and end points of the blocker
    private int blockerDistance2; // blocker distance from left
    private int blockerBeginning2; // blocker top-edge distance from top
    private int blockerEnd2; // blocker bottom-edge distance from top
    private int initialBlockerVelocity2; // initial blocker speed multiplier
    private float blockerVelocity2; // blocker speed multiplier during game
    private Line target; // start and end points of the target
    private int targetDistance; // target distance from left
    private int targetBeginning; // target distance from top
    private double pieceLength; // length of a target piece
    private int targetEnd; // target bottom's distance from top
    private int initialTargetVelocity; // initial target speed multiplier
    private float targetVelocity; // target speed multiplier
    private int lineWidth; // width of the target and blocker
    private boolean[] hitStates; // is each target piece hit?
    private int targetPiecesHit; // number of target pieces hit (out of 7)
    // variables for the cannon and cannonball
   private Point cannonball; // cannonball image's upper-left corner
    private int cannonballVelocityX; // cannonball's x velocity
    private int cannonballVelocityY; // cannonball's y velocity
    private boolean cannonballOnScreen; // whether cannonball on the screen
    private int cannonballRadius; // cannonball's radius
    private int cannonballSpeed; // cannonball's speed */
    private int cannonBaseRadius; // cannon base's radius
    private int cannonLength; // cannon barrel's length
    private int originalcannonballSpeed;
    private int originalcannonballRadius;
    private Point barrelEnd; // the endpoint of the cannon's barrel
    private int screenWidth;
    private int screenHeight;
    private SoundPool soundPool; // plays sound effects
    private SparseIntArray soundMap; // maps IDs to SoundPool
    private cannonballshot[] ballsfired;
    private double cooldown;
    private boolean slowmo;
    private boolean extraTime;
    private long levelID;
    private  boolean turnOnSecondBlocker;
    private int fastShot;

 private Line powerUp; // start and end points of the PowerUP
 private int powerUpDistance; // PowerUp distance from left
 private int powerUpBeginning; // PowerUP top-edge distance from top
 private int powerUpEnd; // PowerUp bottom-edge distance from top
 private int initialpowerUpVelocity; // initial PowerUp speed multiplier
 private float powerUpVelocity; // Powerup speed multiplier during game
 private int powerUpWidth; // width of the target and PowerUp
 private int powerUpDistanceLast;
 private Paint powerUpPaint;



    // Paint variables used when drawing each item on the screen
    private Paint textPaint; // Paint used to draw text
    private Paint cannonballPaint; // Paint used to draw the cannonball
    private Paint cannonPaint; // Paint used to draw the cannon
    private Paint blockerPaint; // Paint used to draw the blocker
    private Paint blockerPaint2; // Paint used to draw the blocker2

    private Paint targetPaint; // Paint used to draw the target
    private Paint backgroundPaint; // Paint used to clear the drawing area

    // public constructor
    public CannonView(Context context, AttributeSet attrs) {
        super(context, attrs); // call superclass constructor
        activity = (Activity) context; // store reference to MainActivity
        //Get Initial Settings
        resetter = getDefaultSharedPreferences(context).getBoolean(RESETTER, false);
        defaulter = getDefaultSharedPreferences(context).getBoolean(DEFAULTER, false);
        setDefaultValues(context, R.xml.preferences, false);

        // register listener for SharedPreferences changes
        getDefaultSharedPreferences(context).
                registerOnSharedPreferenceChangeListener(
                        preferenceChangeListener);


        // register SurfaceHolder.Callback listener
        getHolder().addCallback(this);

        // initialize Lines and Point representing game items
        blocker = new Line(); // create the blocker as a Line
        blocker2 = new Line(); // create the blocker as a Line

        target = new Line(); // create the target as a Line
        cannonball = new Point(); // create the cannonball as a Point
        powerUp = new Line();

        ballsfired = new cannonballshot[5];

        // initialize hitStates as a boolean array
        hitStates = new boolean[TARGET_PIECES];

        // initialize SoundPool to play the app's three sound effects
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        // create Map of sounds and pre-load sounds
        soundMap = new SparseIntArray(7); // create new HashMap
        soundMap.put(TARGET_SOUND_ID,
                soundPool.load(context, R.raw.target_hit, 1));
        soundMap.put(CANNON_SOUND_ID,
                soundPool.load(context, R.raw.cannon_fire, 1));
        soundMap.put(BLOCKER_SOUND_ID,
                soundPool.load(context, R.raw.blocker_hit, 1));
        soundMap.put(POWERUP_SOUND_ID,
                soundPool.load(context, R.raw.powerup_fire, 1));
        soundMap.put(LEVEL_SOUND_ID,
                soundPool.load(context, R.raw.yescurrvyscum, 1));
        soundMap.put(WINNER_SOUND_ID,
                soundPool.load(context, R.raw.yourbooty, 1));
        soundMap.put(EXPLODE_SOUND_ID,
                soundPool.load(context, R.raw.explode, 1));

        //soundMap.put(new sound ID, soundPool...

        // construct Paints for drawing text, cannonball, cannon,
        // blocker and target; these are configured in method onSizeChanged
        textPaint = new Paint();
        cannonPaint = new Paint();
        cannonballPaint = new Paint();
        blockerPaint = new Paint();
        blockerPaint2 = new Paint();
        targetPaint = new Paint();
        backgroundPaint = new Paint();
        powerUpPaint = new Paint();


    } // end CannonView constructor

    // called by surfaceChanged when the size of the SurfaceView changes,
    // such as when it's first added to the View hierarchy
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            screenWidth = w; // store CannonView's width
            screenHeight = h; // store CannonView's height
            cannonBaseRadius = h / 18; // cannon base radius 1/18 screen height
            cannonLength = w / 24; // cannon length 1/8 screen width
            for (int i = 1; i < 5; i++) {
                ballsfired[i] = new cannonballshot();
                ballsfired[i].Radius = h / 36;
                ballsfired[i].Speed = h * 2;
                ballsfired[i].originalRadius = h / 36;
                ballsfired[i].originalSpeed = h* 2;
                ballsfired[i].cannonballPaint = new Paint();
            }

            /// cannonball radius 1/36 screen width
            cannonballRadius = h / 36;
            // cannonball speed multiplier
            cannonballSpeed = h * 2;

            lineWidth = w / 60; // target and blocker 1/24 screen width
            powerUpWidth = w / 60;

            //set orginals to reset
            //originalcannonballRadius = w / 36;
            originalcannonballRadius = h / 36;
            //originalcannonballSpeed = w * 3 / 2;
            originalcannonballSpeed = h * 2;

            // configure instance variables related to the blocker
            blockerDistance = w * 5 / 8; // blocker 5/8 screen width from left
            blockerBeginning = h / 8; // distance from top 1/8 screen height
            blockerEnd = h * 3 / 8; // distance from top 3/8 screen height
            initialBlockerVelocity = h / 4; // initial blocker speed multiplier
            blocker.start = new Point(blockerDistance, blockerBeginning);
            blocker.end = new Point(blockerDistance, blockerEnd);


            //add same things for blocker2
            blockerDistance = w * 4 / 8; // blocker 5/8 screen width from left
            blockerBeginning2 = h / 8; // distance from top 1/8 screen height
            blockerEnd2 = h * 1 / 4; // distance from top 3/8 screen height
            initialBlockerVelocity2 = h / 3; // initial blocker speed multiplier
            blocker2.start = new Point(blockerDistance2, blockerBeginning2);
            blocker2.end = new Point(blockerDistance2, blockerEnd2);

            //add all the things for powerup
            powerUpDistance = w * 3 / 8;
            powerUpDistanceLast = w * 6 / 8;
            powerUpBeginning = h * 48 / 50; // PowerUp top-edge distance from top
            powerUpEnd = h * 49 / 50; // PowerUp bottom-edge distance from top
            initialpowerUpVelocity = h; // initial PowerUp speed multiplier
            powerUp.start = new Point(powerUpDistance, powerUpBeginning);
            powerUp.end = new Point(powerUpDistance, powerUpEnd);


            // configure instance variables related to the target
            targetDistance = w * 7 / 8; // target 7/8 screen width from left
            targetBeginning = h / 8; // distance from top 1/8 screen height
            targetEnd = h * 7 / 8; // distance from top 7/8 screen height
            pieceLength = (targetEnd - targetBeginning) / TARGET_PIECES;
            initialTargetVelocity = -h / 4; // initial target speed multiplier
            target.start = new Point(targetDistance, targetBeginning);
            target.end = new Point(targetDistance, targetEnd);

            // endpoint of the cannon's barrel initially points horizontally
            barrelEnd = new Point(cannonLength, h / 2);

            // configure Paint objects for drawing game elements
            textPaint.setTextSize(w / 60); // text size 1/20 of screen width
            textPaint.setAntiAlias(true); // smoothes the text
            cannonPaint.setStrokeWidth(lineWidth / 3f); // set line thickness
            blockerPaint.setStrokeWidth(lineWidth / 2); // set line thickness\
            blockerPaint2.setStrokeWidth(lineWidth / 2); // set line thickness
            targetPaint.setStrokeWidth(lineWidth/ 2); // set line thickness
            backgroundPaint.setColor(Color.WHITE); // set background color
            //set powerup paint
            powerUpPaint.setStrokeWidth(powerUpWidth/ 2); // set line thickness
        }
else {
            screenWidth = w; // store CannonView's width
            screenHeight = h; // store CannonView's height
            cannonBaseRadius = h / 18; // cannon base radius 1/18 screen height
            cannonLength = w / 8; // cannon length 1/8 screen width
            for (int i = 1; i < 5; i++) {
                ballsfired[i] = new cannonballshot();
                ballsfired[i].Radius = w / 36;
                ballsfired[i].Speed = w * 3 / 2;
                ballsfired[i].originalRadius = w / 36;
                ballsfired[i].originalSpeed = w * 3 / 2;
                ballsfired[i].cannonballPaint = new Paint();
            }

            /// cannonball radius 1/36 screen width
            cannonballRadius = w / 36;
            // cannonball speed multiplier
            cannonballSpeed = w * 3 / 2;

            lineWidth = w / 24; // target and blocker 1/24 screen width
            powerUpWidth = w / 35;



            //set orginals to reset
            //originalcannonballRadius = w / 36;
            originalcannonballRadius = w / 36;
            //originalcannonballSpeed = w * 3 / 2;
            originalcannonballSpeed = w;

            // configure instance variables related to the blocker
            blockerDistance = w * 5 / 8; // blocker 5/8 screen width from left
            blockerBeginning = h / 8; // distance from top 1/8 screen height
            blockerEnd = h * 3 / 8; // distance from top 3/8 screen height
            initialBlockerVelocity = h / 2; // initial blocker speed multiplier
            blocker.start = new Point(blockerDistance, blockerBeginning);
            blocker.end = new Point(blockerDistance, blockerEnd);

            //Configure same for blocker 2
            blockerDistance2 = w * 4 / 8; // blocker 5/8 screen width from left
            blockerBeginning2 = h / 8; // distance from top 1/8 screen height
            blockerEnd2 = h * 1 / 4; // distance from top 3/8 screen height
            initialBlockerVelocity2 = h ; // initial blocker speed multiplier
            blocker2.start = new Point(blockerDistance2, blockerBeginning2);
            blocker2.end = new Point(blockerDistance2, blockerEnd2);

            //add all the things for powerup
            powerUpDistance = w * 3 / 8;
            powerUpDistanceLast = w * 6 / 8;
            powerUpBeginning = h * 48 / 50; // blocker top-edge distance from top
            powerUpEnd = h * 49 / 50; // blocker bottom-edge distance from top
            initialpowerUpVelocity = h; // initial blocker speed multiplier
            powerUp.start = new Point(powerUpDistance, powerUpBeginning);
            powerUp.end = new Point(powerUpDistance, powerUpEnd);


            // configure instance variables related to the target
            targetDistance = w * 7 / 8; // target 7/8 screen width from left
            targetBeginning = h / 8; // distance from top 1/8 screen height
            targetEnd = h * 7 / 8; // distance from top 7/8 screen height
            pieceLength = (targetEnd - targetBeginning) / TARGET_PIECES;
            initialTargetVelocity = -h / 4; // initial target speed multiplier
            target.start = new Point(targetDistance, targetBeginning);
            target.end = new Point(targetDistance, targetEnd);

            // endpoint of the cannon's barrel initially points horizontally
            barrelEnd = new Point(cannonLength, h / 2);

            // configure Paint objects for drawing game elements
            textPaint.setTextSize(w / 20); // text size 1/20 of screen width
            textPaint.setAntiAlias(true); // smoothes the text
            cannonPaint.setStrokeWidth(lineWidth * 1.5f); // set line thickness
            blockerPaint.setStrokeWidth(lineWidth); // set line thickness
            blockerPaint2.setStrokeWidth(lineWidth); // set line thickness
            targetPaint.setStrokeWidth(lineWidth); // set line thickness
            backgroundPaint.setColor(Color.WHITE); // set background color
            //set powerup paint
            powerUpPaint.setStrokeWidth(powerUpWidth); // set line thickness
        }



        // set up and start a new game
    } // end method onSizeChanged

    // reset all the screen elements and start a new game
    public void newGame() {
        //Reset colors, if player has been to other levels.
        backgroundPaint.setColor(Color.WHITE); // set background color
        cannonballPaint.setColor(Color.BLACK);


        level = 1;
        turnOnSecondBlocker = false;
        extraTime = false;


        // set every element of hitStates to false--restores target pieces
        for (int i = 0; i < TARGET_PIECES; i++)
            hitStates[i] = false;

        targetPiecesHit = 0; // no target pieces have been hit
        slowmo = false;
        blockerVelocity = initialBlockerVelocity; // set initial velocity

        //set powerupvelocity
        powerUpVelocity = initialpowerUpVelocity;
        targetVelocity = initialTargetVelocity; // set initial velocity
        timeLeft = 15; // start the countdown at 10 seconds
        //cannonballOnScreen = false; // the cannonball is not on the screen
        cannonballOnScreen = false;



        shotsFired = 0; // set the initial number of shots fired
        totalElapsedTime = 0.0; // set the time elapsed to zero
        cooldown = totalElapsedTime;
        ballsfired[1].cannonballOnScreen = false;


        // set the start and end Points of the blocker and target
        blocker.start.set(blockerDistance, blockerBeginning);
        blocker.end.set(blockerDistance, blockerEnd);
        target.start.set(targetDistance, targetBeginning);
        target.end.set(targetDistance, targetEnd);
        //and powerup
        powerUp.start = new Point(powerUpDistance, powerUpBeginning);
        powerUp.end = new Point(powerUpDistance, powerUpEnd);

        //Set the ball speed
        //cannonballRadius = originalcannonballRadius; // reset to level 1 size
        cannonballRadius = originalcannonballRadius;
        cannonballRadius /= 2;

        //cannonballSpeed =  originalcannonballSpeed; // reset to level 1 speed
        cannonballSpeed = originalcannonballSpeed;
        blockerPaint.setColor(Color.BLACK);
        powerUpPaint.setColor(Color.GREEN);


        if (gameOver) // starting a new game after the last game ended
        {
            gameOver = false;
            cannonThread = new CannonThread(getHolder(), this); // create thread
            cannonThread.start(); // start the game loop thread
        }
        if (nextLevel) // starting a new game after the last game ended
        {
            nextLevel = false;
            cannonThread = new CannonThread(getHolder(), this); // create thread
            cannonThread.start(); // start the game loop thread
        }
    } // end method newGame

    // reset all the screen elements and start next level
    public void secondLevel() {
        level = 2;
        turnOnSecondBlocker = true;
        extraTime = false;

        // set every element of hitStates to false--restores target pieces
        for (int i = 0; i < TARGET_PIECES; i++)
            hitStates[i] = false;
        slowmo = false;
        targetPiecesHit = 0; // no target pieces have been hit
        blockerVelocity = initialBlockerVelocity; // set initial velocity
        blockerVelocity2 = initialBlockerVelocity2;
        targetVelocity = initialTargetVelocity; // set initial velocity
        timeLeft = 15; // start the countdown at 10 seconds
        //powerUpVelocity /= 2;

        backgroundPaint.setColor(Color.GRAY); // set background color

        //cannonballOnScreen = false; // the cannonball is not on the screen
        cannonballOnScreen = false;
        shotsFired = 0; // set the initial number of shots fired
        totalElapsedTime = 0.0; // set the time elapsed to zero
        cooldown = totalElapsedTime;
        ballsfired[1].cannonballOnScreen = false;

        // set the start and end Points of the blocker and target
        blocker.start.set(blockerDistance, blockerBeginning);
        blocker.end.set(blockerDistance, blockerEnd);
        blocker2.start.set(blockerDistance2, blockerBeginning2);
        blocker2.end.set(blockerDistance2, blockerEnd2);
        target.start.set(targetDistance, targetBeginning);
        target.end.set(targetDistance, targetEnd);
        powerUp.start.set(powerUpDistanceLast, powerUpBeginning);
        powerUp.end.set(powerUpDistanceLast, powerUpEnd);

        blockerPaint2.setColor(Color.BLACK);


        //Set the ball speed
        cannonballRadius = originalcannonballRadius; // cannonball radius 1/36 screen width
        //cannonballSpeed *=  1.5; // cannonball speed multiplier
        cannonballSpeed /= 1.5;
        if (nextLevel) // starting a new game after the last game ended
        {
            nextLevel = false;
            cannonThread = new CannonThread(getHolder(), this); // create thread
            cannonThread.start(); // start the game loop thread
        }

    } // end method secondLevel
    // reset all the screen elements and start next level
    public void thirdLevel() {
        level = 3;
        turnOnSecondBlocker = false;
        extraTime = false;


        backgroundPaint.setColor(Color.DKGRAY); // set background color

        // set every element of hitStates to false--restores target pieces
        for (int i = 0; i < TARGET_PIECES; i++)
            hitStates[i] = false;
        slowmo = false;
        targetPiecesHit = 0; // no target pieces have been hit
        blockerVelocity = initialBlockerVelocity * 2; // set initial velocity
        targetVelocity = initialTargetVelocity; // set initial velocity
        timeLeft = 10; // start the countdown at 10 seconds
        powerUpVelocity = initialpowerUpVelocity;

        //cannonballOnScreen = false; // the cannonball is not on the screen
        cannonballOnScreen = false;

        shotsFired = 0; // set the initial number of shots fired
        totalElapsedTime = 0.0; // set the time elapsed to zero
        cooldown = totalElapsedTime;
        ballsfired[1].cannonballOnScreen = false;

        // set the start and end Points of the blocker and target
        blocker.start.set(blockerDistance, blockerBeginning);
        blocker.end.set(blockerDistance, blockerEnd);
        target.start.set(targetDistance, targetBeginning);
        target.end.set(targetDistance, targetEnd);

        powerUp.start.set(powerUpDistanceLast, powerUpBeginning);
        powerUp.end.set(powerUpDistanceLast, powerUpEnd);



       // cannonball speed multiplier
        // cannonball radius 1/36 screen width
        cannonballRadius *= 1.5;
         // cannonball speed multiplier
        cannonballSpeed /= 2;
        blockerPaint.setColor(Color.RED);



        if (nextLevel) // starting a new game after the last game ended
        {
            nextLevel = false;
            cannonThread = new CannonThread(getHolder(), this); // create thread
            cannonThread.start(); // start the game loop thread
        }
    } // end method thirdLevel


    public void fourthLevel() {

        backgroundPaint.setColor(Color.BLACK); // set background color

        level = 4;
        turnOnSecondBlocker = true;
        extraTime = false;


        // set every element of hitStates to false--restores target pieces
        for (int i = 0; i < TARGET_PIECES; i++)
            hitStates[i] = false;

        targetPiecesHit = 0; // no target pieces have been hit
        slowmo = false;
        blockerVelocity = initialBlockerVelocity; // set initial velocity

        //set powerupvelocity
        powerUpVelocity = initialpowerUpVelocity;
        targetVelocity = initialTargetVelocity; // set initial velocity
        timeLeft = 20; // start the countdown at 10 seconds
        //cannonballOnScreen = false; // the cannonball is not on the screen
        cannonballOnScreen = false;
        cannonballPaint.setColor(Color.RED);
        ballsfired[1].cannonballPaint.setColor(Color.RED);



        shotsFired = 0; // set the initial number of shots fired
        totalElapsedTime = 0.0; // set the time elapsed to zero
        cooldown = totalElapsedTime;
        ballsfired[1].cannonballOnScreen = false;


        // set the start and end Points of the blocker and target
        blocker.start.set(blockerDistance, blockerBeginning);
        blocker.end.set(blockerDistance, blockerEnd);
        target.start.set(targetDistance, targetBeginning);
        target.end.set(targetDistance, targetEnd);
        //and powerup
        powerUp.start = new Point(powerUpDistance, powerUpBeginning);
        powerUp.end = new Point(powerUpDistance, powerUpEnd);

        //Set the ball speed
        //cannonballRadius = originalcannonballRadius; // reset to level 1 size
        cannonballRadius = originalcannonballRadius;

        //cannonballSpeed =  originalcannonballSpeed; // reset to level 1 speed
        cannonballSpeed = originalcannonballSpeed/2;
        blockerPaint.setColor(Color.BLACK);
        powerUpPaint.setColor(Color.GREEN);


        if (gameOver) // starting a new game after the last game ended
        {
            gameOver = false;
            cannonThread = new CannonThread(getHolder(), this); // create thread
            cannonThread.start(); // start the game loop thread
        }
        if (nextLevel) // starting a new game after the last game ended
        {
            nextLevel = false;
            cannonThread = new CannonThread(getHolder(), this); // create thread
            cannonThread.start(); // start the game loop thread
        }
    } // end method newGame

    // called repeatedly by the CannonThread to update game elements

    protected void updatePositions(double elapsedTimeMS) {
        double interval = elapsedTimeMS / 1000.0; // convert to seconds

        if (timeLeft <= 7 || TARGET_PIECES - targetPiecesHit == 1) slowmo = true;
        else slowmo = false;
        if(slowmo){
            interval = elapsedTimeMS / 3000;
            if(fastShot == 0){cannonballSpeed = originalcannonballSpeed ;
            ballsfired[1].Speed= originalcannonballSpeed;}
        }
        if(fastShot > 0){
            cannonballSpeed = 3 * originalcannonballSpeed;
            ballsfired[1].Speed= 3 * originalcannonballSpeed;
        }
        else{
            cannonballSpeed = originalcannonballSpeed;
        }
        if (cannonballOnScreen) // if there is currently a shot fired
        {

            // update cannonball position
            cannonball.x += interval * cannonballVelocityX;
            cannonball.y += interval * cannonballVelocityY;

            // check for collision with blocker
            if (cannonball.x + cannonballRadius > blockerDistance &&
                    cannonball.x - cannonballRadius < blockerDistance &&
                    cannonball.y + cannonballRadius > blocker.start.y &&
                    cannonball.y - cannonballRadius < blocker.end.y) {
                cannonballVelocityX *= -1; // reverse cannonball's direction


                // play blocker sound
                soundPool.play(soundMap.get(BLOCKER_SOUND_ID), 1, 1, 1, 0, 1f);
            }   //check for collision with powerUp
            else if (cannonball.x + cannonballRadius > powerUpDistance &&
                        cannonball.x - cannonballRadius < powerUpDistance &&
                        cannonball.y + cannonballRadius > powerUp.start.y &&
                        cannonball.y - cannonballRadius < powerUp.end.y) {
                if(!extraTime) {
                    extraTime = true;
                    cannonballOnScreen = false;
                    timeLeft = timeLeft + 2 * HIT_REWARD; // add reward to remaining time
                    fastShot = 2;
                    // play powerup sound
                    soundPool.play(soundMap.get(EXPLODE_SOUND_ID), 1, 1, 1, 0, 1f);
                }
            }
            // check for collisions with left and right walls
            else if (cannonball.x + cannonballRadius > screenWidth ||
                    cannonball.x - cannonballRadius < 0) {
                cannonballOnScreen = false; // remove cannonball from screen
                timeLeft -= MISS_PENALTY; // penalize the user
            }
            // check for collisions with top and bottom walls
            else if (cannonball.y + cannonballRadius > screenHeight ||
                    cannonball.y - cannonballRadius < 0) {
                cannonballOnScreen = false; // remove cannonball from screen
                timeLeft -= MISS_PENALTY; // penalize the user
            }
            // check for cannonball collision with target
            else if (cannonball.x + cannonballRadius > targetDistance &&
                    cannonball.x - cannonballRadius < targetDistance &&
                    cannonball.y + cannonballRadius > target.start.y &&
                    cannonball.y - cannonballRadius < target.end.y) {
                // determine target section number (0 is the top)
                int section =
                        (int) ((cannonball.y - target.start.y) / pieceLength);

                // check if the piece hasn't been hit yet
                if ((section >= 0 && section < TARGET_PIECES) &&
                        !hitStates[section]) {
                    hitStates[section] = true; // section was hit
                    cannonballOnScreen = false; // remove cannonball
                    timeLeft += HIT_REWARD; // add reward to remaining time


                    // play target hit sound
                    soundPool.play(soundMap.get(TARGET_SOUND_ID), 1,
                            1, 1, 0, 1f);

                    // if all pieces have been hit
                    if (++targetPiecesHit == TARGET_PIECES) {
                        cannonThread.setRunning(false); // terminate thread
                        showGameOverDialog(R.string.win); // show winning dialog
                        gameOver = false;
                        nextLevel = true;

                    }
                }
            }
        }


        if (ballsfired[1].cannonballOnScreen) {
                // update cannonball position
                ballsfired[1].x += interval * ballsfired[1].VelocityX;
                ballsfired[1].y += interval * ballsfired[1].VelocityY;

                // check for collision with blocker
                if (ballsfired[1].x +ballsfired[1].Radius > blockerDistance &&
                        ballsfired[1].x - ballsfired[1].Radius < blockerDistance &&
                        ballsfired[1].y + ballsfired[1].Radius > blocker.start.y &&
                        ballsfired[1].y - ballsfired[1].Radius < blocker.end.y) {
                    ballsfired[1].VelocityX *= -1; // reverse cannonball's direction


                    // play blocker sound
                    soundPool.play(soundMap.get(BLOCKER_SOUND_ID), 1, 1, 1, 0, 1f);
                } else if (ballsfired[1].x + ballsfired[1].Radius > powerUpDistance &&
                            ballsfired[1].x - ballsfired[1].Radius < powerUpDistance &&
                            ballsfired[1].y + ballsfired[1].Radius > powerUp.start.y &&
                            ballsfired[1].y - ballsfired[1].Radius < powerUp.end.y) {
                    if(!extraTime) {
                        extraTime = true;
                        cannonballOnScreen = false;
                        timeLeft = timeLeft + 2 * HIT_REWARD; // add reward to remaining time
                        fastShot = 2;
                        // play powerup sound
                        soundPool.play(soundMap.get(EXPLODE_SOUND_ID), 1, 1, 1, 0, 1f);
                    }
                }
                // check for collisions with left and right walls
                else if (ballsfired[1].x + ballsfired[1].Radius > screenWidth ||
                        ballsfired[1].x - ballsfired[1].Radius < 0) {
                    ballsfired[1].cannonballOnScreen = false; // remove cannonball from screen
                    timeLeft -= MISS_PENALTY; // penalize the user
                }
                // check for collisions with top and bottom walls
                else if (ballsfired[1].y + ballsfired[1].Radius > screenHeight ||
                        ballsfired[1].y - ballsfired[1].Radius < 0) {
                    ballsfired[1].cannonballOnScreen = false; // remove cannonball from screen
                    timeLeft -= MISS_PENALTY; // penalize the user
                }
                // check for cannonball collision with target
                else if( ballsfired[1].x + ballsfired[1].Radius > targetDistance &&
                        ballsfired[1].x - ballsfired[1].Radius < targetDistance &&
                        ballsfired[1].y + ballsfired[1].Radius > target.start.y &&
                        ballsfired[1].y -ballsfired[1].Radius < target.end.y) {
                    // determine target section number (0 is the top)
                    int section =
                            (int) ((ballsfired[1].y - target.start.y) / pieceLength);

                    // check if the piece hasn't been hit yet
                    if ((section >= 0 && section < TARGET_PIECES) &&
                            !hitStates[section]) {
                        hitStates[section] = true; // section was hit
                        ballsfired[1].cannonballOnScreen = false; // remove cannonball
                        timeLeft += HIT_REWARD; // add reward to remaining time


                        // play target hit sound
                        soundPool.play(soundMap.get(TARGET_SOUND_ID), 1,
                                1, 1, 0, 1f);

                        // if all pieces have been hit
                        if (++targetPiecesHit == TARGET_PIECES) {
                            cannonThread.setRunning(false); // terminate thread
                            showGameOverDialog(R.string.win); // show winning dialog
                            gameOver = false;
                            nextLevel = true;


                        }
                    }
                }
            }

        // update the blocker's position
        double blockerUpdate = interval * blockerVelocity;
        blocker.start.y += blockerUpdate;
        blocker.end.y += blockerUpdate;

        // update the target's position
        double targetUpdate = interval * targetVelocity;
        target.start.y += targetUpdate;
        target.end.y += targetUpdate;

        //update the powerUp's position
        double powerUpUpdate = interval * powerUpVelocity;
        powerUp.start.y += powerUpUpdate;
        powerUp.end.y += powerUpUpdate;

        // if the blocker hit the top or bottom, reverse direction
        if (blocker.start.y < 0 || blocker.end.y > screenHeight)
            blockerVelocity *= -1;

        // if the target hit the top or bottom, reverse direction
        if (target.start.y < 0 || target.end.y > screenHeight)
            targetVelocity *= -1;

        if (powerUp.start.y < 0 || powerUp.end.y > screenHeight)
            powerUpVelocity *= -1;

        timeLeft -= interval; // subtract from time left

        // if the timer reached zero
        if (timeLeft <= 0.0) {
            timeLeft = 0.0;
            gameOver = true; // the game is over
            cannonThread.setRunning(false); // terminate thread
            showGameOverDialog(R.string.lose); // show the losing dialog
        }


    } // end method updatePositions
    protected void updatePositions2(double elapsedTimeMS) {
        double interval = elapsedTimeMS / 1000.0; // convert to seconds

        if (timeLeft <= 7 || TARGET_PIECES - targetPiecesHit == 1) slowmo = true;
        else slowmo = false;
        if(slowmo){
            interval = elapsedTimeMS / 3000;
            if(fastShot == 0){cannonballSpeed = originalcannonballSpeed ;
                ballsfired[1].Speed= originalcannonballSpeed;}
        }
        if(fastShot > 0){
            cannonballSpeed = 3 * originalcannonballSpeed;
            ballsfired[1].Speed = 3 * originalcannonballSpeed;
        }
        else{
            cannonballSpeed = originalcannonballSpeed;
        }
        if (cannonballOnScreen) // if there is currently a shot fired
        {
            // update cannonball position
            cannonball.x += interval * cannonballVelocityX;
            cannonball.y += interval * cannonballVelocityY;

            // check for collision with blocker
            if (cannonball.x + cannonballRadius > blockerDistance &&
                    cannonball.x - cannonballRadius < blockerDistance &&
                    cannonball.y + cannonballRadius > blocker.start.y &&
                    cannonball.y - cannonballRadius < blocker.end.y) {
                cannonballVelocityX *= -1; // reverse cannonball's direction


                // play blocker sound
                soundPool.play(soundMap.get(BLOCKER_SOUND_ID), 1, 1, 1, 0, 1f);
            } else if (cannonball.x + cannonballRadius > blockerDistance2 &&
                cannonball.x - cannonballRadius < blockerDistance2 &&
                cannonball.y + cannonballRadius > blocker2.start.y &&
                cannonball.y - cannonballRadius < blocker2.end.y) {
            cannonballVelocityX *= -1; // reverse cannonball's direction


            // play blocker sound
            soundPool.play(soundMap.get(BLOCKER_SOUND_ID), 1, 1, 1, 0, 1f);
          }
            else if (cannonball.x + cannonballRadius > powerUpDistanceLast &&
                        cannonball.x - cannonballRadius < powerUpDistanceLast &&
                        cannonball.y + cannonballRadius > powerUp.start.y &&
                        cannonball.y - cannonballRadius < powerUp.end.y) {
                if(!extraTime) {
                    extraTime = true;
                    cannonballOnScreen = false;
                    timeLeft = timeLeft + 2 * HIT_REWARD; // add reward to remaining time
                    fastShot = 2;
                    // play powerup sound
                    soundPool.play(soundMap.get(EXPLODE_SOUND_ID), 1, 1, 1, 0, 1f);
                }
            }
            // check for collisions with left and right walls
            else if (cannonball.x + cannonballRadius > screenWidth ||
                    cannonball.x - cannonballRadius < 0) {
                cannonballOnScreen = false; // remove cannonball from screen
                timeLeft -= MISS_PENALTY; // penalize the user
            }
            // check for collisions with top and bottom walls
            else if (cannonball.y + cannonballRadius > screenHeight ||
                    cannonball.y - cannonballRadius < 0) {
                cannonballOnScreen = false; // remove cannonball from screen
                timeLeft -= MISS_PENALTY; // penalize the user
            }
            // check for cannonball collision with target
            else if (cannonball.x + cannonballRadius > targetDistance &&
                    cannonball.x - cannonballRadius < targetDistance &&
                    cannonball.y + cannonballRadius > target.start.y &&
                    cannonball.y - cannonballRadius < target.end.y) {
                // determine target section number (0 is the top)
                int section =
                        (int) ((cannonball.y - target.start.y) / pieceLength);

                // check if the piece hasn't been hit yet
                if ((section >= 0 && section < TARGET_PIECES) &&
                        !hitStates[section]) {
                    hitStates[section] = true; // section was hit
                    cannonballOnScreen = false; // remove cannonball
                    timeLeft += HIT_REWARD; // add reward to remaining time


                    // play target hit sound
                    soundPool.play(soundMap.get(TARGET_SOUND_ID), 1,
                            1, 1, 0, 1f);

                    // if all pieces have been hit
                    if (++targetPiecesHit == TARGET_PIECES) {
                        if(level == 4){
                            cannonThread.setRunning(false); // terminate thread
                            showGameOverDialog(R.string.success); // show winning dialog
                            gameOver = true;
                            nextLevel = false;
                        }
                        else {
                            cannonThread.setRunning(false); // terminate thread
                            showGameOverDialog(R.string.win); // show winning dialog
                            gameOver = false;
                            nextLevel = true;
                        }

                    }
                }
            }
        }


            if(ballsfired[1].cannonballOnScreen) {
                // update cannonball position
                ballsfired[1].x += interval * ballsfired[1].VelocityX;
                ballsfired[1].y += interval * ballsfired[1].VelocityY;

                // check for collision with blocker
                if (ballsfired[1].x +ballsfired[1].Radius > blockerDistance &&
                        ballsfired[1].x - ballsfired[1].Radius < blockerDistance &&
                        ballsfired[1].y + ballsfired[1].Radius > blocker.start.y &&
                        ballsfired[1].y - ballsfired[1].Radius < blocker.end.y) {
                    ballsfired[1].VelocityX *= -1; // reverse cannonball's direction

                    // play blocker sound
                    soundPool.play(soundMap.get(BLOCKER_SOUND_ID), 1, 1, 1, 0, 1f);
                } else if (ballsfired[1].x +ballsfired[1].Radius > blockerDistance2 &&
                        ballsfired[1].x - ballsfired[1].Radius < blockerDistance2 &&
                        ballsfired[1].y + ballsfired[1].Radius > blocker2.start.y &&
                        ballsfired[1].y - ballsfired[1].Radius < blocker2.end.y) {
                    ballsfired[1].VelocityX *= -1; // reverse cannonball's direction

                    // play blocker sound
                    soundPool.play(soundMap.get(BLOCKER_SOUND_ID), 1, 1, 1, 0, 1f);
                }else if (ballsfired[1].x + ballsfired[1].Radius > powerUpDistanceLast &&
                            ballsfired[1].x - ballsfired[1].Radius < powerUpDistanceLast &&
                            ballsfired[1].y + ballsfired[1].Radius > powerUp.start.y &&
                            ballsfired[1].y - ballsfired[1].Radius < powerUp.end.y) {
                    if(!extraTime) {
                        extraTime = true;
                        cannonballOnScreen = false;
                        timeLeft = timeLeft + 2 * HIT_REWARD; // add reward to remaining time
                        fastShot = 2;
                        // play powerup sound
                        soundPool.play(soundMap.get(EXPLODE_SOUND_ID), 1, 1, 1, 0, 1f);
                    }

                }

                    // check for collisions with left and right walls
                    else if (ballsfired[1].x + ballsfired[1].Radius > screenWidth ||
                            ballsfired[1].x - ballsfired[1].Radius < 0) {
                        ballsfired[1].cannonballOnScreen = false; // remove cannonball from screen
                        timeLeft -= MISS_PENALTY; // penalize the user
                    }
                    // check for collisions with top and bottom walls
                    else if (ballsfired[1].y + ballsfired[1].Radius > screenHeight ||
                            ballsfired[1].y - ballsfired[1].Radius < 0) {
                        ballsfired[1].cannonballOnScreen = false; // remove cannonball from screen
                        timeLeft -= MISS_PENALTY; // penalize the user
                    }
                    // check for cannonball collision with target
                    else if( ballsfired[1].x + ballsfired[1].Radius > targetDistance &&
                            ballsfired[1].x - ballsfired[1].Radius < targetDistance &&
                            ballsfired[1].y + ballsfired[1].Radius > target.start.y &&
                            ballsfired[1].y -ballsfired[1].Radius < target.end.y) {
                        // determine target section number (0 is the top)
                        int section =
                                (int) ((ballsfired[1].y - target.start.y) / pieceLength);

                        // check if the piece hasn't been hit yet
                        if ((section >= 0 && section < TARGET_PIECES) &&
                                !hitStates[section]) {
                            hitStates[section] = true; // section was hit
                            ballsfired[1].cannonballOnScreen = false; // remove cannonball
                            timeLeft += HIT_REWARD; // add reward to remaining time


                            // play target hit sound
                            soundPool.play(soundMap.get(TARGET_SOUND_ID), 1,
                                    1, 1, 0, 1f);

                            // if all pieces have been hit
                            if (++targetPiecesHit == TARGET_PIECES) {
                                if(level == 4){
                                    cannonThread.setRunning(false); // terminate thread
                                    showGameOverDialog(R.string.success); // show winning dialog
                                    gameOver = true;
                                    nextLevel = false;
                                }
                                else {
                                    cannonThread.setRunning(false); // terminate thread
                                    showGameOverDialog(R.string.win); // show winning dialog
                                    gameOver = false;
                                    nextLevel = true;
                                }
                            }
                        }
                    }
                }


        // update the blocker's position
        double blockerUpdate = interval * blockerVelocity;
        blocker.start.y += blockerUpdate;
        blocker.end.y += blockerUpdate;

        // update the blocker's position
        double blockerUpdate2 = interval * blockerVelocity2;
        blocker2.start.y += blockerUpdate2;
        blocker2.end.y += blockerUpdate2;

        // update the target's position
        double targetUpdate = interval * targetVelocity;
        target.start.y += targetUpdate;
        target.end.y += targetUpdate;

        //update the powerUp's position
        double powerUpUpdate = interval * powerUpVelocity;
        powerUp.start.y += powerUpUpdate;
        powerUp.end.y += powerUpUpdate;

        // if the blocker hit the top or bottom, reverse direction
        if (blocker.start.y < 0 || blocker.end.y > screenHeight)
            blockerVelocity *= -1;
        // if the blocker hit the top or bottom, reverse direction
        if (blocker2.start.y < 0 || blocker2.end.y > screenHeight)
            blockerVelocity2 *= -1;

        // if the target hit the top or bottom, reverse direction
        if (target.start.y < 0 || target.end.y > screenHeight)
            targetVelocity *= -1;

        if (powerUp.start.y < 0 || powerUp.end.y > screenHeight)
            powerUpVelocity *= -1;

        timeLeft -= interval; // subtract from time left

        // if the timer reached zero
        if (timeLeft <= 0.0) {
            timeLeft = 0.0;
            gameOver = true; // the game is over
            cannonThread.setRunning(false); // terminate thread
            showGameOverDialog(R.string.lose); // show the losing dialog
        }
    } // end method updatePositions
    protected void updatePositions3(double elapsedTimeMS) {
        double interval = elapsedTimeMS / 1000.0; // convert to seconds
        if (timeLeft <= 7 || TARGET_PIECES - targetPiecesHit == 1) slowmo = true;
        else slowmo = false;
        if(slowmo){
            interval = elapsedTimeMS / 3000;
            if(fastShot == 0){cannonballSpeed = originalcannonballSpeed ;
                ballsfired[1].Speed= originalcannonballSpeed;}
        }
        if(fastShot > 0){
            cannonballSpeed = 3 * originalcannonballSpeed;
            ballsfired[1].Speed = 3 * originalcannonballSpeed;
        }
        else{
            cannonballSpeed = originalcannonballSpeed;
        }

       if (cannonballOnScreen) // if there is currently a shot fired
        {
            // update cannonball position
            cannonball.x += interval * cannonballVelocityX;
            cannonball.y += interval * cannonballVelocityY;

            // check for collision with blocker
            if (cannonball.x + cannonballRadius > blockerDistance &&
                    cannonball.x - cannonballRadius < blockerDistance &&
                    cannonball.y + cannonballRadius > blocker.start.y &&
                    cannonball.y - cannonballRadius < blocker.end.y) {
                cannonballVelocityX *= -1; // reverse cannonball's direction

                // play blocker sound
                soundPool.play(soundMap.get(BLOCKER_SOUND_ID), 1, 1, 1, 0, 1f);
            } else if (cannonball.x + cannonballRadius > powerUpDistanceLast &&
                        cannonball.x - cannonballRadius < powerUpDistanceLast &&
                        cannonball.y + cannonballRadius > powerUp.start.y &&
                        cannonball.y - cannonballRadius < powerUp.end.y) {
                if(!extraTime) {
                    extraTime = true;
                    cannonballOnScreen = false;
                    timeLeft = timeLeft + 2 * HIT_REWARD; // add reward to remaining time
                    // play powerup sound
                    soundPool.play(soundMap.get(EXPLODE_SOUND_ID), 1, 1, 1, 0, 1f);
                }
            }
            // check for collisions with left and right walls
            else if (cannonball.x + cannonballRadius > screenWidth ||
                    cannonball.x - cannonballRadius < 0) {
                cannonballOnScreen = false; // remove cannonball from screen
                timeLeft -= MISS_PENALTY; // penalize the user

            }
            // check for collisions with top and bottom walls
            else if (cannonball.y + cannonballRadius > screenHeight ||
                    cannonball.y - cannonballRadius < 0) {
                cannonballOnScreen = false; // remove cannonball from screen
                timeLeft -= MISS_PENALTY; // penalize the user

            }
            // check for cannonball collision with target
            else if (cannonball.x + cannonballRadius > targetDistance &&
                    cannonball.x - cannonballRadius < targetDistance &&
                    cannonball.y + cannonballRadius > target.start.y &&
                    cannonball.y - cannonballRadius < target.end.y) {
                // determine target section number (0 is the top)
                int section =
                        (int) ((cannonball.y - target.start.y) / pieceLength);

                // check if the piece hasn't been hit yet
                if ((section >= 0 && section < TARGET_PIECES) &&
                        !hitStates[section]) {
                    hitStates[section] = true; // section was hit
                    cannonballOnScreen = false; // remove cannonball
                    timeLeft += HIT_REWARD; // add reward to remaining time


                    // play target hit sound
                    soundPool.play(soundMap.get(TARGET_SOUND_ID), 1,
                            1, 1, 0, 1f);


                    // if all pieces have been hit
                    if (++targetPiecesHit == TARGET_PIECES) {
                        cannonThread.setRunning(false); // terminate thread
                        showGameOverDialog(R.string.win); // show winning dialog
                        gameOver = false;
                        nextLevel = true;



                    }
                }
            }
        }


            if(ballsfired[1].cannonballOnScreen) {
                // update cannonball position
                ballsfired[1].x += interval * ballsfired[1].VelocityX;
                ballsfired[1].y += interval * ballsfired[1].VelocityY;

                // check for collision with blocker
                if (ballsfired[1].x +ballsfired[1].Radius > blockerDistance &&
                        ballsfired[1].x - ballsfired[1].Radius < blockerDistance &&
                        ballsfired[1].y + ballsfired[1].Radius > blocker.start.y &&
                        ballsfired[1].y - ballsfired[1].Radius < blocker.end.y) {
                    ballsfired[1].VelocityX *= -1; // reverse cannonball's direction


                    // play blocker sound
                    soundPool.play(soundMap.get(BLOCKER_SOUND_ID), 1, 1, 1, 0, 1f);
                } else if (ballsfired[1].x + ballsfired[1].Radius > powerUpDistanceLast &&
                            ballsfired[1].x - ballsfired[1].Radius < powerUpDistanceLast &&
                            ballsfired[1].y + ballsfired[1].Radius > powerUp.start.y &&
                            ballsfired[1].y - ballsfired[1].Radius < powerUp.end.y) {
                    if(!extraTime) {
                        extraTime = true;
                        cannonballOnScreen = false;
                        timeLeft = timeLeft + 2 * HIT_REWARD; // add reward to remaining time
                        // play powerup sound
                        soundPool.play(soundMap.get(EXPLODE_SOUND_ID), 1, 1, 1, 0, 1f);
                    }
                }
                // check for collisions with left and right walls
                else if (ballsfired[1].x + ballsfired[1].Radius > screenWidth ||
                        ballsfired[1].x - ballsfired[1].Radius < 0) {
                    ballsfired[1].cannonballOnScreen = false; // remove cannonball from screen
                    timeLeft -= MISS_PENALTY; // penalize the user
                }
                // check for collisions with top and bottom walls
                else if (ballsfired[1].y + ballsfired[1].Radius > screenHeight ||
                        ballsfired[1].y - ballsfired[1].Radius < 0) {
                    ballsfired[1].cannonballOnScreen = false; // remove cannonball from screen
                    timeLeft -= MISS_PENALTY; // penalize the user
                }
                // check for cannonball collision with target
                else if( ballsfired[1].x + ballsfired[1].Radius > targetDistance &&
                        ballsfired[1].x - ballsfired[1].Radius < targetDistance &&
                        ballsfired[1].y + ballsfired[1].Radius > target.start.y &&
                        ballsfired[1].y -ballsfired[1].Radius < target.end.y) {
                    // determine target section number (0 is the top)
                    int section =
                            (int) ((ballsfired[1].y - target.start.y) / pieceLength);

                    // check if the piece hasn't been hit yet
                    if ((section >= 0 && section < TARGET_PIECES) &&
                            !hitStates[section]) {
                        hitStates[section] = true; // section was hit
                        ballsfired[1].cannonballOnScreen = false; // remove cannonball
                        timeLeft += HIT_REWARD; // add reward to remaining time


                        // play target hit sound
                        soundPool.play(soundMap.get(TARGET_SOUND_ID), 1,
                                1, 1, 0, 1f);


                        // if all pieces have been hit
                        if (++targetPiecesHit == TARGET_PIECES) {
                            cannonThread.setRunning(false); // terminate thread
                            showGameOverDialog(R.string.win); // show winning dialog
                            gameOver = false;
                            nextLevel = true;


                        }
                    }
                }
            }


        // update the blocker's position
        double blockerUpdate = interval * blockerVelocity;
        blocker.start.y += blockerUpdate;
        blocker.end.y += blockerUpdate;

        // update the target's position
        double targetUpdate = interval * targetVelocity;
        target.start.y += targetUpdate;
        target.end.y += targetUpdate;

        //update the powerUp's position
        double powerUpUpdate = interval * powerUpVelocity;
        powerUp.start.y += powerUpUpdate;
        powerUp.end.y += powerUpUpdate;

        // if the blocker hit the top or bottom, reverse direction
        if (blocker.start.y < 0 || blocker.end.y > screenHeight)
            blockerVelocity *= -1;

        // if the target hit the top or bottom, reverse direction
        if (target.start.y < 0 || target.end.y > screenHeight)
            targetVelocity *= -1;

        if (powerUp.start.y < 0 || powerUp.end.y > screenHeight)
            powerUpVelocity *= -1;

        timeLeft -= interval; // subtract from time left


        // if the timer reached zero
        if (timeLeft <= 0.0) {
            timeLeft = 0.0;
            gameOver = true; // the game is over
            cannonThread.setRunning(false); // terminate thread
            showGameOverDialog(R.string.lose); // show the losing dialog
        }
    } // end method updatePositions


    // fires a cannonball
    public void fireCannonball(MotionEvent event) {

        if((totalElapsedTime - cooldown) > .25){
            if (cannonballOnScreen) {// if a cannonball is already on the screen
                if(ballsfired[1].cannonballOnScreen){
                    return;
                }
                //create instance of class cannonball
                else {
                    double angle = alignCannon(event); // get the cannon barrel's angle

                    ballsfired[1].Radius = cannonballRadius;
                    ballsfired[1].Speed = cannonballSpeed;
                    ballsfired[1].cannonballPaint = cannonballPaint;
                    ballsfired[1].originalSpeed = originalcannonballSpeed;
                    ballsfired[1].originalRadius = originalcannonballRadius;

                    // move the cannonball to be inside the cannon
                    ballsfired[1].x = ballsfired[1].Radius; // align x-coordinate with cannon
                    ballsfired[1].y = screenHeight / 2; // centers ball vertically

                    // get the x component of the total velocity
                    ballsfired[1].VelocityX = (int) (ballsfired[1].Speed * Math.sin(angle));

                    // get the y component of the total velocity
                    ballsfired[1].VelocityY = (int) (-ballsfired[1].Speed * Math.cos(angle));
                    ballsfired[1].cannonballOnScreen = true; // the cannonball is on the screen
                    ++shotsFired; // increment shotsFired

                    // play cannon fired sound
                    if(slowmo)soundPool.play(soundMap.get(POWERUP_SOUND_ID), 1, 1, 1, 0, 1f);
                    else soundPool.play(soundMap.get(CANNON_SOUND_ID), 1, 1, 1, 0, 1f);
                    cooldown = totalElapsedTime;
                    if (fastShot > 0){
                        fastShot--;
                    }
                }
                }  else {


                double angle = alignCannon(event); // get the cannon barrel's angle

                // move the cannonball to be inside the cannon
                cannonball.x = cannonballRadius; // align x-coordinate with cannon
                cannonball.y = screenHeight / 2; // centers ball vertically
                // get the x component of the total velocity
                cannonballVelocityX = (int) (cannonballSpeed * Math.sin(angle));
                // get the y component of the total velocity
                cannonballVelocityY = (int) (-cannonballSpeed * Math.cos(angle));
                cannonballOnScreen = true; // the cannonball is on the screen
                ++shotsFired; // increment shotsFired
                // play cannon fired sound

                if(slowmo)soundPool.play(soundMap.get(POWERUP_SOUND_ID), 1, 1, 1, 0, 1f);
                else soundPool.play(soundMap.get(CANNON_SOUND_ID), 1, 1, 1, 0, 1f);
                cooldown = totalElapsedTime;
                if (fastShot > 0){
                    fastShot--;
                }
            } }
        else {
            return;
        }

    } // end method fireCannonball

    // aligns the cannon in response to a user touch
    public double alignCannon(MotionEvent event) {
        // get the location of the touch in this view
        Point touchPoint = new Point((int) event.getX(), (int) event.getY());

        // compute the touch's distance from center of the screen
        // on the y-axis
        double centerMinusY = (screenHeight / 2 - touchPoint.y);

        double angle = 0; // initialize angle to 0

        // calculate the angle the barrel makes with the horizontal
        if (centerMinusY != 0) // prevent division by 0
            angle = Math.atan((double) touchPoint.x / centerMinusY);

        // if the touch is on the lower half of the screen
        if (touchPoint.y > screenHeight / 2)
            angle += Math.PI; // adjust the angle

        // calculate the endpoint of the cannon barrel
        barrelEnd.x = (int) (cannonLength * Math.sin(angle));
        barrelEnd.y =
                (int) (-cannonLength * Math.cos(angle) + screenHeight / 2);

        return angle; // return the computed angle
    } // end method alignCannon

    // draws the game to the given Canvas
    public void drawGameElements(Canvas canvas) {
        // clear the background
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(),
                backgroundPaint);

        // display time remaining
        // display changed speed based on level
        switch(level){
            case 1:textPaint.setColor(Color.BLACK);
                canvas.drawText(getResources().getString(
                    R.string.time_remaining_format, timeLeft), 30, 50, textPaint);
                break;
            case 2:canvas.drawText(getResources().getString(
                        R.string.current_speed_format, cannonballSpeed, timeLeft), 10, 50, textPaint);
                break;
            case 3:canvas.drawText(getResources().getString(
                    R.string.current_speed_format,cannonballSpeed, timeLeft), 10, 50, textPaint);
                break;
            case 4:textPaint.setColor(Color.RED);
                canvas.drawText(getResources().getString(
                    R.string.current_speed_format,cannonballSpeed, timeLeft), 10, 50, textPaint);
                break;
        }

        // if a cannonball is currently on the screen, draw it
        if (cannonballOnScreen)
            canvas.drawCircle(cannonball.x, cannonball.y, cannonballRadius,
                    cannonballPaint);
        if(ballsfired[1].cannonballOnScreen) {
            canvas.drawCircle(ballsfired[1].x, ballsfired[1].y, ballsfired[1].Radius,
                    ballsfired[1].cannonballPaint);
        }
        // draw the cannon barrel
        canvas.drawLine(0, screenHeight / 2, barrelEnd.x, barrelEnd.y,
                cannonPaint);

        // draw the cannon base
        canvas.drawCircle(0, (int) screenHeight / 2,
                (int) cannonBaseRadius, cannonPaint);

        // draw the blocker
        canvas.drawLine(blocker.start.x, blocker.start.y, blocker.end.x,
                blocker.end.y, blockerPaint);
        if (turnOnSecondBlocker){
            // draw the blocker
            canvas.drawLine(blocker2.start.x, blocker2.start.y, blocker2.end.x,
                    blocker2.end.y, blockerPaint2);
        }
        // draw the powerUp
        if(!extraTime) {
            canvas.drawLine(powerUp.start.x, powerUp.start.y, powerUp.end.x,
                    powerUp.end.y, powerUpPaint);
        }
        Point currentPoint = new Point(); // start of current target section

        // initialize currentPoint to the starting point of the target
        currentPoint.x = target.start.x;
        currentPoint.y = target.start.y;

        // draw the target
        for (int i = 0; i < TARGET_PIECES; i++) {
            // if this target piece is not hit, draw it
            if (!hitStates[i]) {
                // alternate coloring the pieces
                if (i % 2 != 0)
                    targetPaint.setColor(Color.BLUE);
                else
                    targetPaint.setColor(Color.YELLOW);

                canvas.drawLine(currentPoint.x, currentPoint.y, target.end.x,
                        (int) (currentPoint.y + pieceLength), targetPaint);
            }

            // move currentPoint to the start of the next piece
            currentPoint.y += pieceLength;
        }
    } // end method drawGameElements

    // display an AlertDialog when the game ends
    private void showGameOverDialog(final int messageId) {
        // DialogFragment to display quiz stats and start new quiz
        final DialogFragment gameResult =
                new DialogFragment() {
                    // create an AlertDialog and return it
                    @Override
                    public Dialog onCreateDialog(Bundle bundle) {
                        // create dialog displaying String resource for messageId
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(getActivity());
                        builder.setTitle(getResources().getString(messageId));

                        //Hope all of this works

                        if(resetter) {
                            AsyncTask<Object, Object, Object> saveScoreTask =
                                    new AsyncTask<Object, Object, Object>() {
                                        @Override
                                        protected Object doInBackground(Object... params) {

                                            //levelID = parseLong(LEVEL_ID);
                                            DatabaseConnector databaseConnector =
                                                    new DatabaseConnector(getActivity());

                                            // insert the contact information into the database
                                            levelID = databaseConnector.insertScore(
                                                    String.valueOf(level),
                                                    String.valueOf(totalElapsedTime));
                                            // display number of shots fired and total time elapsed

                                            return null;
                                        }

                                    };
                            saveScoreTask.execute((Object[]) null);
                        }
                        builder.setMessage(getResources().getString(
                                R.string.results_format, shotsFired, totalElapsedTime));
                        if (messageId == R.string.win){
                            soundPool.play(soundMap.get(LEVEL_SOUND_ID), 1, 1, 1, 0, 1f);

                            builder.setPositiveButton(R.string.next_level,
                                    new DialogInterface.OnClickListener() {
                                        // called when "Next Level" Button is pressed
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialogIsDisplayed = false;
                                            ++level;
                                            switch(level){
                                                case 2:
                                                    secondLevel(); //start second level
                                                    break;
                                                case 3:
                                                    thirdLevel();  //start third level
                                                    break;
                                                case 4:
                                                    fourthLevel();
                                                    break;
                                            }
                                        }
                                    } // end anonymous inner class
                            ); // end call to setPositiveButton
                        }
                        else {
                            if(messageId == R.string.success){
                                soundPool.play(soundMap.get(WINNER_SOUND_ID), 1, 1, 1, 0, 1f);
                            }
                            builder.setPositiveButton(R.string.reset_game,
                                    new DialogInterface.OnClickListener() {
                                        // called when "Reset Game" Button is pressed
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialogIsDisplayed = false;
                                            level = 1;
                                            gameOver = true;
                                            newGame(); // set up and start a new game
                                        }
                                    } // end anonymous inner class
                            ); // end call to setPositiveButton
                        }
                        return builder.create(); // return the AlertDialog
                    } // end method onCreateDialog
                }; // end DialogFragment anonymous inner class



        // in GUI thread, use FragmentManager to display the DialogFragment
        activity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        dialogIsDisplayed = true;

                        gameResult.setCancelable(false); // modal dialog
                        gameResult.show(activity.getFragmentManager(), "results");

                    }

                } // end Runnable

        ); // end call to runOnUiThread
    } // end method showGameOverDialog






    // stops the game; called by CannonGameFragment's onPause method
    public void stopGame() {
        if (cannonThread != null)
            cannonThread.setRunning(false); // tell thread to terminate
    }

    // releases resources; called by CannonGameFragment's onDestroy method
    public void releaseResources() {
        soundPool.release(); // release all resources used by the SoundPool
        soundPool = null;
    }

    // called when surface changes size
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format,
                               int width, int height) {
    }

    // called when surface is first created
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (!dialogIsDisplayed) {
            cannonThread = new CannonThread(holder, this); // create thread
            cannonThread.setRunning(true); // start game running
            cannonThread.start(); // start the game loop thread
        }
    }

    // called when the surface is destroyed
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // ensure that thread terminates properly
        boolean retry = true;
        cannonThread.setRunning(false); // terminate cannonThread

        while (retry) {
            try {
                cannonThread.join(); // wait for cannonThread to finish
                retry = false;
            } catch (InterruptedException e) {
                Log.e(TAG, "Thread interrupted", e);
            }
        }
    } // end method surfaceDestroyed

    // called when the user touches the screen in this Activity
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // get int representing the type of action which caused this event
        int action = e.getAction();
            // the user user touched the screen or dragged along the screen
            if (action == MotionEvent.ACTION_DOWN ||
                    action == MotionEvent.ACTION_MOVE) {
                fireCannonball(e); // fire the cannonball toward the touch point

        }
        return true;
    } // end method onTouchEvent

    class cannonballshot extends  Point{
        int VelocityX;
        int VelocityY;
        boolean cannonballOnScreen;
        int Radius;
        int Speed;
        int originalSpeed;
        int originalRadius;
        Paint cannonballPaint;

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


                } // end method onSharedPreferenceChanged
            };
} // end class CannonView
