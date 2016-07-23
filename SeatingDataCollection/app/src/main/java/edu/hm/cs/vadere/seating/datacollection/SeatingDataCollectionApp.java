package edu.hm.cs.vadere.seating.datacollection;

import android.util.Log;

import com.orm.SugarApp;

public class SeatingDataCollectionApp extends SugarApp {

    public static final String DATABASE_NAME = "data.db";
    public static final int DATABASE_VERSION = 7;
    private static final String TAG = "SeatingDCApp";

    @Override
    public void onCreate() {
//        deleteDatabase();
        super.onCreate();
    }

    private void deleteDatabase() {
        Log.i(TAG, "deleting database");
        deleteDatabase(DATABASE_NAME);
    }
}
