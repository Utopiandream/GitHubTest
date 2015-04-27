// DatabaseConnector.java
// Provides easy connection and creation of User Scores database.
package com.dmst3b.projects.project2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;import java.lang.Override;import java.lang.String;

public class DatabaseConnector 
{
   // database name
   private static final String DATABASE_NAME = "UserScore";
      
   private SQLiteDatabase database; // for interacting with the database
   private DatabaseOpenHelper databaseOpenHelper; // creates the database

   // public constructor for DatabaseConnector
   public DatabaseConnector(Context context) 
   {
      // create a new DatabaseOpenHelper
      databaseOpenHelper = 
         new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
   }

   // open the database connection
   public void open() throws SQLException 
   {
      // create or open a database for reading/writing
      database = databaseOpenHelper.getWritableDatabase();
   }

   // close the database connection
   public void close() 
   {
      if (database != null)
         database.close(); // close the database connection
   } 

   // inserts a new score in the database
   public long insertScore(String name, String score)
   {
      ContentValues newScore = new ContentValues();
      newScore.put("name", name);
      newScore.put("score", score);

      open(); // open the database
      long levelID = database.insert("score", null, newScore);
      close(); // close the database
      return levelID;
   } 

   // updates an existing score in the database
   public void updateScore(long id, String name, String score)
   {
      ContentValues editScore = new ContentValues();
      editScore.put("name", name);
      editScore.put("score", score);


      open(); // open the database
      database.update("score", editScore, "_id=" + id, null);
      close(); // close the database
   } // end method updateScore

   // return a Cursor with all score names in the database
   public Cursor getAllScores()
   {
      return database.query("score", new String[] {"_id", "name"},
         null, null, null, null, "name");
   } 

   // return a Cursor containing specified score's information
   public Cursor getOneScore(long id)
   {
      return database.query(
         "score", null, "_id=" + id, null, null, null, null);
   } 

   // delete the score specified by the given String name
   public void deleteScore(long id)
   {
      open(); // open the database
      database.delete("score", "_id=" + id, null);
      close(); // close the database
   }

    public void clearScore()
    {
        open(); // open the database
        database.delete("score", null, null);
        close(); // close the database
    }



    private class DatabaseOpenHelper extends SQLiteOpenHelper
   {
      // constructor
      public DatabaseOpenHelper(Context context, String name,
         CursorFactory factory, int version) 
      {
         super(context, name, factory, version);
      }

      // creates the score table when the database is created
      @Override
      public void onCreate(SQLiteDatabase db) 
      {
         // query to create a new table named score
         String createQuery = "CREATE TABLE score" +
            "(_id integer primary key autoincrement," +
            "name TEXT, score TEXT);";
                  
         db.execSQL(createQuery); // execute query to create the database
      } 

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, 
          int newVersion) 
      {
      }
   } // end class DatabaseOpenHelper
} // end class DatabaseConnector
