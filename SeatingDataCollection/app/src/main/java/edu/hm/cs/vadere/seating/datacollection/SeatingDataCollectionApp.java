package edu.hm.cs.vadere.seating.datacollection;

import com.orm.SugarApp;

public class SeatingDataCollectionApp extends SugarApp {

    public static final String DATABASE_NAME = "data.db";
    public static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
